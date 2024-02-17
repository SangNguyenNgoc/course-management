package com.example.coursemanagement.dal.interfaces;

import com.example.coursemanagement.dtos.Course;

import java.util.List;

public interface ICourseDal {
    List<Course> getAllCourses();
}
