package org.example;

import java.time.LocalDate;

public class Lecture {
    private Course course;
    private LocalDate date;
    private String topic;

    public Lecture(Course course, LocalDate date, String topic) {
        this.course = course;
        this.date = date;
        this.topic = topic;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "course=" + course.getCourseName() +
                ", date=" + date +
                ", topic='" + topic + '\'' +
                '}';
    }
}
