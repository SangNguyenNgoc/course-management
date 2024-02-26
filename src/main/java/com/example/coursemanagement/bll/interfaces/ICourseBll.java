package com.example.coursemanagement.bll.interfaces;

import com.example.coursemanagement.dtos.Course;

import java.util.List;
import java.util.Optional;

public interface ICourseBll {
    List<Course> getAllCourse();

    Course getById(Integer courseId);

    void registerStudentForCourse(String personId, Integer courseId) throws Exception;

    Optional<Course> createCourse(String title, String credits, Integer departmentId, String url, Integer teacherId) throws Exception;

    Optional<Course> createCourse(String title, String credits, Integer departmentId, String location, String days, String time, Integer teacherId) throws Exception;

    void deleteCourse(Integer courseId) throws Exception;

    int updateCourse(Integer courseId, String title, String credits, Integer departmentId, String url, Integer teacherId) throws Exception;

    int updateCourse(Integer courseId, String title, String credits, Integer departmentId, String location, String days, String time, Integer teacherId) throws Exception;
}
