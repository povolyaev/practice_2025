package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attendance {
    private Map<Student, Map<Lecture, Boolean>> attendanceRecords = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(Attendance.class);
    public Attendance() {
    }

    public void markAttendance(Student student, Lecture lecture, boolean isPresent) {
        if (student == null || lecture == null) {
            throw new IllegalArgumentException("Student and Lecture cannot be null");
        }
        attendanceRecords
                .computeIfAbsent(student, k -> new HashMap<>())
                .put(lecture, isPresent);
    }

    public double getCourseAttendancePercentage(Student student, Course course) {
        if (!attendanceRecords.containsKey(student)) {
            logger.warn("Для студента(ки) {} нет записей", student.getName());
            return 0.0;
        }

        Map<Lecture, Boolean> studentLectures = attendanceRecords.get(student);
        long total = studentLectures.keySet().stream()
                .filter(lecture -> lecture.getCourse().equals(course))
                .count();
        long totalLectures = studentLectures.keySet().stream()
                .filter(lecture -> lecture.getCourse().equals(course))
                .count();

        if (totalLectures == 0L) {
            logger.debug("По курсу {} не было лекций", course.getCourseName());
            return 0.0;
        }

        long present = studentLectures.entrySet().stream()
                .filter(e -> e.getKey().getCourse().equals(course) && e.getValue())
                .count();

        long presentCount = studentLectures.entrySet().stream()
                .filter(entry -> entry.getKey().getCourse().equals(course))
                .filter(Map.Entry::getValue)
                .count();

        double percentage = (double) present / total * 100;
        logger.info("Посещаемость студента {} на курсе {}: {}%",
                student.getName(), course.getCourseName(), percentage);

        return percentage;
    }

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

    public List<Student> getStudentsWithLowAttendance(double threshold) {
        List<Student> result = new ArrayList<>();

        attendanceRecords.forEach((student, lectures) -> {
            lectures.keySet().stream()
                    .map(Lecture::getCourse)
                    .distinct()
                    .forEach(course -> {
                        double percentage = getCourseAttendancePercentage(student, course);
                        if (percentage < threshold && !result.contains(student)) {
                            result.add(student);
                        }
                    });
        });

        return result;
    }

    public boolean getAttendanceFor(Student student, Lecture lecture) {
        if (student == null || lecture == null) {
            throw new IllegalArgumentException("Student and Lecture cannot be null");
        }
        return attendanceRecords
                .getOrDefault(student, new HashMap<>())
                .getOrDefault(lecture, false);
    }

    // Метод для доступа к записям посещаемости (может быть полезен для тестов)
    public Map<Student, Map<Lecture, Boolean>> getAttendanceRecords() {
        return attendanceRecords;
    }
}