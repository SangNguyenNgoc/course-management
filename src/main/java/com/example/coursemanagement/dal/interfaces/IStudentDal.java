package com.example.coursemanagement.dal.interfaces;

import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.dtos.StudentGrace;

import java.util.List;
import java.util.Optional;

public interface IStudentDal {

    Optional<Student> getById(Integer studentId);

    List<StudentGrace> getStudentsInCourse(Integer courseId);

    int updateGrade(Integer personId, Integer courseId, Double grade);
}
