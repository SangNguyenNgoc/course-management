package com.example.coursemanagement.bll.interfaces;

import com.example.coursemanagement.dtos.Course;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ICourseBll {
    List<Course> getAllCourse();

    Course getById(Integer courseId);

    void registerStudentForCourse(String personId, Integer courseId) throws Exception;

    Optional<Course> createCourse(String title, String credits, Integer departmentId, String url, Integer teacherId);

    Optional<Course> createCourse(String title, String credits, Integer departmentId, String location, LocalDate days, String time, Integer teacherId);

    void deleteCourse(Integer courseId) throws Exception;

    int updateCourse(Course course);
}
