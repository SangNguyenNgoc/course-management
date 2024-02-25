package com.example.coursemanagement.gui.model;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.dtos.StudentGrace;
import com.example.coursemanagement.gui.controller.CourseDetailController;
import com.example.coursemanagement.gui.controller.UpdateGradeController;
import com.example.coursemanagement.gui.page.Component;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class TableCellWithButton extends TableCell<StudentGrace, Void> {
    private final Button button;

    public TableCellWithButton(Integer id, CourseDetailController courseDetailController) {
        this.button = new Button("Chỉnh sửa điểm");
        this.button.getStylesheets().add(Objects.requireNonNull(HomeApplication.class.getResource("style/cell-button.css")).toExternalForm());
        button.setOnMouseEntered(event -> {
            button.setUnderline(true);
        });
        button.setOnMouseExited(event -> {
            button.setUnderline(false);
        });
        button.setOnAction(event -> {
            StudentGrace studentGrace = getTableView().getItems().get(getIndex());
            FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.UPDATE_GRADE.getValue()));
            Parent root = null;
            try {
                root = loader.load();
                UpdateGradeController<CourseDetailController> controller = loader.getController();
                controller.setStudentGrace(studentGrace);
                controller.setCourseId(id);
                controller.setController(courseDetailController);
                Stage stage = new Stage();
                controller.setStage(stage);
                controller.initialize("updateGrade");
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.UTILITY);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
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