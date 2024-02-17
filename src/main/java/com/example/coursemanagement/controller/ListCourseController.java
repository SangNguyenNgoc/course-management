package com.example.coursemanagement.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.dal.DbConnection;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.OnlineCourse;
import com.example.coursemanagement.dtos.OnsiteCourse;
import com.example.coursemanagement.model.ButtonModel;
import com.example.coursemanagement.page.Component;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ListCourseController implements Initializable {


    @FXML
    public HBox leftToolbar;

    @FXML
    public HBox rightToolbar;

    @FXML
    public VBox content;

    @FXML
    public ScrollPane scrollPane;


    private static final List<ButtonModel> LIST_BUTTONS = Arrays.asList(
            ButtonModel.builder().key("allCourse").text("Tất cả").build(),
            ButtonModel.builder().key("onlineCourse").text("Khóa trực tuyến").build(),
            ButtonModel.builder().key("inPersonCourse").text("Khóa tại chỗ").build()
    );

    private static final ButtonModel ADD_COURSE = ButtonModel.builder().key("addCourse").text("Tạo khóa học").build();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LIST_BUTTONS.forEach(item ->
                leftToolbar.getChildren().add(initListButtons(item, "toolbar-button"))
        );
        rightToolbar.getChildren().add(initAddCourseButton(ADD_COURSE, "toolbar-button"));
        initListCourses("allCourse");
    }

    private Button initListButtons(ButtonModel item, String styleClass) {
        Button button = new Button(item.getText());
        if(Objects.equals(item.getKey(), "allCourse")) {
            button.getStyleClass().add("action");
        }
        button.getStyleClass().add(styleClass);
        button.setId(item.getKey());
        button.setOnMouseClicked(event -> {
            leftToolbar.getChildren().forEach(children ->
                    children.getStyleClass().remove("action"));
            button.getStyleClass().add("action");
            initListCourses(item.getKey());
        });
        return button;
    }

    private Button initAddCourseButton(ButtonModel item, String styleClass) {
        Button button = new Button(item.getText());
        button.getStyleClass().add(styleClass);
        button.setId(item.getKey());
        return button;
    }

    public void initListCourses(String type) {
        content.getChildren().clear();
        try {
            List<Course> courses = CourseBll.getInstance().getAllCourse();
            switch (type) {
                case "onlineCourse": {
                    courses = courses.stream().filter(item -> item instanceof OnlineCourse).collect(Collectors.toList());
                    break;
                }
                case "inPersonCourse": {
                    courses = courses.stream().filter(item -> item instanceof OnsiteCourse).collect(Collectors.toList());
                    break;
                }
                case "allCourse": {
                    break;
                }
                default: {
                    courses = courses.stream().filter(item -> item.getTitle().equalsIgnoreCase(type)).collect(Collectors.toList());
                    System.out.println(type);
                }
            }
            for (Course course : courses) {
                FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.COURSE_ITEM.getValue()));
                Parent root = loader.load();
                CourseItemController controller = loader.getController();
                controller.setData(course);
                content.getChildren().add(root);
            }

        } catch (IOException e) {
            Logger.getLogger(ListCourseController.class.getName()).log(Level.SEVERE, "Error", e);
        }
    }

    public void test() {
        System.out.println("Click Scroll");
    }
}
