package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.Department;
import com.example.coursemanagement.dtos.Teacher;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
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
    private ComboBox onsiteTeacherInput;
    @FXML
    private TextField onsiteLocationInput;
    @FXML
    private TextField onsiteSizeInput;
    @FXML
    private ComboBox onsiteDepartmentInput;
    @FXML
    private TextField onsiteTimeInput;
    @FXML
    private DatePicker onsiteDateInput;

    private ArrayList<Teacher> teachers = new ArrayList<>();

    private ArrayList<Department> departments = new ArrayList<>();

    private void setDataTecherInput(){
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
                Optional<Course> newCourse = CourseBll.getInstance().createCourse(
                        onsiteNameInput.getText(),
                        onsiteCreditInput.getText(),
                        departments.get(onsiteDepartmentInput.getSelectionModel().getSelectedIndex()).getId(),
                        onsiteLocationInput.getText(),
                        onsiteDateInput.getValue(),
                        onsiteTimeInput.getText(),
                        teachers.get(onsiteTeacherInput.getSelectionModel().getSelectedIndex()).getId()
                        );
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
        //Lấy danh sách giáo viên gán nào mảng teacher

        //Lấy danh sách giáo viên gán nào mảng teacher

        setDataDepartmentInput();
        setDataTecherInput();
        setEventSubmitBtn();
    }

    private boolean checkCombobox(){
        if(onsiteDepartmentInput.getSelectionModel().getSelectedIndex() == -1){
            DialogUtil.getInstance().showAlert("Cảnh báo","Chưa chọn khoa ", Alert.AlertType.WARNING);
            return false;
        }else {
            if (onsiteTeacherInput.getSelectionModel().getSelectedIndex() == -1){
                DialogUtil.getInstance().showAlert("Cảnh báo","Chưa chọn giáo viên ", Alert.AlertType.WARNING);
                return false;
            }
        }
        return true;
    }
}
