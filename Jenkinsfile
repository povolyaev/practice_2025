pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: env.BRANCH_NAME, url: 'https://github.com/povolyaev/practice_2025.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            when {
                expression { return env.BRANCH_NAME.startsWith('feature/') }
            }
            steps {
                sh 'mvn test'
            }
        }

        stage('Static Analysis') {
            when {
                expression { return env.BRANCH_NAME == 'develop' }
            }
            steps {
                sh 'mvn checkstyle:checkstyle'
                checkStyle canComputeNew: false, pattern: '**/target/checkstyle-result.xml', showViolations: true
            }
        }

        stage('Code Coverage') {
            steps {
                sh 'mvn jacoco:report'
                jacoco execPattern: '**/target/jacoco.exec', classPattern: '**/target/classes', sourcePattern: '**/src/main/java'
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    def coverage = readJSON file: 'target/site/jacoco/index.json'
                    def lineCoverage = coverage.counter[0].covered * 100 / coverage.counter[0].total
                    if (lineCoverage < 30) {
                        error "Line coverage is ${lineCoverage}% which is below the required 30%"
                    } else {
                        echo "Line coverage is OK: ${lineCoverage}%"
                    }
                }
            }
        }

        stage('Install Artifacts') {
            steps {
                sh 'mvn install'
            }
        }

        stage('Publish Artifact') {
            steps {
                script {
                    def artifactPath = 'target/your-artifact.jar'
                    def targetDir = '/opt/jenkins/artifacts/'
                    sh "cp ${artifactPath} ${targetDir}"
                }
            }
        }
    }
}