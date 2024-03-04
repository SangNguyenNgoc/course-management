package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.bll.dtos.Course;
import com.example.coursemanagement.bll.dtos.OnlineCourse;
import com.example.coursemanagement.bll.dtos.OnsiteCourse;
import com.example.coursemanagement.gui.page.Component;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ListCourseController {
    public VBox content;

    public void initListCourses(String type, DashboardController dashboardController) {
        content.getChildren().clear();
        try {
            List<Course> courses = CourseBll.getInstance().getAllCourse();
            switch (type) {
                case "onlineCourse": {
                    courses = courses.stream().filter(item ->
                            item instanceof OnlineCourse)
                            .collect(Collectors.toList());
                    break;
                }
                case "onsiteCourse": {
                    courses = courses.stream().filter(item ->
                            item instanceof OnsiteCourse)
                            .collect(Collectors.toList());
                    break;
                }
                case "allCourse": {
                    break;
                }
                default: {
                    courses = courses.stream().filter(item ->
                            item.getTitle().toLowerCase().contains(type.toLowerCase())
                            || item.getDepartment().toLowerCase().contains(type.toLowerCase()))
                            .collect(Collectors.toList());
                }
            }
            render(courses, dashboardController);
        } catch (IOException e) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, "Error", e);
        }
    }

    public void initListCourses(List<Course> courses, String type, DashboardController dashboardController) {
        content.getChildren().clear();
        try {
            switch (type) {
                case "onlineCourse": {
                    courses = courses.stream().filter(item ->
                                    item instanceof OnlineCourse)
                            .collect(Collectors.toList());
                    break;
                }
                case "onsiteCourse": {
                    courses = courses.stream().filter(item ->
                                    item instanceof OnsiteCourse)
                            .collect(Collectors.toList());
                    break;
                }
                case "allCourse": {
                    break;
                }
            }
            render(courses, dashboardController);
        } catch (IOException e) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, "Error", e);
        }
    }

    public void render(List<Course> courses, DashboardController dashboardController) throws IOException {
        if(courses.isEmpty()) {
            FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.EMPTY.getValue()));
            Parent root = loader.load();
            EmptyController controller = loader.getController();
            controller.setText("Không có khóa học !!!");
            content.getChildren().add(root);
        } else {
            for (Course course : courses) {
                FXMLLoader loader = new FXMLLoader(
                        HomeApplication.class.getResource(Component.COURSE_ITEM.getValue()));
                Parent root = loader.load();
                CourseItemController controller = loader.getController();
                controller.setData(course, dashboardController);
                content.getChildren().add(root);
            }
        }
    }
}
