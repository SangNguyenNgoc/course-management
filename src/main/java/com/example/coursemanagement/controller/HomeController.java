package com.example.coursemanagement.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.page.Component;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public Button searchButton;
    public BorderPane mainPane;
    public TextField searchField;


    @FXML
    protected void onSearchButtonClick() {
        try {
            if(!searchField.getText().trim().isEmpty()) {
                FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.DASHBOARD.getValue()));
                Parent root = loader.load();
                mainPane.setCenter(root);
                DashboardController controller = loader.getController();
                controller.initListCourses(searchField.getText());
                searchField.setText("");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.DASHBOARD.getValue()));
            Parent root = null;
            root = loader.load();
            mainPane.setCenter(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}