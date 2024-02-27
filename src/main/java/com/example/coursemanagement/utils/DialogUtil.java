package com.example.coursemanagement.utils;

import javafx.scene.control.Alert;

public class DialogUtil {

    private static class DialogUtilHolder{
        private static final DialogUtil INSTANCE = new DialogUtil();
    }

    private DialogUtil() {

    }

    public static DialogUtil getInstance() {
        return DialogUtilHolder.INSTANCE;
    }

    public void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
