package com.example.coursemanagement.bll.interfaces;

import com.example.coursemanagement.dtos.StudentGrace;
import com.example.coursemanagement.dtos.Teacher;

import java.util.List;

public interface IPersonBll {
    List<StudentGrace> getStudentsInCourse(Integer courseId);

    List<Teacher> getAllTeachers();

    void updateGrade(Integer personId, Integer courseId, String grade) throws Exception;
}
