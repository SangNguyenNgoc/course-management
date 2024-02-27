package com.example.coursemanagement.bll;

import com.example.coursemanagement.dal.CourseDal;
import com.example.coursemanagement.dal.StudentDal;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.dtos.StudentGrade;
import com.example.coursemanagement.utils.AppUtil;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentBll {

    private static class StudentBllHolder {
        private static final StudentBll INSTANCE = new StudentBll();
    }

    private StudentBll() {
    }

    public static StudentBll getInstance() {
        return StudentBll.StudentBllHolder.INSTANCE;
    }

    public List<Student> getAllStudents() {
        List<Student> students = StudentDal.getInstance().getAll();
        if (students == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return new ArrayList<>();
        } else {
            return students;
        }
    }

    public List<StudentGrade> getStudentsInCourse(Integer courseId) {
        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if (course == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            return new ArrayList<>();
        }
        List<StudentGrade> studentGrades = StudentDal.getInstance().getStudentsInCourse(courseId);
        if (studentGrades == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            return new ArrayList<>();
        }
        return studentGrades;
    }

    public void updateGrade(Integer personId, Integer courseId, String grade) throws Exception {
        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if (course == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            throw new Exception();
        }
        Student student = StudentDal.getInstance().getById(personId).orElse(null);
        if (student == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không tìm thấy học sinh.", Alert.AlertType.ERROR);
            throw new Exception();
        }
        double gradeDouble = AppUtil.getInstance().validateDouble(grade, "Điểm");
        if (gradeDouble < 0 || gradeDouble > 10) {
            DialogUtil.getInstance().showAlert("Lỗi", "Điểm phải là số lớn hơn 0 và bé hơn 10.", Alert.AlertType.ERROR);
            return;
        }
        StudentDal.getInstance().updateGrade(personId, courseId, gradeDouble);

    }

    public int deleteGrade(Integer personId, Integer courseId) throws Exception {
        Course course = CourseDal.getInstance().getById(courseId).orElse(null);
        if (course == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không tìm thấy khóa học.", Alert.AlertType.ERROR);
            throw new Exception();
        }
        Student student = StudentDal.getInstance().getById(personId).orElse(null);
        if (student == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không tìm thấy học sinh.", Alert.AlertType.ERROR);
            throw new Exception();
        }
        return StudentDal.getInstance().deleteGrade(courseId, personId);

    }
    public int addStudent(String firstname, String lastName, LocalDate date) {
        Student student = Student.builder()
                .id(1)
                .firstName(firstname)
                .lastName(lastName)
                .enrollmentDate(convertToUtilDate(date))
                .build();

        // Thực hiện thêm student vào cơ sở dữ liệu
        return StudentDal.getInstance().addStudent(student);
    }

    public int updateStudent(Integer studentId, String firstname, String lastName, LocalDate date) {
        Student student = Student.builder()
                .id(studentId)
                .firstName(firstname)
                .lastName(lastName)
                .enrollmentDate(convertToUtilDate(date))
                .build();
        if (StudentDal.getInstance().getById(student.getId()).isEmpty()) {
            DialogUtil.getInstance().showAlert("Lỗi", "Học sinh không tồn tại trong hệ thống.", Alert.AlertType.ERROR);
            return 0; // Trả về 0 để biểu thị lỗi
        }
        // Thực hiện sửa thông tin học sinh
        return StudentDal.getInstance().updateStudent(student);
    }

    public int deleteStudent(Integer studentId) {
        // Kiểm tra xem student có tồn tại trong bảng studentgrade không
        if (StudentDal.getInstance().isStudentInGradeTable(studentId)) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không thể xóa học sinh vì học sinh đã có điểm trong bảng studentgrade.", Alert.AlertType.ERROR);
            return 0; // Trả về 0 để biểu thị lỗi
        }

        // Thực hiện xóa học sinh
        return StudentDal.getInstance().deleteStudent(studentId);
    }

    public Date convertToUtilDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return java.sql.Date.valueOf(localDate);
    }
}
