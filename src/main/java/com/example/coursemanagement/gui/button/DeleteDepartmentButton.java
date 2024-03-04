package com.example.coursemanagement.gui.button;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.dtos.Department;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.util.Objects;

public class DeleteDepartmentButton extends TableCell<Department, Void> {
    private final Button button;

    public DeleteDepartmentButton() {

        this.button = new Button("Xóa");
        this.button.getStylesheets().add(Objects.requireNonNull(HomeApplication.class.getResource("style/cell-button.css")).toExternalForm());
        button.setOnMouseEntered(event -> {
            button.setUnderline(true);
        });
        button.setOnMouseExited(event -> {
            button.setUnderline(false);
        });
        button.setOnMouseClicked(event -> {
            System.out.println(getIndex());
            Department department = getTableView().getItems().get(getIndex());
            System.out.println(department.getId());
        });
    }

    public void setButton(int index) {
        if(getIndex() == index) {
            this.button.setText("Cập nhật");
        }

    }

    @Override
    protected void updateItem(Void unused, boolean b) {
        super.updateItem(unused, b);
        if (b) {
            setGraphic(null);
        } else {
            setGraphic(button);
        }
    }
}
