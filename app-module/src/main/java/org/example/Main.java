package org.example;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;

import org.example.*;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        Student student1 = new Student("Иван Иванов", 1001);
        Student student2 = new Student("Петр Петров", 1002);
        Course math = new Course("Математика", "MATH101");
        Course physics = new Course("Физика", "PHYS201");
        Lecture mathLecture1 = new Lecture(math, LocalDate.of(2023, 9, 1), "Введение в алгебру");
        Lecture mathLecture2 = new Lecture(math, LocalDate.of(2023, 9, 8), "Линейные уравнения");
        Lecture physicsLecture1 = new Lecture(physics, LocalDate.of(2023, 9, 2), "Механика");
        Lecture physicsLecture2 = new Lecture(physics, LocalDate.of(2023, 9, 9), "Термодинамика");
        Attendance attendance = new Attendance();
        attendance.markAttendance(student1, mathLecture1, true);
        attendance.markAttendance(student1, mathLecture2, true);
        attendance.markAttendance(student1, physicsLecture1, true);
        attendance.markAttendance(student1, physicsLecture2, false);
        attendance.markAttendance(student2, mathLecture1, true);
        attendance.markAttendance(student2, mathLecture2, false);
        attendance.markAttendance(student2, physicsLecture1, false);
        attendance.markAttendance(student2, physicsLecture2, false);
        LocalDate startDate = LocalDate.of(2023, 9, 1);
        LocalDate endDate = LocalDate.of(2023, 9, 10);
        attendance.generateAttendanceReport(startDate, endDate);
        List<Student> lowAttendanceStudents = attendance.getStudentsWithLowAttendance(50.0);
        System.out.println("\nСтуденты с посещаемостью ниже 50%:");
        lowAttendanceStudents.forEach((student) -> {
            PrintStream var10000 = System.out;
            String var10001 = student.getName();
            var10000.println(var10001 + " (ID: " + student.getStudentId() + ")");
        });
        System.out.println("\nДетальная статистика:");
        double mathAttendance = attendance.getCourseAttendancePercentage(student1, math);
        System.out.printf("%s посещал %s на %.2f%%\n", student1.getName(), math.getCourseName(), mathAttendance);
    }
}