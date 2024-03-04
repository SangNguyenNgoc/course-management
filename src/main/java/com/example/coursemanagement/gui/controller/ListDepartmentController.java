package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.DepartmentBll;
import com.example.coursemanagement.bll.dtos.Department;
import com.example.coursemanagement.gui.page.Component;
import com.example.coursemanagement.bll.utils.AppUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ListDepartmentController {
    public VBox content;

    public void initList() {
        content.getChildren().clear();
        List<Department> departments = DepartmentBll.getInstance().getAll();
        renderList(departments);
    }

    private void renderList(List<Department> departments) {
        if(!departments.isEmpty()) {
            for (Department department : departments) {
                FXMLLoader loader = new FXMLLoader(
                        HomeApplication.class.getResource(Component.DEPARTMENT_ITEM.getValue()));
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

    public void initList(String key) {
        content.getChildren().clear();
        List<Department> departments = DepartmentBll.getInstance().getAll();
        if(AppUtil.getInstance().isInteger(key)) {
            departments = departments.stream().filter(item -> item.getId().toString().equals(key)).collect(Collectors.toList());
        } else {
            departments = departments.stream().filter(item -> item.getName().toLowerCase().contains(key.toLowerCase())).collect(Collectors.toList());
        }
        renderList(departments);
    }


}
