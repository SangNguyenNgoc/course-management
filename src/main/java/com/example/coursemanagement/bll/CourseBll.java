package com.example.coursemanagement.bll;

import com.example.coursemanagement.bll.interfaces.ICourseBll;
import com.example.coursemanagement.dal.CourseDal;
import com.example.coursemanagement.dtos.Course;

import java.util.List;

public class CourseBll implements ICourseBll {

    private static class CourseBllHolder {
        private static final CourseBll INSTANCE = new CourseBll();
    }

    private CourseBll(){}

    public static CourseBll getInstance() {
        return CourseBllHolder.INSTANCE;
    }

    public List<Course> getAllCourse() {
        return CourseDal.getInstance().getAllCourses();
    }
}
