package org.example;
// Программа должна обрабатывать информацию о посещаемости студентами университета. Она должна уметь:
// Хранить данные о студентах, предметах и посещаемости
// Отмечать посещения лекций
// Вычислять процент посещаемости для каждого студента
// Формировать отчеты по посещаемости


import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Создаем студентов
        Student student1 = new Student("Иван Иванов", 1001);
        Student student2 = new Student("Петр Петров", 1002);

        // Создаем предметы
        Course math = new Course("Математика", "MATH101");
        Course physics = new Course("Физика", "PHYS201");

        // Создаем лекции
        Lecture mathLecture1 = new Lecture(math, LocalDate.of(2023, 9, 1), "Введение в алгебру");
        Lecture mathLecture2 = new Lecture(math, LocalDate.of(2023, 9, 8), "Линейные уравнения");
        Lecture physicsLecture1 = new Lecture(physics, LocalDate.of(2023, 9, 2), "Механика");
        Lecture physicsLecture2 = new Lecture(physics, LocalDate.of(2023, 9, 9), "Термодинамика");

        // Создаем систему учета посещаемости
        Attendance attendance = new Attendance();

        // Отмечаем посещения
        attendance.markAttendance(student1, mathLecture1, true);
        attendance.markAttendance(student1, mathLecture2, true);
        attendance.markAttendance(student1, physicsLecture1, true);
        attendance.markAttendance(student1, physicsLecture2, false);

        attendance.markAttendance(student2, mathLecture1, true);
        attendance.markAttendance(student2, mathLecture2, false);
        attendance.markAttendance(student2, physicsLecture1, false);
        attendance.markAttendance(student2, physicsLecture2, false);

        // Генерируем отчет за период
        LocalDate startDate = LocalDate.of(2023, 9, 1);
        LocalDate endDate = LocalDate.of(2023, 9, 10);
        attendance.generateAttendanceReport(startDate, endDate);

        // Проверяем студентов с низкой посещаемостью
        List<Student> lowAttendanceStudents = attendance.getStudentsWithLowAttendance(50.0);
        System.out.println("\nСтуденты с посещаемостью ниже 50%:");
        lowAttendanceStudents.forEach(student ->
                System.out.println(student.getName() + " (ID: " + student.getStudentId() + ")"));

        // Дополнительная проверка посещаемости по предмету
        System.out.println("\nДетальная статистика:");
        double mathAttendance = attendance.getCourseAttendancePercentage(student1, math);
        System.out.printf("%s посещал %s на %.2f%%\n",
                student1.getName(), math.getCourseName(), mathAttendance);
    }
}