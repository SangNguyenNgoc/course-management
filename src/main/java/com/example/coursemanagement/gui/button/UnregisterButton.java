package com.example.coursemanagement.gui.button;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.StudentBll;
import com.example.coursemanagement.bll.dtos.StudentGrade;
import com.example.coursemanagement.gui.controller.CourseDetailController;
import com.example.coursemanagement.gui.utils.DialogUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;

import java.util.Objects;
import java.util.Optional;

public class UnregisterButton extends TableCell<StudentGrade, Void> {
    private final Button button;

    public UnregisterButton(Integer courseId, CourseDetailController courseDetailController) {
        this.button = new Button("Hủy kết quả");
        this.button.getStylesheets().add(Objects.requireNonNull(
                HomeApplication.class.getResource("style/cell-button.css")).toExternalForm());
        button.setOnMouseEntered(event -> {
            button.setUnderline(true);
        });
        button.setOnMouseExited(event -> {
            button.setUnderline(false);
        });
        button.setOnAction(event -> {
            StudentGrade studentGrade = getTableView().getItems().get(getIndex());
            if(DialogUtil.getInstance().showConfirm("Hủy kêt quả", "Bạn có chắc chắn muốn hủy kết quả của sinh viên này?")) {
                try {
                    int result = StudentBll.getInstance().deleteGrade(studentGrade.getId(), courseId);
                    if(result != 0) {
                        DialogUtil.getInstance().showAlert(
                                "Thông báo", "Hủy kết quả thành công.", Alert.AlertType.INFORMATION);
                    } else {
                        DialogUtil.getInstance().showAlert(
                                "Lỗi", "Hủy kết quả thất bại.", Alert.AlertType.ERROR);
                    }
                    courseDetailController.initCourseDetail(courseId);
                } catch (Exception e) {
                    DialogUtil.getInstance().showAlert(
                            "Lỗi", "Hủy kết quả thất bại.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(button);
        }
    }
}