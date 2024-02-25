package com.example.coursemanagement.bll;

import com.example.coursemanagement.bll.interfaces.ICourseBll;
import com.example.coursemanagement.dal.CourseDal;
import com.example.coursemanagement.dal.PersonDal;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.utils.AppUtil;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.scene.control.Alert;

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
    public void registerStudentForCourse(String studentId, Integer courseId) throws Exception {
        int id = AppUtil.getInstance().validateInteger(studentId, "Mã sinh viên");
        Student student = PersonDal.getInstance().getStudentById(id).orElse(null);
        if(student == null) {
            DialogUtil.getInstance().showAlert("Lỗi","Không tìm thấy sinh viên.", Alert.AlertType.ERROR);
            throw new Exception();
        }

        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if(course == null) {
            DialogUtil.getInstance().showAlert("Lỗi","Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            throw new Exception();
        }


        if(CourseDal.getInstance().isStudentInCourse(id, courseId)) {
            DialogUtil.getInstance().showAlert("Lỗi","Sinh viên đã đăng ký khóa học.", Alert.AlertType.ERROR);
            throw new Exception();
        }

        if(CourseDal.getInstance().isStudentInCourse(id, courseId) == null) {
            DialogUtil.getInstance().showAlert("Lỗi","Đã có lỗi xảy ra, vui lòng thử lại sau!", Alert.AlertType.ERROR);
            throw new Exception();
        }

        int result = CourseDal.getInstance().registerStudentForCourse(id, courseId);
        if(result == 0) {
            DialogUtil.getInstance().showAlert("Lỗi","Đã có lỗi xảy ra, vui lòng thử lại sau!", Alert.AlertType.ERROR);
            throw new Exception();
        }
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
    public void deleteCourse(Integer courseId) throws Exception {
        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if(course == null) {
            DialogUtil.getInstance().showAlert("Lỗi","Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            throw new Exception();
        }
        int amount = PersonBll.getInstance().getStudentsInCourse(courseId).size();
        if(amount != 0) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không thể xóa do đã có học sinh đăng ký.", Alert.AlertType.WARNING);
            throw new Exception();
        }
        CourseDal.getInstance().deleteCourse(courseId);
    }

    @Override
    public int updateCourse(Course course) {
        return 0;
    }
}
