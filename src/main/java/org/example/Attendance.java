package org.example;

import org.example.Course;
import org.example.Lecture;
import org.example.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attendance {
    private Map<Student, Map<Lecture, Boolean>> attendanceRecords;

    public Attendance() {
        attendanceRecords = new HashMap<>();
    }

    // Обновленный метод для отметки посещения с привязкой к лекции
    public void markAttendance(Student student, Lecture lecture, boolean isPresent) {
        attendanceRecords.putIfAbsent(student, new HashMap<>());
        attendanceRecords.get(student).put(lecture, isPresent);
    }

    // Метод для получения посещаемости студента по курсу
    public double getCourseAttendancePercentage(Student student, Course course) {
        if (!attendanceRecords.containsKey(student)) {
            return 0.0;
        }

        long totalLectures = attendanceRecords.get(student).keySet().stream()
                .filter(lecture -> lecture.getCourse().equals(course))
                .count();

        if (totalLectures == 0) return 0.0;

        long presentCount = attendanceRecords.get(student).entrySet().stream()
                .filter(entry -> entry.getKey().getCourse().equals(course))
                .filter(Map.Entry::getValue)
                .count();

        return (double) presentCount / totalLectures * 100;
    }

    // Новый метод: отчет по посещаемости с фильтрацией по дате
    public void generateAttendanceReport(LocalDate startDate, LocalDate endDate) {
        System.out.println("\nОтчет по посещаемости с " + startDate + " по " + endDate + ":");

        attendanceRecords.forEach((student, lectures) -> {
            System.out.println("\nСтудент: " + student.getName());

            lectures.entrySet().stream()
                    .filter(entry -> !entry.getKey().getDate().isBefore(startDate))
                    .filter(entry -> !entry.getKey().getDate().isAfter(endDate))
                    .forEach(entry -> {
                        Lecture lecture = entry.getKey();
                        System.out.printf("Дата: %s | Предмет: %s | Тема: %s | Присутствие: %s\n",
                                lecture.getDate(),
                                lecture.getCourse().getCourseName(),
                                lecture.getTopic(),
                                entry.getValue() ? "Да" : "Нет");
                    });

            // Вывод общей статистики по студенту
            lectures.keySet().stream()
                    .map(Lecture::getCourse)
                    .distinct()
                    .forEach(course -> {
                        double percentage = getCourseAttendancePercentage(student, course);
                        System.out.printf("Общая посещаемость по %s: %.2f%%\n",
                                course.getCourseName(), percentage);
                    });
        });
    }

    // Новый метод: поиск студентов с низкой посещаемостью
    public List<Student> getStudentsWithLowAttendance(double threshold) {
        List<Student> result = new ArrayList<>();

        attendanceRecords.forEach((student, lectures) -> {
            lectures.keySet().stream()
                    .map(Lecture::getCourse)
                    .distinct()
                    .forEach(course -> {
                        double percentage = getCourseAttendancePercentage(student, course);
                        if (percentage < threshold) {
                            if (!result.contains(student)) {
                                result.add(student);
                            }
                        }
                    });
        });

        return result;
    }
}