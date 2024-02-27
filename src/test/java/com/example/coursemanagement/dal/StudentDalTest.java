package com.example.coursemanagement.dal;

import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.dtos.StudentGrace;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentDalTest {

    @Test
    void getStudentsInCourse() {
        List<StudentGrace> studentGraces = StudentDal.getInstance().getStudentsInCourse(4041);
        studentGraces.forEach(item -> {
            System.out.println(item.getGrade());
        });
    }


    @Test
    void getInstance() {
    }

    @Test
    void getById() {
        Student student = StudentDal.getInstance().getById(2).orElse(null);
        assertNotNull(student);
    }

    @Test
    void updateGrade() {
        int t = StudentDal.getInstance().updateGrade(2,2030, 7.8);
        assertNotEquals(0, t);
    }
}