package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.bll.DepartmentBll;
import com.example.coursemanagement.bll.TeacherBll;
import com.example.coursemanagement.bll.dtos.Course;
import com.example.coursemanagement.bll.dtos.Department;
import com.example.coursemanagement.bll.dtos.Teacher;
import com.example.coursemanagement.bll.utils.AppUtil;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class CourseFilterController {
    public Button submitButton;
    public TextField nameInput;
    public ComboBox<String> teacherInput;
    public ComboBox<String> departmentInput;
    public TextField creditInput;

    public void initToFilter(DashboardController dashboardController, Stage stage) {
        List<Department> departments = DepartmentBll.getInstance().getAll();
        List<Teacher> teachers = TeacherBll.getInstance().getAllTeacher();
        departmentInput.getItems().add("Tất cả");
        departments.forEach(item -> departmentInput.getItems().add(item.getName()));
        departmentInput.getSelectionModel().select(0);
        teacherInput.getItems().add("Tất cả");
        teachers.forEach(item -> teacherInput.getItems().add(item.getLastName() + " " + item.getFirstName()));
        teacherInput.getSelectionModel().select(0);
        submitButton.setOnMouseClicked(event -> {
            dashboardController.initListCourses(CourseBll.getInstance().filter(
                    dashboardController.getStage(),
                    nameInput.getText(),
                    creditInput.getText(),
                    departmentInput.getSelectionModel().getSelectedIndex() > 0 ?
                            departmentInput.getSelectionModel().getSelectedItem() : "",
                    teacherInput.getSelectionModel().getSelectedIndex() > 0 ?
                            teacherInput.getSelectionModel().getSelectedItem() : ""
                    ));
            stage.close();
        });
    }


}
