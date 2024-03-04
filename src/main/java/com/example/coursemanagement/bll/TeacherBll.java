package com.example.coursemanagement.bll;

import com.example.coursemanagement.dal.TeacherDal;
import com.example.coursemanagement.bll.dtos.Teacher;
import com.example.coursemanagement.gui.utils.DialogUtil;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<Teacher> getTeacherById(Integer id) {
        Optional<Teacher> teacher = TeacherDal.getInstance().getById(id);
        if (teacher.isEmpty()) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return Optional.empty();
        } else {
            return teacher;
        }
    }
    public int addTeacher(String firstname, String lastName, LocalDate date) {
        if(firstname.isEmpty() || lastName.isEmpty() || date == null) {
            DialogUtil.getInstance().showAlert(
                    "Lỗi",
                    "Được để trống thông tin.",
                    Alert.AlertType.ERROR);
            return 0;
        }
        Teacher teacher = Teacher.builder()
                .firstName(firstname)
                .lastName(lastName)
                .hireDate(LocalDateTime.of(date, LocalTime.now()))
                .build();
        return TeacherDal.getInstance().addTeacher(teacher);
    }

    public int updateTeacher(Integer teacherId, String firstname, String lastName, LocalDate date) {
        if(firstname.isEmpty() || lastName.isEmpty() || date == null) {
            DialogUtil.getInstance().showAlert(
                    "Lỗi",
                    "Được để trống thông tin.",
                    Alert.AlertType.ERROR);
            return 0;
        }
        Teacher teacher = Teacher.builder()
                .id(teacherId)
                .firstName(firstname)
                .lastName(lastName)
                .hireDate(LocalDateTime.of(date, LocalTime.now()))
                .build();
        if (TeacherDal.getInstance().getById(teacher.getId()).isEmpty()) {
            DialogUtil.getInstance().showAlert(
                    "Lỗi", "Giáo viên không tồn tại trong hệ thống.", Alert.AlertType.ERROR);
            return 0; // Trả về 0 để biểu thị lỗi
        }
        return TeacherDal.getInstance().updateTeacher(teacher);
    }

    public int deleteTeacher(Integer teacherId) {
        if (TeacherDal.getInstance().getById(teacherId).isEmpty()) {
            DialogUtil.getInstance().showAlert(
                    "Lỗi", "Giáo viên không tồn tại trong hệ thống.", Alert.AlertType.ERROR);
            return 0; // Trả về 0 để biểu thị lỗi
        }
        if (TeacherDal.getInstance().checkTeacher(teacherId)) {
            DialogUtil.getInstance().showAlert(
                    "Lỗi",
                    "Không thể xóa giảng viên vì đã có phân công giảng dạy.", Alert.AlertType.ERROR);
            return 0; // Trả về 0 để biểu thị lỗi
        }
        return TeacherDal.getInstance().deleteTeacher(teacherId);
    }
}
