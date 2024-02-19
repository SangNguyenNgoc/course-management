package com.example.coursemanagement.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmptyController {

    @FXML
    public Label message;

    public void setText(String text) {
        message.setText(text);
    }
}
