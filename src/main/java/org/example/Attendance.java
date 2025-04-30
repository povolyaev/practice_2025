package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attendance {
    private Map<Student, Map<Course, List<Boolean>>> attendanceRecords;

    public Attendance() {
        attendanceRecords = new HashMap<>();
    }

    // Метод для отметки посещения
    public void markAttendance(Student student, Course course, boolean isPresent) {
        attendanceRecords.putIfAbsent(student, new HashMap<>());
        attendanceRecords.get(student).putIfAbsent(course, new ArrayList<>());
        attendanceRecords.get(student).get(course).add(isPresent);
    }

    // Метод для получения процента посещаемости студента по предмету
    public double getAttendancePercentage(Student student, Course course) {
        if (!attendanceRecords.containsKey(student)) {
            return 0.0;
        }

        Map<Course, List<Boolean>> studentCourses = attendanceRecords.get(student);
        if (!studentCourses.containsKey(course)) {
            return 0.0;
        }

        List<Boolean> attendances = studentCourses.get(course);
        long presentCount = attendances.stream().filter(b -> b).count();
        return (double) presentCount / attendances.size() * 100;
    }

    // Метод для генерации отчета по посещаемости
    public void generateAttendanceReport() {
        System.out.println("Отчет по посещаемости:");
        for (Student student : attendanceRecords.keySet()) {
            System.out.println("\nСтудент: " + student.getName());
            for (Course course : attendanceRecords.get(student).keySet()) {
                double percentage = getAttendancePercentage(student, course);
                System.out.printf("Предмет: %s - Посещаемость: %.2f%%\n",
                        course.getCourseName(), percentage);
            }
        }
    }
}
