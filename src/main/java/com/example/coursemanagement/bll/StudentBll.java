package com.example.coursemanagement.bll;

import com.example.coursemanagement.bll.interfaces.IStudentBll;
import com.example.coursemanagement.dal.CourseDal;
import com.example.coursemanagement.dal.StudentDal;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.dtos.StudentGrace;
import com.example.coursemanagement.utils.AppUtil;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.scene.control.Alert;

import java.util.ArrayList;
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
        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if(course == null) {
            DialogUtil.getInstance().showAlert("Lỗi","Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            return new ArrayList<>();
        }
        List<StudentGrace> studentGraces = StudentDal.getInstance().getStudentsInCourse(courseId);
        if(studentGraces == null) {
            DialogUtil.getInstance().showAlert("Lỗi","Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            return new ArrayList<>();
        }
        return studentGraces;
    }

    @Override
    public int updateGrade(Integer personId, Integer courseId, String grade) throws Exception {
        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if(course == null) {
            DialogUtil.getInstance().showAlert("Lỗi","Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            throw new Exception();
        }
        Student student = StudentDal.getInstance().getById(personId).orElse(null);
        if(student == null) {
            DialogUtil.getInstance().showAlert("Lỗi","Không tìm thấy học sinh.", Alert.AlertType.ERROR);
            throw new Exception();
        }
        double gradeDouble = AppUtil.getInstance().validateDouble(grade, "Điểm");
        if(gradeDouble < 0 || gradeDouble > 10) {
            DialogUtil.getInstance().showAlert("Lỗi","Điểm phải là số lớn hơn 0 và bé hơn 10.", Alert.AlertType.ERROR);
            return 0;
        }
        return StudentDal.getInstance().updateGrade(personId, courseId, gradeDouble);

    }
}
