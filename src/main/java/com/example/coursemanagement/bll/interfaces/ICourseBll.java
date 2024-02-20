package com.example.coursemanagement.bll.interfaces;

import com.example.coursemanagement.dtos.Course;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ICourseBll {
    List<Course> getAllCourse();

    Course getById(Integer courseId);

    int registerStudentForCourse(Integer personId, Integer courseId);

    Optional<Course> createCourse(String title, String personId, Integer credits, Integer departmentId, String url);

    Optional<Course> createCourse(String title, String personId, Integer credits, Integer departmentId, String location, String days, LocalTime time);


    int deleteCourse(Integer courseId);

    int updateCourse(Course course);
}
