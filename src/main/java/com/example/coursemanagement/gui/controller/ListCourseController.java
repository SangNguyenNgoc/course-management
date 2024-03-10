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
            List<Course> courses;
            switch (type) {
                case "onlineCourse": {
                    courses = CourseBll.getInstance().getOnlineCourse();
                    break;
                }
                case "onsiteCourse": {
                    courses = CourseBll.getInstance().getOnsiteCourse();
                    break;
                }
                case "allCourse": {
                    courses = CourseBll.getInstance().getAllCourse();
                    break;
                }
                default: {
                    courses = CourseBll.getInstance().filter(type);
                }
            }
            render(courses, dashboardController);
        } catch (IOException e) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, "Error", e);
        }
    }


    public void render(List<Course> courses, DashboardController dashboardController) throws IOException {
        if (courses.isEmpty()) {
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
