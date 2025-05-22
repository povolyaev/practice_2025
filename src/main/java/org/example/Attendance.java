package org.example;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Attendance {
    // Изменили структуру данных - теперь храним для каждой лекции только последний статус
    private Map<Student, Map<Lecture, Boolean>> attendanceRecords;

    public Attendance() {
        attendanceRecords = new HashMap<>();
    }

    // Метод для отметки посещения (теперь перезаписываем статус)
    public void markAttendance(Student student, Lecture lecture, boolean isPresent) {
        attendanceRecords.putIfAbsent(student, new HashMap<>());
        attendanceRecords.get(student).put(lecture, isPresent);
    }

    // Метод для получения процента посещаемости студента по предмету
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

    // Отчет по посещаемости
    public void generateAttendanceReport(LocalDate startDate, LocalDate endDate) {
        System.out.println("\nОтчет по посещаемости с " + startDate + " по " + endDate + ":");

        attendanceRecords.forEach((student, lecturesMap) -> {
            System.out.println("\nСтудент: " + student.getName());

            // Фильтрация лекций по дате и вывод
            lecturesMap.entrySet().stream()
                    .filter(entry -> {
                        LocalDate lectureDate = entry.getKey().getDate();
                        return !lectureDate.isBefore(startDate) && !lectureDate.isAfter(endDate);
                    })
                    .forEach(entry -> {
                        Lecture lecture = entry.getKey();
                        System.out.printf("Дата: %s | Предмет: %s | Тема: %s | Присутствие: %s\n",
                                lecture.getDate(),
                                lecture.getCourse().getCourseName(),
                                lecture.getTopic(),
                                entry.getValue() ? "Да" : "Нет");
                    });

            // Статистика по курсам
            lecturesMap.keySet().stream()
                    .collect(Collectors.groupingBy(Lecture::getCourse))
                    .forEach((course, lectures) -> {
                        double percentage = getCourseAttendancePercentage(student, course);
                        System.out.printf("Общая посещаемость по %s: %.2f%%\n",
                                course.getCourseName(), percentage);
                    });
        });
    }

    // Поиск студентов с низкой посещаемостью
    public List<Student> getStudentsWithLowAttendance(double threshold) {
        return attendanceRecords.keySet().stream()
                .filter(student -> attendanceRecords.get(student).keySet().stream()
                        .collect(Collectors.groupingBy(Lecture::getCourse))
                        .entrySet().stream()
                        .anyMatch(entry -> {
                            Course course = entry.getKey();
                            double percentage = getCourseAttendancePercentage(student, course);
                            return percentage < threshold;
                        }))
                .collect(Collectors.toList());
    }
}