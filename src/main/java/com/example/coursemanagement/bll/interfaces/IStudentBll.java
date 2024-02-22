package com.example.coursemanagement.bll.interfaces;

import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.dtos.StudentGrace;

import java.util.List;

public interface IStudentBll {
    List<StudentGrace> getStudentsInCourse(Integer courseId);

    int updateGrade(Integer personId, Integer courseId, String grade);
}
