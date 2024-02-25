package com.example.coursemanagement.bll.interfaces;

import com.example.coursemanagement.dtos.Course;

import java.util.List;
import java.util.Optional;

public interface ICourseBll {
    List<Course> getAllCourse();

    Course getById(Integer courseId);

    void registerStudentForCourse(String personId, Integer courseId) throws Exception;

    Optional<Course> createCourse(String title, String name, String credits, Integer departmentId, String url);

    Optional<Course> createCourse(String title, String name, String credits, Integer departmentId, String location, String days, String time);

    void deleteCourse(Integer courseId) throws Exception;

    int updateCourse(Course course);
}
