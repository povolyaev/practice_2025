@echo off
setlocal enabledelayedexpansion

:: =============================================
:: Configuration
:: =============================================
mode con:cols=160 lines=4000

set ARTIFACT_DIR=%CD%\artifacts
set MIN_COVERAGE=30
set PARENT_POM=pom.xml

:: Правильное задание параметров Maven
set MAVEN_OPTS=-Xmx1024m
set MAVEN_OPTS=!MAVEN_OPTS! -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn

if not exist "%ARTIFACT_DIR%" mkdir "%ARTIFACT_DIR%"

:: =============================================
:: 1. Validate Input
:: =============================================
if "%~1"=="" (
    echo [ERROR] Branch name not specified
    echo Usage: %~nx0 branch_name
    exit /b 1
)
set TARGET_BRANCH=%~1
echo [INFO] Starting pipeline for branch: %TARGET_BRANCH%

:: =============================================
:: 2. Git Operations
:: =============================================
echo [1/7] Fetching source code...
git fetch origin
if errorlevel 1 (
    echo [ERROR] Git fetch failed
    exit /b 1
)

echo [DEBUG] Checking out branch: %TARGET_BRANCH%
git checkout %TARGET_BRANCH%
if errorlevel 1 (
    echo [ERROR] Checkout failed for branch: %TARGET_BRANCH%
    exit /b 1
)

git pull origin %TARGET_BRANCH%
if errorlevel 1 (
    echo [ERROR] Git pull failed
    exit /b 1
)

:: =============================================
:: 3. Compilation
:: =============================================
echo [2/7] Compiling source code...
mvn clean compile -f "%PARENT_POM%" > "%ARTIFACT_DIR%\compile.log" 2>&1
if errorlevel 1 (
    echo [ERROR] ❌ Main compilation failed!
    type "%ARTIFACT_DIR%\compile.log" | findstr /i "error fail exception"
    exit /b 1
)

echo [3/7] Compiling tests...
mvn test-compile -f "%PARENT_POM%" > "%ARTIFACT_DIR%\test_compile.log" 2>&1
if errorlevel 1 (
    echo [WARNING] ⚠️ Test compilation warnings
    type "%ARTIFACT_DIR%\test_compile.log" | findstr /i "warning"
)

:: =============================================
:: 4. Testing (Feature Branches Only)
:: =============================================
echo "%TARGET_BRANCH%" | findstr /i /r /c:"^feature/" >nul
if errorlevel 0 (
    echo [4/7] Running tests for feature branch...
    mvn test -f "%PARENT_POM%" > "%ARTIFACT_DIR%\tests.log" 2>&1
    if errorlevel 1 (
        echo [ERROR] Tests failed
        type "%ARTIFACT_DIR%\tests.log" | findstr /i "error fail exception"
        exit /b 1
    )
) else (
    echo [4/7] Skipping tests (not a feature branch)
)

:: =============================================
:: 5. Static Analysis (Develop Branch Only)
:: =============================================
if /i "%TARGET_BRANCH%"=="develop" (
    echo [5/7] Running static analysis...
    mvn checkstyle:check -f "%PARENT_POM%" > "%ARTIFACT_DIR%\checkstyle.log" 2>&1
    if errorlevel 1 (
        echo [ERROR] Checkstyle violations found
        type "%ARTIFACT_DIR%\checkstyle.log" | findstr /i "error violation"
        exit /b 1
    )
) else (
    echo [5/7] Skipping static analysis
)

:: =============================================
:: 6. Coverage Analysis
:: =============================================
echo [6/7] Measuring test coverage...
mvn jacoco:prepare-agent install jacoco:report -f "%PARENT_POM%" > "%ARTIFACT_DIR%\coverage.log" 2>&1
if errorlevel 1 (
    echo [ERROR] Coverage measurement failed
    exit /b 1
)

mvn jacoco:check -f "%PARENT_POM%" > "%ARTIFACT_DIR%\coverage_check.log" 2>&1
findstr /C:"Coverage checks have not been met" "%ARTIFACT_DIR%\coverage_check.log" >nul
if errorlevel 0 (
    echo [ERROR] Code coverage below required %MIN_COVERAGE%%
    type "%ARTIFACT_DIR%\coverage_check.log" | findstr /C:"Rule violated"
    exit /b 1
)

:: =============================================
:: 7. Artifacts Handling
:: =============================================
echo [7/7] Preparing artifacts...
mvn install -DskipTests -f "%PARENT_POM%"
if errorlevel 1 (
    echo [ERROR] Artifact installation failed
    exit /b 1
)

:: Копируем артефакты
xcopy /Y "app-module\target\*.jar" "%ARTIFACT_DIR%\" >nul
xcopy /Y "core-module\target\*.jar" "%ARTIFACT_DIR%\" >nul

:: =============================================
:: Final Output
:: =============================================
echo.
echo ============================================
echo PIPELINE SUCCESSFULLY COMPLETED
echo Artifacts: %ARTIFACT_DIR%
echo Coverage: %CD%\target\site\jacoco\index.html
echo ============================================

endlocal
exit /b 0