package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {
    @Test
    void testCourseCreation() {
        Course course = new Course("Физика", "PHYS202");
        assertEquals("Физика", course.getCourseName());
        assertEquals("PHYS202", course.getCourseCode());
    }

    @Test
    void testToString() {
        Course course = new Course("Химия", "CHEM303");
        String expected = "Course{courseName='Химия', courseCode='CHEM303'}";
        assertEquals(expected, course.toString());
    }
}