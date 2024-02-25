package com.example.coursemanagement.dal.interfaces;

import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.dtos.StudentGrace;
import com.example.coursemanagement.dtos.Teacher;

import java.util.List;
import java.util.Optional;

public interface IPersonDal {

    Optional<Student> getStudentById(Integer studentId);

    List<Teacher> getAllTeachers();

    List<StudentGrace> getStudentsInCourse(Integer courseId);

    int updateGrade(Integer personId, Integer courseId, Double grade);
}
