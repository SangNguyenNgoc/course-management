package com.example.coursemanagement.controller;

import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.OnlineCourse;
import com.example.coursemanagement.dtos.OnsiteCourse;
import com.example.coursemanagement.page.Component;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class CourseItemController {
    
    @FXML
    public Label title;

    public Label department;
    public Label type;
    public Label teacher;
    public Label credits;
    public Label amount;
    public Label info1;
    public Label info2;
    public Label info3;
    public Label link;
    public Button viewListStudentBtn;
    public Button fixBtn;
    public Button deleteBtn;
    public HBox parent;

    public VBox btnList;

    private Course course;

    private DashboardController dashboardController;

    public void setData(Course course, DashboardController dashboardController) {
        this.course = course;
        this.dashboardController = dashboardController;
        if(course instanceof OnsiteCourse) {
            setData((OnsiteCourse) course);
        } else {
            setData((OnlineCourse) course);
        }
    }

    public void setData(OnsiteCourse course) {
        title.setText(course.getTitle());
        department.setText(course.getDepartment());
        type.setText(course.getType());
        teacher.setText(course.getTeacher());
        credits.setText("Số tín chỉ: " + course.getCredits());
        amount.setText("Số lượng: " + course.getSumOfStudent());
        info1.setText("Ngày: " + course.getDays());
        info2.setText("Thời gian: " + course.getTime().toString());
        info3.setText("Địa chỉ: " + course.getLocation());
        link.setText("Lịch học");
    }

    public void setData(OnlineCourse course) {
        title.setText(course.getTitle());
        department.setText(course.getDepartment());
        type.setText(course.getType());
        teacher.setText(course.getTeacher());
        credits.setText("Số tín chỉ: " + course.getCredits());
        amount.setText("Số lượng: " + course.getSumOfStudent());
        info1.setText(course.getUrl());
        info2.setText("");
        info3.setText("");
        link.setText("Liên kết");
        info1.setOnMouseClicked(event -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(info1.getText());
            Clipboard.getSystemClipboard().setContent(content);
        });
        Tooltip tooltip = new Tooltip("Sao chép liên kết");
        tooltip.setShowDelay(Duration.millis(100));
        Tooltip.install(info1, tooltip);
    }

    public void viewListStudents() {
        try {
            dashboardController.initCourseDetail(course.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(course.getId());
    }

    public void fixCourse() {
        System.out.println(title.getText());
    }

    public void deleteCourse() {
        System.out.println(title.getText());
    }

    public void setHeaderDetail(Course course) {
        if(course instanceof OnsiteCourse) {
            setData((OnsiteCourse) course);
        } else {
            setData((OnlineCourse) course);
        }
        parent.getChildren().remove(btnList);
        parent.setSpacing(70);
    }

    public void setHeaderDetail(OnlineCourse course) {
        title.setText(course.getTitle());
        department.setText(course.getDepartment());
        type.setText(course.getType());
        teacher.setText(course.getTeacher());
        credits.setText("Số tín chỉ: " + course.getCredits());
        amount.setText("Số lượng: " + course.getSumOfStudent());
        info1.setText(course.getUrl());
        info2.setText("");
        info3.setText("");
        link.setText("Liên kết");
    }

    public void setHeaderDetail(OnsiteCourse course) {
        title.setText(course.getTitle());
        department.setText(course.getDepartment());
        type.setText(course.getType());
        teacher.setText(course.getTeacher());
        credits.setText("Số tín chỉ: " + course.getCredits());
        amount.setText("Số lượng: " + course.getSumOfStudent());
        info1.setText("Ngày: " + course.getDays());
        info2.setText("Thời gian: " + course.getTime().toString());
        info3.setText("Địa chỉ: " + course.getLocation());
        link.setText("Lịch học");
    }


}
