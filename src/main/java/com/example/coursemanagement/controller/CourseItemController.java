package com.example.coursemanagement.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.OnlineCourse;
import com.example.coursemanagement.dtos.OnsiteCourse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Duration;

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

    public void setData(Course course) {
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
        info1.setText(course.getDays());
        info2.setText(course.getTime().toString());
        info3.setText(course.getLocation());
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
        System.out.println(title.getText());
    }

    public void fixCourse() {
        System.out.println(title.getText());
    }

    public void deleteCourse() {
        System.out.println(title.getText());
    }
}
