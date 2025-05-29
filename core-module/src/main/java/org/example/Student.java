package org.example;

public class Student {
    private String name;
    private int studentId;

    public Student(String name, int studentId) {
        this.name = name;
        this.studentId = studentId;
    }

    public String getName() {
        return this.name;
    }

    public int getStudentId() {
        return this.studentId;
    }

    public String toString() {
        return "Student{name='" + this.name + "', studentId=" + this.studentId + "}";
    }
}
