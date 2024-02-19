package com.example.coursemanagement.dal;

import com.example.coursemanagement.dal.interfaces.IStudentDal;
import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.dtos.StudentGrace;

import java.util.List;

public class StudentDal implements IStudentDal {
    private static class StudentDalHolder{
        private static final StudentDal INSTANCE = new StudentDal();
    }

    private StudentDal(){}

    public static StudentDal getInstance() {
        return StudentDalHolder.INSTANCE;
    }

    @Override
    public List<StudentGrace> getStudentsInCourse(Integer courseId) {
        return null;
    }

    @Override
    public int updateGrade(Integer personId, Integer courseId) {
        return 0;
    }
}
