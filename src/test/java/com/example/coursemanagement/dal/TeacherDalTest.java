package com.example.coursemanagement.dal;

import com.example.coursemanagement.dtos.Teacher;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherDalTest {

    @Test
    void addTeacher() {
        TeacherDal.getInstance().addTeacher(Teacher.builder()
                        .lastName("Hoang")
                        .firstName("Minh").hireDate(LocalDateTime.now())
                .build());
    }

    @Test
    void updateTeacher() {
        TeacherDal.getInstance().updateTeacher(Teacher.builder()
                .lastName("Sang")
                .firstName("Minh").hireDate(LocalDateTime.now()).id(36)
                .build());
    }

    @Test
    void deleteTeacher() {
        TeacherDal.getInstance().deleteTeacher(36);
    }
}