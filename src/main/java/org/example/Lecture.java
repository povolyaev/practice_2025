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
        return this.course;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public String getTopic() {
        return this.topic;
    }

    public String toString() {
        String var10000 = this.course.getCourseName();
        return "Lecture{course=" + var10000 + ", date=" + String.valueOf(this.date) + ", topic='" + this.topic + "'}";
    }
}
