package com.example.coursemanagement.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.page.Component;
import com.example.coursemanagement.page.Route;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCourseController implements Route, Initializable {
    @FXML
    public Label onlineCourse;

    @FXML
    public Label onsiteCourse;

    @FXML
    public AnchorPane body;

    @Override
    public void changeView(Component component) throws IOException {
        body.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(component.getValue()));
        Parent root = null;
        root = loader.load();
        body.getChildren().add(root);
    }

    public void onclickOnsite(){
        onsiteCourse.setOnMouseClicked(event -> {
            try {
                changeView(Component.ONSITE_COURSE_FORM);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void onclickOnline(){
        onlineCourse.setOnMouseClicked(event -> {
            try {
                changeView(Component.ONLINE_COURSE_FORM);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onclickOnline();
        onclickOnsite();
    }
}
