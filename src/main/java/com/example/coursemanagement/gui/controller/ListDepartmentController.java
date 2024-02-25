package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.DepartmentBll;
import com.example.coursemanagement.dtos.Department;
import com.example.coursemanagement.gui.page.Component;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ListDepartmentController {
    public VBox content;

    public void initListDepartment() throws IOException {
        List<Department> departments = DepartmentBll.getInstance().getAll();
        if(!departments.isEmpty()) {
            for (Department department : departments) {
                FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.DEPARTMENT_ITEM.getValue()));
                Parent root = loader.load();
                DepartmentItemController controller = loader.getController();
                controller.initDepartment(department);
                content.getChildren().add(root);
            }
        } else {
            FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.EMPTY.getValue()));
            Parent root = loader.load();
            EmptyController controller = loader.getController();
            controller.setText("Không có khoa !!!");
            content.getChildren().add(root);
        }
    }
}
