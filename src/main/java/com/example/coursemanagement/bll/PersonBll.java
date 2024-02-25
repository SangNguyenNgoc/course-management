package com.example.coursemanagement.bll;

import com.example.coursemanagement.bll.interfaces.IPersonBll;
import com.example.coursemanagement.dal.CourseDal;
import com.example.coursemanagement.dal.PersonDal;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.dtos.StudentGrace;
import com.example.coursemanagement.dtos.Teacher;
import com.example.coursemanagement.utils.AppUtil;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class PersonBll implements IPersonBll {

    private static class StudentBllHolder {
        private static final PersonBll INSTANCE = new PersonBll();
    }

    private PersonBll(){}

    public static PersonBll getInstance() {
        return PersonBll.StudentBllHolder.INSTANCE;
    }

    @Override
    public List<StudentGrace> getStudentsInCourse(Integer courseId) {
        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if(course == null) {
            DialogUtil.getInstance().showAlert("Lỗi","Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            return new ArrayList<>();
        }
        List<StudentGrace> studentGraces = PersonDal.getInstance().getStudentsInCourse(courseId);
        if(studentGraces == null) {
            DialogUtil.getInstance().showAlert("Lỗi","Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            return new ArrayList<>();
        }
        return studentGraces;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = PersonDal.getInstance().getAllTeachers();
        if (teachers == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return new ArrayList<>();
        } else {
            return teachers;
        }
    }

    @Override
    public void updateGrade(Integer personId, Integer courseId, String grade) throws Exception {
        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if(course == null) {
            DialogUtil.getInstance().showAlert("Lỗi","Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            throw new Exception();
        }
        Student student = PersonDal.getInstance().getStudentById(personId).orElse(null);
        if(student == null) {
            DialogUtil.getInstance().showAlert("Lỗi","Không tìm thấy học sinh.", Alert.AlertType.ERROR);
            throw new Exception();
        }
        double gradeDouble = AppUtil.getInstance().validateDouble(grade, "Điểm");
        if(gradeDouble < 0 || gradeDouble > 10) {
            DialogUtil.getInstance().showAlert("Lỗi","Điểm phải là số lớn hơn 0 và bé hơn 10.", Alert.AlertType.ERROR);
            return;
        }
        PersonDal.getInstance().updateGrade(personId, courseId, gradeDouble);

    }
}
