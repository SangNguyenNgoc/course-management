package com.example.coursemanagement.bll.interfaces;

import com.example.coursemanagement.dtos.Course;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ICourseBll {
    List<Course> getAllCourse();

    Course getById(Integer courseId);

    int registerStudentForCourse(Integer personId, Integer courseId);

    Optional<Course> createCourse(String title, String credits, Integer departmentId, String url, Integer teacherId);

    Optional<Course> createCourse(String title, String credits, Integer departmentId, String location, LocalDate days, String time, Integer teacherId);


    int deleteCourse(Integer courseId);

    int updateCourse(Course course);
}
