package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.dtos.Department;
import com.example.coursemanagement.utils.AppUtil;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentItemController implements Initializable {
    public Label col1;
    public Label col2;
    public Label col3;
    public Label col4;
    public Label col5;
    public Button fixDepartment;
    public Button deleteDepartment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fixDepartment.setOnMouseEntered(event -> {
            fixDepartment.setUnderline(true);
        });
        fixDepartment.setOnMouseExited(event -> {
            fixDepartment.setUnderline(false);
        });
        deleteDepartment.setOnMouseEntered(event -> {
            deleteDepartment.setUnderline(true);
        });
        deleteDepartment.setOnMouseExited(event -> {
            deleteDepartment.setUnderline(false);
        });
    }

    public void initDepartment(Department department) {
        col1.setText(department.getId().toString());
        col2.setText(department.getName());
        col3.setText(AppUtil.getInstance().formatDate(department.getStartDate()));
        col4.setText(department.getBudget().toString());
        col5.setText(department.getAdministrator());
    }
}
