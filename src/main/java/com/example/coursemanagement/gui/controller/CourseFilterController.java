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

    private List<Department> departments;

    private List<Teacher> teachers;

    public void initToFilter(DashboardController dashboardController, Stage stage) {
        departments = DepartmentBll.getInstance().getAll();
        teachers = TeacherBll.getInstance().getAllTeacher();
        departmentInput.getItems().add("Tất cả");
        departments.forEach(item -> departmentInput.getItems().add(item.getName()));
        departmentInput.getSelectionModel().select(0);
        teacherInput.getItems().add("Tất cả");
        teachers.forEach(item -> teacherInput.getItems().add(item.getLastName() + " " + item.getFirstName()));
        teacherInput.getSelectionModel().select(0);
        submitButton.setOnMouseClicked(event -> {
            dashboardController.initListCourses(filter());
            stage.close();
        });
    }

    public List<Course> filter() {
        List<Course> courses = CourseBll.getInstance().getAllCourse();
        if(!nameInput.getText().isEmpty()) {
            courses = courses.stream().filter(item -> item.getTitle().toLowerCase().contains(nameInput.getText().toLowerCase())).collect(Collectors.toList());
        }
        if(AppUtil.getInstance().isInteger(creditInput.getText()) && !creditInput.getText().isEmpty()) {
            courses = courses.stream().filter(item -> item.getCredits() == Integer.parseInt(creditInput.getText())).collect(Collectors.toList());
        }
        if(departmentInput.getSelectionModel().getSelectedIndex() > 0) {
            courses = courses.stream().filter(item -> item.getDepartment().toLowerCase().contains(departmentInput.getSelectionModel().getSelectedItem().toLowerCase())).collect(Collectors.toList());
        }
        if(teacherInput.getSelectionModel().getSelectedIndex() > 0) {
            courses = courses.stream().filter(item -> item.getTeacher().toLowerCase().contains(departmentInput.getSelectionModel().getSelectedItem().toLowerCase())).collect(Collectors.toList());

        }
        return courses;
    }
}
