package com.example.coursemanagement.bll;

import com.example.coursemanagement.bll.interfaces.ITeacherBll;
import com.example.coursemanagement.dal.TeacherDal;
import com.example.coursemanagement.dtos.Teacher;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class TeacherBll implements ITeacherBll {

    private static class TeacherBllHolder {
        private static final TeacherBll INSTANCE = new TeacherBll();
    }

    private TeacherBll() {
    }

    public static TeacherBll getInstance() {
        return TeacherBllHolder.INSTANCE;
    }
    @Override
    public List<Teacher> getAllTeacher() {
        List<Teacher> teachers = TeacherDal.getInstance().getAll();
        if (teachers == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return new ArrayList<>();
        } else {
            return teachers;
        }
    }
}
