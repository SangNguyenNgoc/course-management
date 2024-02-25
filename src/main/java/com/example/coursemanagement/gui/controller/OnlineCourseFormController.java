package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.Department;
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
    private ComboBox onlineTeacherInput;
    @FXML
    private TextField onlineLinkInput;
    @FXML
    private TextField onlineSizeInput;
    @FXML
    private ComboBox onlineDepartmentInput;

    private ArrayList<Teacher> teachers = new ArrayList<>();

    private ArrayList<Department> departments = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Lấy danh sách giáo viên gán nào mảng teacher

        //Lấy danh sách giáo viên gán nào mảng teacher

        setDataDepartmentInput();
        setDataTecherInput();
        setEventSubmitBtn();
    }

    private void setDataTecherInput(){
        if(!teachers.isEmpty())
        teachers.forEach(item -> onlineTeacherInput.getItems().add(item.getLastName() + " " + item.getFirstName()));
    }

    private void setDataDepartmentInput(){
        if(!departments.isEmpty())
        departments.forEach(item -> onlineDepartmentInput.getItems().add(item.getName()));
    }

    private void setEventSubmitBtn(){
        onlineSubmitButton.setOnMouseClicked(event -> {
            Optional<Course> newCourse = CourseBll.getInstance().createCourse("",
                    onlineNameInput.getText(),
                    onlineCreditInput.getText(),
                    departments.get(onlineDepartmentInput.getSelectionModel().getSelectedIndex()).getId(),
                    onlineLinkInput.getText());
            if(newCourse.isPresent()){
                DialogUtil.getInstance().showAlert("Thành công","Đã thêm thành công", Alert.AlertType.CONFIRMATION);
                clearInput();
            }else {
                DialogUtil.getInstance().showAlert("Lỗi","Lỗi không xác định", Alert.AlertType.ERROR);
            }
        });
    }

    private void clearInput(){
        onlineDepartmentInput.setValue(null);
        onlineTeacherInput.setValue(null);
        onlineLinkInput.setText("");
        onlineNameInput.setText("");
        onlineCreditInput.setText("");
        onlineSizeInput.setText("");
    }
}
