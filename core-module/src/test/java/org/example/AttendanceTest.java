package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AttendanceTest {
    private Attendance attendance;
    private Student student;
    private Lecture lecture;
    private Course course;

    @BeforeEach
    void setUp() {
        attendance = new Attendance();
        student = new Student("Иван Иванов", 1);
        course = new Course("Математика", "MATH101");
        lecture = new Lecture(course, LocalDate.now(), "Алгебра");
    }

    @Test
    void testMarkAttendance_Present() {
        attendance.markAttendance(student, lecture, true);
        assertTrue(attendance.getAttendanceFor(student, lecture));
    }

    @Test
    void testMarkAttendance_Absent() {
        attendance.markAttendance(student, lecture, false);
        assertFalse(attendance.getAttendanceFor(student, lecture));
    }

    @Test
    void testMarkAttendance_NullStudent_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                attendance.markAttendance(null, lecture, true)
        );
    }

    @Test
    void testMarkAttendance_NullLecture_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                attendance.markAttendance(student, null, true)
        );
    }

    @Test
    void testGetAttendanceFor_UnknownStudent() {
        Student unknownStudent = new Student("Неизвестный", 999);
        assertFalse(attendance.getAttendanceFor(unknownStudent, lecture));
    }

    @Test
    void testGetAttendanceRecords_InitialEmpty() {
        assertTrue(attendance.getAttendanceRecords().isEmpty());
    }

    @Test
    void testGetAttendanceRecords_AfterMarking() {
        attendance.markAttendance(student, lecture, true);
        assertEquals(1, attendance.getAttendanceRecords().size());
    }

    @Test
    void testGetAttendanceFor_NullStudent_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                attendance.getAttendanceFor(null, lecture)
        );
        assertEquals("Student and Lecture cannot be null", exception.getMessage());
    }

    @Test
    void testGetAttendanceFor_NullLecture_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                attendance.getAttendanceFor(student, null)
        );
        assertEquals("Student and Lecture cannot be null", exception.getMessage());
    }

    @Test
    void testMarkAttendance_BothNull_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                attendance.markAttendance(null, null, true)
        );
        assertEquals("Student and Lecture cannot be null", exception.getMessage());
    }

    @Test
    void testGetAttendanceRecords_AfterMultipleMarks() {
        // Первая отметка
        attendance.markAttendance(student, lecture, true);
        // Вторая отметка для того же студента и лекции (перезапись)
        attendance.markAttendance(student, lecture, false);

        assertFalse(attendance.getAttendanceFor(student, lecture));
        assertEquals(1, attendance.getAttendanceRecords().size()); // Проверяем, что запись перезаписана
    }
}