package com.example.coursemanagement.dal.interfaces;

import com.example.coursemanagement.dtos.StudentGrace;

import java.util.List;

public interface IStudentDal {
    List<StudentGrace> getStudentsInCourse(Integer courseId);

    int updateGrade(Integer personId, Integer courseId);
}
