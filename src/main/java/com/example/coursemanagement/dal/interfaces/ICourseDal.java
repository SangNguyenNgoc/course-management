package com.example.coursemanagement.dal.interfaces;

import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.Student;

import java.util.List;
import java.util.Optional;

public interface ICourseDal {
    List<Course> getAll();

    Optional<Course> getById(Integer courseId);

    int registerStudentForCourse(Integer personId, Integer courseId);

    Optional<Course> createCourse(Course course, Integer departmentId, Integer teache);

    int deleteCourse(Integer courseId);

    int updateCourse(Course course);

}
