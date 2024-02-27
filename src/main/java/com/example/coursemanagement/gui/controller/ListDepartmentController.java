package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.DepartmentBll;
import com.example.coursemanagement.dtos.Department;
import com.example.coursemanagement.gui.page.Component;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListDepartmentController implements Initializable {
    public VBox content;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        content.getChildren().clear();
        List<Department> departments = DepartmentBll.getInstance().getAll();
        if(!departments.isEmpty()) {
            for (Department department : departments) {
                FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.DEPARTMENT_ITEM.getValue()));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                DepartmentItemController controller = loader.getController();
                controller.initDepartment(department, this);
                content.getChildren().add(root);
            }
        } else {
            FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.EMPTY.getValue()));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            EmptyController controller = loader.getController();
            controller.setText("Không có khoa !!!");
            content.getChildren().add(root);
        }
    }

}
