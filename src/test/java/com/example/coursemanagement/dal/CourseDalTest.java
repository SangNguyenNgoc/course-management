package com.example.coursemanagement.dal;

import com.example.coursemanagement.bll.dtos.Course;
import com.example.coursemanagement.bll.dtos.OnlineCourse;
import com.example.coursemanagement.bll.utils.DbConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
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

    @Test
    void test() {
        Connection connection = DbConnection.getInstance().getConnection();
        DbConnection.getInstance().printInfo(connection);
    }
}