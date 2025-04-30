package org.example;
// Программа должна обрабатывать информацию о посещаемости студентами университета. Она должна уметь:
// Хранить данные о студентах, предметах и посещаемости
// Отмечать посещения лекций
// Вычислять процент посещаемости для каждого студента
// Формировать отчеты по посещаемости


public class Main {
    public static void main(String[] args) {
        // Создаем студентов
        Student student1 = new Student("Иван Иванов", 1001);
        Student student2 = new Student("Петр Петров", 1002);

        // Создаем предметы
        Course math = new Course("Математика", "MATH101");
        Course physics = new Course("Физика", "PHYS201");

        // Создаем систему учета посещаемости
        Attendance attendance = new Attendance();

        // Отмечаем посещения
        attendance.markAttendance(student1, math, true);
        attendance.markAttendance(student1, math, true);
        attendance.markAttendance(student1, math, false);
        attendance.markAttendance(student1, physics, true);

        attendance.markAttendance(student2, math, true);
        attendance.markAttendance(student2, math, false);
        attendance.markAttendance(student2, physics, false);
        attendance.markAttendance(student2, physics, false);

        // Генерируем отчет
        attendance.generateAttendanceReport();

        // Выводим посещаемость конкретного студента по предмету
        System.out.println("\nДетальная информация:");
        double mathAttendance = attendance.getAttendancePercentage(student1, math);
        System.out.printf("%s посещал %s на %.2f%%\n",
                student1.getName(), math.getCourseName(), mathAttendance);
    }
}