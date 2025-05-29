package org.example;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class LectureTest {
    @Test
    void testLectureCreation() {
        Course course = new Course("Биология", "BIO404");
        LocalDate date = LocalDate.of(2023, 10, 20);
        Lecture lecture = new Lecture(course, date, "Клеточная теория");

        assertEquals(course, lecture.getCourse());
        assertEquals(date, lecture.getDate());
        assertEquals("Клеточная теория", lecture.getTopic());
    }

    @Test
    void testToString() {
        Course course = new Course("История", "HIST505");
        LocalDate date = LocalDate.of(2023, 11, 1);
        Lecture lecture = new Lecture(course, date, "Древний мир");
        String expected = "Lecture{course=История, date=2023-11-01, topic='Древний мир'}";
        assertEquals(expected, lecture.toString());
    }
}