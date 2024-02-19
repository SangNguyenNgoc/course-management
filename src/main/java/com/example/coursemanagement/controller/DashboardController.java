package com.example.coursemanagement.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.OnlineCourse;
import com.example.coursemanagement.dtos.OnsiteCourse;
import com.example.coursemanagement.model.ButtonModel;
import com.example.coursemanagement.page.Component;
import com.example.coursemanagement.page.Route;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.*;
public class DashboardController implements Initializable, Route {


    @FXML
    public HBox leftToolbar;

    @FXML
    public HBox rightToolbar;

    @FXML
    public Pane body;

    private static final List<ButtonModel> TYPE_COURSE_BTN = Arrays.asList(
            ButtonModel.builder().key("allCourse").text("Tất cả").build(),
            ButtonModel.builder().key("onlineCourse").text("Khóa trực tuyến").build(),
            ButtonModel.builder().key("inPersonCourse").text("Khóa tại chỗ").build()
    );

    private static final ButtonModel ADD_COURSE_BTN = ButtonModel.builder().key("addCourse").text("Tạo khóa học").build();

    private static final ButtonModel BACK_BTN = ButtonModel.builder().key("back").text("Quay lại").build();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearLeftToolbar();
        clearRightToolbar();
        TYPE_COURSE_BTN.forEach(item ->
                addButtonLeftToolbar(initTypeCourseButtons(item))
        );
        addButtonRightToolbar(initAddCourseButton());
        initListCourses("allCourse");
    }

    private Button initTypeCourseButtons(ButtonModel item) {
        Button button = new Button(item.getText());
        if(Objects.equals(item.getKey(), "allCourse")) {
            button.getStyleClass().add("action");
        }
        button.getStyleClass().add("toolbar-button");
        button.setId(item.getKey());
        button.setOnMouseClicked(event -> {
            leftToolbar.getChildren().forEach(children ->
                    children.getStyleClass().remove("action"));
            button.getStyleClass().add("action");
            initListCourses(item.getKey());
        });
        return button;
    }

    public void initListCourses(String key) {
        try {
            body.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.LIST_COURSE.getValue()));
            Parent root = null;
            root = loader.load();
            ListCourseController controller = loader.getController();
            controller.initListCourses(key);
            body.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Button initAddCourseButton() {
        Button button = new Button(DashboardController.ADD_COURSE_BTN.getText());
        button.getStyleClass().add("toolbar-button");
        button.setId(DashboardController.ADD_COURSE_BTN.getKey());
        button.setOnMouseClicked(event -> {
            try {
                clearLeftToolbar();
                button.getStyleClass().add("action");
                addButtonLeftToolbar(initBackButton());
                changeView(Component.ADD_COURSE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return button;
    }

    private void addCourseEvent() {

    }

    private Button initBackButton() {
        Button button = new Button(DashboardController.BACK_BTN.getText());
        button.getStyleClass().add("toolbar-button");
        button.setId(DashboardController.BACK_BTN.getKey());
        button.setOnMouseClicked(event -> {
            initialize(null, null);
        });
        return button;
    }


    @Override
    public void changeView(Component component) throws IOException {
        body.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(component.getValue()));
        Parent root = null;
        root = loader.load();
        body.getChildren().add(root);
    }

    private void clearLeftToolbar() {
        leftToolbar.getChildren().clear();
    }

    private void clearRightToolbar() {
        rightToolbar.getChildren().clear();
    }

    private void addButtonLeftToolbar(Button button) {
        leftToolbar.getChildren().add(button);
    }

    private void addButtonRightToolbar(Button button) {
        rightToolbar.getChildren().add(button);
    }
}
