package org.example;

public class Course {
    private String courseName;
    private String courseCode;

    public Course(String courseName, String courseCode) {
        this.courseName = courseName;
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public String toString() {
        return "Course{courseName='" + this.courseName + "', courseCode='" + this.courseCode + "'}";
    }
}
