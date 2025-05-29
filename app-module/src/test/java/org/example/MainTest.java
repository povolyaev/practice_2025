package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    private Main main;
    private Attendance attendance;
    private Student student1;
    private Student student2;
    private Course math;
    private Course physics;

    @BeforeEach
    void setUp() {
        main = new Main();
        attendance = new Attendance();
        student1 = new Student("Иван Иванов", 1001);
        student2 = new Student("Петр Петров", 1002);
        math = new Course("Математика", "MATH101");
        physics = new Course("Физика", "PHYS201");
    }

    @Test
    void testAttendanceMarking() {
        Lecture mathLecture = new Lecture(math, LocalDate.of(2023, 9, 1), "Введение в алгебру");
        attendance.markAttendance(student1, mathLecture, true);
        assertTrue(attendance.getAttendanceFor(student1, mathLecture));
    }

    @Test
    void testLowAttendanceStudents() {
        // Настройка тестовых данных
        Lecture mathLecture = new Lecture(math, LocalDate.of(2023, 9, 1), "Введение в алгебру");
        Lecture physicsLecture = new Lecture(physics, LocalDate.of(2023, 9, 2), "Механика");

        // Студент 1 посетил 1 из 2 лекций (50%)
        attendance.markAttendance(student1, mathLecture, true);
        attendance.markAttendance(student1, physicsLecture, false);

        // Студент 2 не посетил ни одной лекции (0%)
        attendance.markAttendance(student2, mathLecture, false);
        attendance.markAttendance(student2, physicsLecture, false);

        // Проверка
        List<Student> lowAttendanceStudents = attendance.getStudentsWithLowAttendance(50.0);
        assertEquals(2, lowAttendanceStudents.size());
        assertEquals(student1, lowAttendanceStudents.get(0));
    }

    @Test
    void testCourseAttendancePercentage() {
        Lecture mathLecture1 = new Lecture(math, LocalDate.of(2023, 9, 1), "Введение в алгебру");
        Lecture mathLecture2 = new Lecture(math, LocalDate.of(2023, 9, 8), "Линейные уравнения");

        // Студент посетил 1 из 2 лекций (50%)
        attendance.markAttendance(student1, mathLecture1, true);
        attendance.markAttendance(student1, mathLecture2, false);

        double percentage = attendance.getCourseAttendancePercentage(student1, math);
        assertEquals(50.0, percentage, 0.01); // Погрешность 0.01
    }
}