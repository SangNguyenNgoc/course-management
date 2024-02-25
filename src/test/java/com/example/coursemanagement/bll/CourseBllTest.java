package com.example.coursemanagement.bll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseBllTest {

    @Test
    void registerStudentForCourse() {
        try {
            CourseBll.getInstance().registerStudentForCourse("7", 1045);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}