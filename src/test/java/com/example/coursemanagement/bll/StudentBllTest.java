package com.example.coursemanagement.bll;

import com.example.coursemanagement.dal.StudentDal;
import com.example.coursemanagement.dtos.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static javafx.beans.binding.Bindings.when;
import static org.junit.jupiter.api.Assertions.*;

class StudentBllTest {
    private StudentBll studentBll;

    @BeforeEach
    public void setUp() {
        studentBll = StudentBll.getInstance();
    }

    @Test
    public void testAddStudent() {
        Student student = new Student();
        student.setId(38);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEnrollmentDate(new Date(System.currentTimeMillis()));

        int result = studentBll.addStudent("John", "Doe", LocalDate.now());

        assertEquals(1, result);
    }

    @Test
    public void testUpdateStudent() {
        Student student = new Student();
        student.setId(38); // ID của học sinh cần cập nhật
        student.setFirstName("Michal");
        student.setLastName("Smith");
        student.setEnrollmentDate(new Date(System.currentTimeMillis()));

//        int result = studentBll.updateStudent(student);

//        assertEquals(1, result);
    }

    @Test
    void deleteStudent() {

        int result = studentBll.deleteStudent(38);

        assertEquals(1, result);
    }
}