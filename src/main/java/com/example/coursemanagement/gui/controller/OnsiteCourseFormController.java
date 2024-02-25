package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.bll.DepartmentBll;
import com.example.coursemanagement.bll.PersonBll;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.Department;
import com.example.coursemanagement.dtos.Teacher;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OnsiteCourseFormController implements Initializable {

    @FXML
    private Button onsiteSubmitButton;
    @FXML
    private TextField onsiteNameInput;
    @FXML
    private TextField onsiteCreditInput;
    @FXML
    private ComboBox<String> onsiteTeacherInput;
    @FXML
    private TextField onsiteLocationInput;
    @FXML
    private TextField onsiteSizeInput;
    @FXML
    private ComboBox<String> onsiteDepartmentInput;
    @FXML
    private TextField onsiteTimeInput;
    @FXML
    private DatePicker onsiteDateInput;

    private List<Teacher> teachers;

    private List<Department> departments;

    private void setDataTeacherInput(){
        if(!teachers.isEmpty())
            teachers.forEach(item -> onsiteTeacherInput.getItems().add(item.getLastName() + " " + item.getFirstName()));
    }

    private void setDataDepartmentInput(){
        if(!departments.isEmpty())
            departments.forEach(item -> onsiteDepartmentInput.getItems().add(item.getName()));
    }

    private void setEventSubmitBtn(){
        onsiteSubmitButton.setOnMouseClicked(event -> {
            if(checkCombobox()){
                Optional<Course> newCourse = CourseBll.getInstance().createCourse("",
                        onsiteNameInput.getText(),
                        onsiteCreditInput.getText(),
                        departments.get(onsiteDepartmentInput.getSelectionModel().getSelectedIndex()).getId(),
                        onsiteLocationInput.getText(),
                        onsiteDateInput.getValue().toString(),
                        onsiteTimeInput.getText());
                if(newCourse.isPresent()){
                    DialogUtil.getInstance().showAlert("Thành công","Đã thêm thành công", Alert.AlertType.CONFIRMATION);
                    clearInput();
                }else {
                    DialogUtil.getInstance().showAlert("Lỗi","Lỗi không xác định", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void clearInput(){
        onsiteCreditInput.setText("");
        onsiteDepartmentInput.setValue(null);
        onsiteSizeInput.setText("");
        onsiteNameInput.setText("");
        onsiteLocationInput.setText("");
        onsiteTeacherInput.setValue(null);
        onsiteTimeInput.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        teachers = PersonBll.getInstance().getAllTeachers();
        departments = DepartmentBll.getInstance().getAll();

        setDataDepartmentInput();
        setDataTeacherInput();
        setEventSubmitBtn();
    }

    private boolean checkCombobox(){
        if(onsiteDepartmentInput.getSelectionModel().getSelectedIndex() == -1){
            DialogUtil.getInstance().showAlert("Cảnh báo","Chưa chọn khoa ", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
}
