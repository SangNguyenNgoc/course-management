package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.gui.page.Component;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public Button searchButton;
    public BorderPane mainPane;
    public TextField searchField;

    private FXMLLoader loader;

    private Parent root = null;

    private DashboardController controller;


    @FXML
    protected void onSearchButtonClick() {
        if(!searchField.getText().trim().isEmpty()) {
            try {
                controller.search(searchField.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onSearchButtonClick();
        }
            System.out.println("enter");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loader = new FXMLLoader(HomeApplication.class.getResource(Component.DASHBOARD.getValue()));
            root = loader.load();
            mainPane.setCenter(root);
            controller = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}