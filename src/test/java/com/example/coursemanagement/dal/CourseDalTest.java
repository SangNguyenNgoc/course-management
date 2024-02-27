package com.example.coursemanagement.dal;

import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.OnlineCourse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CourseDalTest {

    @Test
    void getAllCourses() {
        List<Course> courses = CourseDal.getInstance().getAll();
        courses.forEach(course -> {
            if(course instanceof OnlineCourse) {
                System.out.println(course.toString());
            } else {
                System.out.println(course.toString());
            }
        });
    }

    @Test
    void registerStudentForCourse() {
        int result = CourseDal.getInstance().registerStudentForCourse(4,1045);
        assertNotEquals(result, 0);
    }
}