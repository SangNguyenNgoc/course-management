package com.example.coursemanagement.bll;

import com.example.coursemanagement.dal.StudentDal;
import com.example.coursemanagement.dal.TeacherDal;
import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.dtos.Teacher;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeacherBll {

    private static class TeacherBllHolder {
        private static final TeacherBll INSTANCE = new TeacherBll();
    }

    private TeacherBll() {
    }

    public static TeacherBll getInstance() {
        return TeacherBllHolder.INSTANCE;
    }


    public List<Teacher> getAllTeacher() {
        List<Teacher> teachers = TeacherDal.getInstance().getAll();
        if (teachers == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return new ArrayList<>();
        } else {
            return teachers;
        }
    }

    public int addTeacher(String firstname, String lastName, LocalDate date) {
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName(firstname)
                .lastName(lastName)
                .hireDate(LocalDateTime.of(date, LocalTime.now()))
                .build();
        return TeacherDal.getInstance().addTeacher(teacher);
    }

    public int updateTeacher(Integer studentId, String firstname, String lastName, LocalDate date) {
        Teacher teacher = Teacher.builder()
                .id(studentId)
                .firstName(firstname)
                .lastName(lastName)
                .hireDate(LocalDateTime.of(date, LocalTime.now()))
                .build();
        if (TeacherDal.getInstance().getById(teacher.getId()).isEmpty()) {
            DialogUtil.getInstance().showAlert("Lỗi", "Giáo viên không tồn tại trong hệ thống.", Alert.AlertType.ERROR);
            return 0; // Trả về 0 để biểu thị lỗi
        }
        return TeacherDal.getInstance().updateTeacher(teacher);
    }

    public int deleteTeacher(Integer teacherId) {
        if (!TeacherDal.getInstance().checkTeacher(teacherId)) {
            DialogUtil.getInstance().showAlert("Lỗi", "Không thể xóa giảng viên vì đã có phân công.", Alert.AlertType.ERROR);
            return 0; // Trả về 0 để biểu thị lỗi
        }
        return TeacherDal.getInstance().deleteTeacher(teacherId);
    }
}
