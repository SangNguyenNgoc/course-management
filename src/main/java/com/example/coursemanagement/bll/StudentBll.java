package com.example.coursemanagement.bll;

import com.example.coursemanagement.bll.interfaces.IStudentBll;
import com.example.coursemanagement.dtos.StudentGrace;

import java.util.List;

public class StudentBll implements IStudentBll {

    private static class StudentBllHolder {
        private static final StudentBll INSTANCE = new StudentBll();
    }

    private StudentBll(){}

    public static StudentBll getInstance() {
        return StudentBll.StudentBllHolder.INSTANCE;
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
