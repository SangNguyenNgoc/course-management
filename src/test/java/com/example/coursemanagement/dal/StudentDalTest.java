package com.example.coursemanagement.dal;

import com.example.coursemanagement.dtos.StudentGrace;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentDalTest {

    @Test
    void getStudentsInCourse() {
        List<StudentGrace> studentGraces = StudentDal.getInstance().getStudentsInCourse(1045);
        studentGraces.forEach(item -> {
            System.out.println(item.getEnrollmentDate());
        });
    }
}