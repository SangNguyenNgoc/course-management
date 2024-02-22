package com.example.coursemanagement.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.model.ButtonModel;
import com.example.coursemanagement.page.Component;
import com.example.coursemanagement.page.Route;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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

    private String stage;

    public void setStage(String stage) {
        this.stage = stage;
    }

    private static final List<ButtonModel> TOOLBAR_BTN = Arrays.asList(
            ButtonModel.builder().key("course").text("Khóa học").build(),
            ButtonModel.builder().key("department").text("Khoa").build(),
            ButtonModel.builder().key("student").text("Sinh viên").build(),
            ButtonModel.builder().key("teacher").text("Giảng viên").build()
    );

    private static final List<ButtonModel> COURSE_BTN = Arrays.asList(
            ButtonModel.builder().key("allCourse").text("Tất cả").build(),
            ButtonModel.builder().key("onlineCourse").text("Khóa trực tuyến").build(),
            ButtonModel.builder().key("onsiteCourse").text("Khóa tại chỗ").build()
    );

    private static final ButtonModel BACK_BTN = ButtonModel.builder().key("back").text("Quay lại").build();

    private static final ButtonModel ADD_BTN = ButtonModel.builder().key("add").text("Thêm mới").build();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearLeftToolbar();
        clearRightToolbar();
        addButtonLeftToolbar(initCourseToolbarButtons(TOOLBAR_BTN.get(0)));
        addButtonLeftToolbar(initStudentToolbarBtn(TOOLBAR_BTN.get(1)));
        addButtonLeftToolbar(initDepartmentToolbarBtn(TOOLBAR_BTN.get(2)));
        addButtonLeftToolbar(initTeacherToolbarBtn(TOOLBAR_BTN.get(3)));
        addButtonRightToolbar(initAddButton());
        initListCourses("allCourse");
    }

    private MenuButton initCourseToolbarButtons(ButtonModel item) {
        MenuButton button = new MenuButton(item.getText());
        button.getStyleClass().addAll("toolbar-button", "menu-button", "action");
        button.setId(item.getKey());
        COURSE_BTN.forEach(btn -> {
            MenuItem option = new MenuItem(btn.getText());
            button.getItems().add(option);
            option.setOnAction(actionEvent -> {
                if(Objects.equals(btn.getKey(), "addCourse")) {
                    try {
                        clearLeftToolbar();
                        clearRightToolbar();
                        addButtonLeftToolbar(initBackButton());
                        changeView(Component.ADD_COURSE);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    initListCourses(btn.getKey());
                }
                leftToolbar.getChildren().forEach(children ->
                        children.getStyleClass().remove("action"));
                button.getStyleClass().add("action");
            });

        });
        return button;
    }

    private MenuButton initStudentToolbarBtn(ButtonModel item) {
        MenuButton button = new MenuButton(item.getText());
        button.getStyleClass().addAll("toolbar-button", "menu-button");
        button.setId(item.getKey());
        button.setOnMouseClicked(event -> {
            leftToolbar.getChildren().forEach(children ->
                    children.getStyleClass().remove("action"));
            button.getStyleClass().add("action");
        });
        return button;
    }

    private MenuButton initDepartmentToolbarBtn(ButtonModel item) {
        MenuButton button = new MenuButton(item.getText());
        button.getStyleClass().addAll("toolbar-button", "menu-button");
        button.setId(item.getKey());
        button.setOnMouseClicked(event -> {
            leftToolbar.getChildren().forEach(children ->
                    children.getStyleClass().remove("action"));
            button.getStyleClass().add("action");
        });
        return button;
    }

    private MenuButton initTeacherToolbarBtn(ButtonModel item) {
        MenuButton button = new MenuButton(item.getText());
        button.getStyleClass().addAll("toolbar-button");
        button.setId(item.getKey());
        button.setOnMouseClicked(event -> {
            leftToolbar.getChildren().forEach(children ->
                    children.getStyleClass().remove("action"));
            button.getStyleClass().add("action");
        });
        return button;
    }


    public void initListCourses(String key) {
        try {
            setStage("course");
            body.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.LIST_COURSE.getValue()));
            Parent root = null;
            root = loader.load();
            ListCourseController controller = loader.getController();
            controller.initListCourses(key, this);
            body.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Button initAddButton() {
        Button button = new Button(DashboardController.ADD_BTN.getText());
        button.getStyleClass().add("toolbar-button");
        button.setId(DashboardController.ADD_BTN.getKey());
        button.setOnAction(event -> {
            leftToolbar.getChildren().forEach(children ->
                    children.getStyleClass().remove("action"));
            button.getStyleClass().add("action");
            switch (stage) {
                case "course": {
                    try {
                        clearLeftToolbar();
                        clearRightToolbar();
                        body.getChildren().clear();
                        addButtonLeftToolbar(initBackButton());
                        changeView(Component.ADD_COURSE);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }

                case "courseDetail": {
                    System.out.println("Register");
                    break;
                }
            }
        });
        return button;
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

    public void clearLeftToolbar() {
        leftToolbar.getChildren().clear();
    }

    public void clearRightToolbar() {
        rightToolbar.getChildren().clear();
    }

    public void addButtonLeftToolbar(MenuButton button) {
        leftToolbar.getChildren().add(button);
    }

    public void addButtonLeftToolbar(Button button) {
        leftToolbar.getChildren().add(button);
    }

    public void addButtonRightToolbar(Button button) {
        rightToolbar.getChildren().add(button);
    }

    public void initCourseDetail(Integer id) throws IOException {
        setStage("courseDetail");
        clearLeftToolbar();
        clearRightToolbar();
        body.getChildren().clear();
        addButtonLeftToolbar(initBackButton());
        addButtonRightToolbar(initAddButton());
        FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.COURSE_DETAIL.getValue()));
        Parent root = null;
        root = loader.load();
        CourseDetailController controller = loader.getController();
        controller.initCourseDetail(id);
        body.getChildren().add(root);
    }
}
