package com.example.coursemanagement.dal.interfaces;

import com.example.coursemanagement.dtos.Course;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ICourseDal {
    List<Course> getAll();

    Optional<Course> getById(Integer courseId);

    int registerStudentForCourse(Integer studentId, Integer courseId);

    Optional<Course> createCourse(Course course, Integer departmentId, Integer teacher);

    void deleteCourse(Integer courseId);

    Boolean isStudentInCourse(Integer studentId, Integer courseId);

    Boolean createCourseOnsite(Integer courseId, String location, String date, LocalTime time);

    Boolean createCourseOnline(Integer courseId, String url);

    int updateCourse(Course course, Integer departmentId, Integer teacher);

    int updateCourseOnsite(Integer courseId, String location, String date, LocalTime time);

    int updateCourseOnline(Integer courseId, String url);

}
