package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.DepartmentBll;
import com.example.coursemanagement.bll.dtos.Department;
import com.example.coursemanagement.gui.page.Component;
import com.example.coursemanagement.bll.utils.AppUtil;
import com.example.coursemanagement.gui.utils.DialogUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;

public class DepartmentItemController {
    public Label col1;
    public Label col2;
    public Label col3;
    public Label col4;
    public Label col5;

    ListDepartmentController listDepartmentController;

    private Department department;

    public void initDepartment(Department department, ListDepartmentController listDepartmentController) {
        this.listDepartmentController = listDepartmentController;
        this.department = department;
        col1.setText(this.department.getId().toString());
        col2.setText(this.department.getName());
        col3.setText(AppUtil.getInstance().formatDate(this.department.getStartDate()));
        col4.setText(this.department.getBudget().toString());
        col5.setText(this.department.getAdministrator());
    }

    public void initDepartment(Integer departmentId) {
        this.department = DepartmentBll.getInstance().getById(departmentId).orElse(null);
        if(this.department != null) {
            col1.setText(this.department.getId().toString());
            col2.setText(this.department.getName());
            col3.setText(AppUtil.getInstance().formatDate(this.department.getStartDate()));
            col4.setText(this.department.getBudget().toString());
            col5.setText(this.department.getAdministrator());
        }
    }

    public void updateDepartment() {
        Optional<Department> d = DepartmentBll.getInstance().getDepartmentById(department.getId());
        if(d.isPresent()) {
            FXMLLoader loader = new FXMLLoader(
                    HomeApplication.class.getResource(Component.ADD_DEPARTMENT.getValue()));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            AddDepartmentController<DepartmentItemController> addDepartmentController = loader.getController();
            addDepartmentController.setDepartment(department);
            addDepartmentController.setController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        }
    }


    public void deleteDepartment() {
        if(DialogUtil.getInstance().showConfirm("Xóa khoa", "Bạn có chăc chắn muốn xóa khoa này?")) {
            try {
                if(DepartmentBll.getInstance().deleteDepartment(department.getId()) != 0) {
                    DialogUtil.getInstance().showAlert(
                            "Thông báo", "Xóa thành công.", Alert.AlertType.INFORMATION);
                    listDepartmentController.initList();
                } else {
                    DialogUtil.getInstance().showAlert(
                            "Lỗi", "Xóa không thành công, lỗi bất định.", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                DialogUtil.getInstance().showAlert(
                        "Lỗi", "Xóa không thành công.", Alert.AlertType.ERROR);
            }
        }
    }
}
