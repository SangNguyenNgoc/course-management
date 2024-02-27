package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.bll.StudentBll;
import com.example.coursemanagement.dtos.StudentGrace;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateGradeController<T> {

    public TextField inputField;
    public Label title;
    public Label label;
    public Button submitBtn;

    private StudentGrace studentGrace;

    private Stage stage;

    private T controller;

    private Integer courseId;

    public void setStudentGrace(StudentGrace studentGrace) {
        this.studentGrace = studentGrace;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public void setController(T controller) {
        this.controller = controller;
    }

    public void initialize(String state) {
        switch (state) {
            case "updateGrade": {
                title.setText("Chỉnh sửa điểm");
                label.setText("Nhập điểm");
                submitBtn.setText("Chỉnh sửa");
                submitBtn.setOnMouseClicked(event -> {
                    updateGrade();
                });
                break;
            }

            case "register": {
                title.setText("Đăng ký mới");
                label.setText("Nhập mã sinh viên");
                submitBtn.setText("Đăng ký");
                submitBtn.setOnMouseClicked(event -> {
                    registerCourse();
                });
                break;
            }
        }
    }

    public void updateGrade() {
        try {
            StudentBll.getInstance().updateGrade(studentGrace.getId(), courseId, inputField.getText());
            DialogUtil.getInstance().showAlert("Thông báo", "Thay đổi điểm thành công", Alert.AlertType.INFORMATION);
            CourseDetailController c = (CourseDetailController) controller;
            c.initCourseDetail(courseId);
        } catch (Exception e) {
            DialogUtil.getInstance().showAlert("Lỗi", "Thay đổi điểm không thành công", Alert.AlertType.ERROR);
        }
        this.stage.close();
    }

    public void registerCourse() {
        try {
            CourseBll.getInstance().registerStudentForCourse(inputField.getText(), courseId);
            DialogUtil.getInstance().showAlert("Thông báo", "Đăng ký thành công", Alert.AlertType.INFORMATION);
            DashboardController c = (DashboardController) controller;
            c.initCourseDetail(courseId);
        } catch (Exception e) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đăng ký không thành công", Alert.AlertType.ERROR);
        }
        this.stage.close();
    }
}
