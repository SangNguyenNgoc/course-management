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

    @Test
    void updateCourse() {
        int result = 0;
        try {
            result = CourseBll.getInstance().updateCourse(2021, "Composition", "4", 1, "bbbbb",4);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertNotEquals(0, result);
    }

    @Test
    void testUpdateCourse() {
    }
}