package com.example.coursemanagement.gui.utils;

import com.example.coursemanagement.bll.StudentBll;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

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

    public boolean showConfirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> reply = alert.showAndWait();
        return reply.isPresent() && reply.get() == ButtonType.OK;
    }
}
