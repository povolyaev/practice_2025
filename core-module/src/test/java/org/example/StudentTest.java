package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {
    @Test
    void testStudentCreation() {
        Student student = new Student("Анна Петрова", 2);
        assertEquals("Анна Петрова", student.getName());
        assertEquals(2, student.getStudentId());
    }

    @Test
    void testToString() {
        Student student = new Student("Сергей Сидоров", 3);
        String expected = "Student{name='Сергей Сидоров', studentId=3}";
        assertEquals(expected, student.toString());
    }
}