package com.example.coursemanagement.bll;

import com.example.coursemanagement.bll.interfaces.ICourseBll;
import com.example.coursemanagement.dal.CourseDal;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.scene.control.Alert;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseBll implements ICourseBll {

    private static class CourseBllHolder {
        private static final CourseBll INSTANCE = new CourseBll();
    }

    private CourseBll() {
    }

    public static CourseBll getInstance() {
        return CourseBllHolder.INSTANCE;
    }

    @Override
    public List<Course> getAllCourse() {
        List<Course> courses = CourseDal.getInstance().getAll();
        if (courses == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return new ArrayList<>();
        } else {
            return courses;
        }
    }

    @Override
    public Course getById(Integer courseId) {
        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if (course == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            return null;
        } else {
            return course;
        }
    }

    @Override
    public int registerStudentForCourse(Integer personId, Integer courseId) {
        return 0;
    }

    @Override
    public Optional<Course> createCourse(String title, String name, String credits, Integer departmentId, String url) {
        return Optional.empty();
    }

    @Override
    public Optional<Course> createCourse(String title, String name, String credits, Integer departmentId, String location, String days, String time) {
        return Optional.empty();
    }

    @Override
    public int deleteCourse(Integer courseId) {
        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if(course == null) {
            DialogUtil.getInstance().showAlert("Lỗi","Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            return 0;
        }
        int amount = StudentBll.getInstance().getStudentsInCourse(courseId).size();
        if(amount != 0) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không thể xóa do đã có học sinh đăng ký.", Alert.AlertType.WARNING);
            return 0;
        }
        return CourseDal.getInstance().deleteCourse(courseId);
    }

    @Override
    public int updateCourse(Course course) {
        return 0;
    }
}
