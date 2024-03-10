package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.dtos.Course;
import com.example.coursemanagement.gui.button.ButtonModel;
import com.example.coursemanagement.gui.page.Component;
import com.example.coursemanagement.gui.page.Route;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

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

    @Getter
    private String stage;

    private Integer courseId;

    public void setStage(String stage) {
        this.stage = stage;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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

    private static final ButtonModel FILTER_BTN = ButtonModel.builder().key("filter").text("Lọc").build();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearLeftToolbar();
        clearRightToolbar();
        addButtonLeftToolbar(initCourseToolbarButtons(TOOLBAR_BTN.get(0)));
        addButtonLeftToolbar(initDepartmentToolbarBtn(TOOLBAR_BTN.get(1)));
        addButtonLeftToolbar(initStudentToolbarBtn(TOOLBAR_BTN.get(2)));
        addButtonLeftToolbar(initTeacherToolbarBtn(TOOLBAR_BTN.get(3)));
        addButtonRightToolbar(initAddButton());
        addButtonRightToolbar(initFilterButton());
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
                    if(rightToolbar.getChildren().size() > 1) {
                        rightToolbar.getChildren().remove(1);
                    }
                    addButtonRightToolbar(initFilterButton());
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
            if(rightToolbar.getChildren().size() > 1) {
                rightToolbar.getChildren().remove(1);
            }
            leftToolbar.getChildren().forEach(children ->
                    children.getStyleClass().remove("action"));
            button.getStyleClass().add("action");
            try {
                initListStudent(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return button;
    }

    private MenuButton initDepartmentToolbarBtn(ButtonModel item) {
        MenuButton button = new MenuButton(item.getText());
        button.getStyleClass().addAll("toolbar-button", "menu-button");
        button.setId(item.getKey());
        button.setOnMouseClicked(event -> {
            if(rightToolbar.getChildren().size() > 1) {
                rightToolbar.getChildren().remove(1);
            }
            leftToolbar.getChildren().forEach(children ->
                    children.getStyleClass().remove("action"));
            button.getStyleClass().add("action");
            try {
                initListDepartment(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return button;
    }

    private MenuButton initTeacherToolbarBtn(ButtonModel item) {
        MenuButton button = new MenuButton(item.getText());
        button.getStyleClass().addAll("toolbar-button");
        button.setId(item.getKey());
        button.setOnMouseClicked(event -> {
            if(rightToolbar.getChildren().size() > 1) {
                rightToolbar.getChildren().remove(1);
            }
            leftToolbar.getChildren().forEach(children ->
                    children.getStyleClass().remove("action"));
            button.getStyleClass().add("action");
            try {
                initListTeacher(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return button;
    }


    public void initListCourses(String key) {
        try {
            setStage(key);
            body.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(
                    HomeApplication.class.getResource(Component.LIST_COURSE.getValue()));
            Parent root = null;
            root = loader.load();
            ListCourseController controller = loader.getController();
            controller.initListCourses(key, this);
            body.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initListCourses(List<Course> courses) {
        try {
            body.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.LIST_COURSE.getValue()));
            Parent root = null;
            root = loader.load();
            ListCourseController controller = loader.getController();
            controller.render(courses, this);
            body.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Button initFilterButton() {
        Button button = new Button(DashboardController.FILTER_BTN.getText());
        button.getStyleClass().add("toolbar-button");
        button.setId(DashboardController.ADD_BTN.getKey());
        button.setOnMouseClicked(event -> {
            FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.COURSE_FILTER.getValue()));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            CourseFilterController controller = loader.getController();
            Stage stage = new Stage();
            controller.initToFilter(this, stage);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        });
        return button;
    }


    private Button initAddButton() {
        Button button = new Button(DashboardController.ADD_BTN.getText());
        button.getStyleClass().add("toolbar-button");
        button.setId(DashboardController.ADD_BTN.getKey());
        button.setOnAction(event -> {
            switch (stage) {
                case "course", "allCourse", "onlineCourse", "onsiteCourse": {
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
                    FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.REGISTER.getValue()));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    RegisterController<DashboardController> controller = loader.getController();
                    Stage stage = new Stage();
                    controller.setStage(stage);
                    controller.setCourseId(courseId);
                    controller.initialize("register");
                    controller.setController(this);
                    stage.setScene(new Scene(root));
                    stage.initStyle(StageStyle.UTILITY);
                    stage.show();
                    break;
                }

                case "departments": {
                    FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.ADD_DEPARTMENT.getValue()));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AddDepartmentController<DashboardController> addDepartmentController = loader.getController();
                    addDepartmentController.setController(this);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.initStyle(StageStyle.UTILITY);
                    stage.show();
                    break;
                }

                case "students": {
                    FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.ADD_PERSON.getValue()));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AddPersonController<DashboardController> controller = loader.getController();
                    controller.setState("addStudent");
                    controller.setController(this);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.initStyle(StageStyle.UTILITY);
                    stage.show();
                    break;
                }

                case "teachers": {
                    FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.ADD_PERSON.getValue()));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AddPersonController<DashboardController> controller = loader.getController();
                    controller.setState("addTeacher");
                    controller.setController(this);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.initStyle(StageStyle.UTILITY);
                    stage.show();
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

    public void initCourseDetail(Integer id, String studentSearch) throws IOException {
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
        setCourseId(id);
        if(studentSearch == null) {
            controller.initCourseDetail(id);
        } else {
            controller.initCourseDetail(id, studentSearch);
        }
        body.getChildren().add(root);
    }

    public void initListDepartment(String key) throws IOException {
        setStage("departments");
        body.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.LIST_DEPARTMENT.getValue()));
        Parent root = null;
        root = loader.load();
        ListDepartmentController controller = loader.getController();
        if(key == null) {
            controller.initList();
        } else {
            controller.initList(key);
        }
        body.getChildren().add(root);

    }

    public void initListStudent(String key) throws IOException {
        setStage("students");
        body.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.LIST_PERSON.getValue()));
        Parent root = null;
        root = loader.load();
        ListPersonController controller = loader.getController();
        controller.setState("student");
        if(key == null) {
            controller.initList();
        } else {
            controller.initList(key);
        }
        body.getChildren().add(root);
    }

    public void initListTeacher(String key) throws IOException {
        setStage("teachers");
        body.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.LIST_PERSON.getValue()));
        Parent root = null;
        root = loader.load();
        ListPersonController controller = loader.getController();
        controller.setState("teacher");
        if(key == null) {
            controller.initList();
        } else {
            controller.initList(key);
        }
        body.getChildren().add(root);
    }

    public void search(String text) throws IOException {
        if(stage.equals("departments")) {
            initListDepartment(text);
            return;
        }
        if(stage.equals("students")) {
            initListStudent(text);
            return;
        }
        if(stage.equals("teachers")) {
            initListTeacher(text);
            return;
        }
        if(stage.equals("courseDetail")) {
            initCourseDetail(courseId, text);
            return;
        }
        initListCourses(text);
    }
}
