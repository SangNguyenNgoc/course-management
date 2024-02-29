package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.bll.DepartmentBll;
import com.example.coursemanagement.bll.TeacherBll;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.Department;
import com.example.coursemanagement.dtos.OnlineCourse;
import com.example.coursemanagement.dtos.Teacher;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OnlineCourseFormController implements Initializable {

    @FXML
    private Button onlineSubmitButton;
    @FXML
    private TextField onlineNameInput;
    @FXML
    private TextField onlineCreditInput;
    @FXML
    private ComboBox<String> onlineTeacherInput;
    @FXML
    private TextField onlineLinkInput;
    @FXML
    private ComboBox<String> onlineDepartmentInput;

    private List<Teacher> teachers = new ArrayList<>();

    private List<Department> departments = new ArrayList<>();

    private OnlineCourse onlineCourse;

    private DashboardController dashboardController;

    public OnlineCourseFormController() {}

    OnlineCourseFormController(OnlineCourse onlineCourse, DashboardController dashboardController) {
        this.onlineCourse = onlineCourse;
        this.dashboardController = dashboardController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teachers = TeacherBll.getInstance().getAllTeacher();
        departments = DepartmentBll.getInstance().getAll();
        setDataDepartmentInput();
        setDataTeacherInput();
        if(onlineCourse != null) {
            initData();
        } else {
            setEventSubmitBtn();
        }
    }

    private void setDataTeacherInput() {
        if (!teachers.isEmpty()) {
            teachers.forEach(item -> onlineTeacherInput.getItems().add(item.getLastName() + " " + item.getFirstName()));
        }
    }

    private void setDataDepartmentInput() {
        if (!departments.isEmpty()) {
            departments.forEach(item -> onlineDepartmentInput.getItems().add(item.getName()));
        }
    }

    private void setEventSubmitBtn() {
        onlineSubmitButton.setOnMouseClicked(event -> {
            if (checkCombobox()) {
                try {
                    Optional<Course> newCourse = CourseBll.getInstance().createCourse(
                            onlineNameInput.getText(),
                            onlineCreditInput.getText(),
                            departments.get(onlineDepartmentInput.getSelectionModel().getSelectedIndex()).getId(),
                            onlineLinkInput.getText(),
                            teachers.get(onlineTeacherInput.getSelectionModel().getSelectedIndex()).getId()
                    );
                    if (newCourse.isPresent()) {
                        DialogUtil.getInstance().showAlert("Thành công", "Đã thêm thành công", Alert.AlertType.CONFIRMATION);
                        clearInput();
                    } else {
                        DialogUtil.getInstance().showAlert("Lỗi", "Lỗi không xác định", Alert.AlertType.ERROR);
                    }
                } catch (Exception e) {
                    DialogUtil.getInstance().showAlert("Lỗi", "Thêm không thành công.", Alert.AlertType.ERROR);
                }
            }

        });
    }

    private void clearInput() {
        onlineDepartmentInput.setValue(null);
        onlineTeacherInput.setValue(null);
        onlineLinkInput.setText("");
        onlineNameInput.setText("");
        onlineCreditInput.setText("");
    }

    private boolean checkCombobox() {
        if (onlineDepartmentInput.getSelectionModel().getSelectedIndex() == -1) {
            DialogUtil.getInstance().showAlert("Cảnh báo", "Chưa chọn khoa ", Alert.AlertType.WARNING);
            return false;
        } else {
            if (onlineTeacherInput.getSelectionModel().getSelectedIndex() == -1) {
                DialogUtil.getInstance().showAlert("Cảnh báo", "Chưa chọn khoa ", Alert.AlertType.WARNING);
                return false;
            }
        }
        return true;
    }

    public void initData() {
        onlineDepartmentInput.setValue(null);
        onlineTeacherInput.setValue(null);
        onlineLinkInput.setText(onlineCourse.getUrl());
        onlineNameInput.setText(onlineCourse.getTitle());
        onlineCreditInput.setText(onlineCourse.getCredits() + "");
        onlineSubmitButton.setText("Cập nhật");
        onlineDepartmentInput.getSelectionModel().select(onlineCourse.getDepartment());
        onlineTeacherInput.getSelectionModel().select(onlineCourse.getTeacher());
        onlineSubmitButton.setOnMouseClicked(event -> {
            try {
                CourseBll.getInstance().updateCourse(
                        onlineCourse.getId(),
                        onlineNameInput.getText(),
                        onlineCreditInput.getText(),
                        departments.get(onlineDepartmentInput.getSelectionModel().getSelectedIndex()).getId(),
                        onlineLinkInput.getText(),
                        teachers.get(onlineTeacherInput.getSelectionModel().getSelectedIndex()).getId()
                );
                DialogUtil.getInstance().showAlert("Thông báo", "Cập nhật thành công.", Alert.AlertType.INFORMATION);
                dashboardController.initListCourses(dashboardController.getStage());
            } catch (Exception e) {
                DialogUtil.getInstance().showAlert("Lỗi", "Cập nhật không thành công.", Alert.AlertType.ERROR);
            }
        });
    }
}
