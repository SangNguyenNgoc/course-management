package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.bll.StudentBll;
import com.example.coursemanagement.bll.dtos.Student;
import com.example.coursemanagement.bll.dtos.StudentGrade;
import com.example.coursemanagement.bll.utils.AppUtil;
import com.example.coursemanagement.gui.utils.DialogUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController<T> {

    public TextField inputField;
    public Label title;
    public Label label;
    public Button submitBtn;

    public Label name;
    public Label day;
    public Button find;

    private StudentGrade studentGrade;

    private Stage stage;

    private T controller;

    private Integer courseId;

    public void setStudentGrace(StudentGrade studentGrade) {
        this.studentGrade = studentGrade;
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
                submitBtn.setDisable(true);
                inputField.setOnMouseClicked(event -> {
                    name.setText("Họ và tên: ");
                    day.setText("Ngày đăng ký: ");
                    submitBtn.setDisable(true);
                });
                submitBtn.setOnMouseClicked(event -> {
                    registerCourse();
                });
                break;
            }
        }
    }

    public void findStudent() {
        try {
            int id = AppUtil.getInstance().validateInteger(inputField.getText(), "Mã sinh viên");
            StudentBll.getInstance().getStudentById(id).ifPresent(item -> {
                name.setText("Họ và tên: " + item.getLastName() + " " + item.getFirstName());
                day.setText("Ngày đăng ký: " + item.getEnrollmentDate().toString());
                submitBtn.setDisable(false);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void updateGrade() {
        try {
            StudentBll.getInstance().updateGrade(studentGrade.getId(), courseId, inputField.getText());
            DialogUtil.getInstance().showAlert("Thông báo", "Thay đổi điểm thành công", Alert.AlertType.INFORMATION);
            CourseDetailController c = (CourseDetailController) controller;
            c.initCourseDetail(courseId);
        } catch (Exception e) {
            DialogUtil.getInstance().showAlert("Lỗi", "Thay đổi điểm không thành công", Alert.AlertType.ERROR);
        }
    }

    public void registerCourse() {
        try {
            CourseBll.getInstance().registerStudentForCourse(inputField.getText(), courseId);
            DialogUtil.getInstance().showAlert(
                    "Thông báo", "Đăng ký thành công", Alert.AlertType.INFORMATION);
            DashboardController c = (DashboardController) controller;
            c.initCourseDetail(courseId, null);
        } catch (Exception e) {
            DialogUtil.getInstance().showAlert(
                    "Lỗi", "Đăng ký không thành công", Alert.AlertType.ERROR);
        }
    }
}
