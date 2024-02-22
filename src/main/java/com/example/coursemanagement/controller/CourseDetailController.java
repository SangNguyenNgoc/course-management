package com.example.coursemanagement.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.bll.StudentBll;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.StudentGrace;
import com.example.coursemanagement.page.Component;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class CourseDetailController {
    public VBox header;
    public TableColumn<StudentGrace, String> idColumn;
    public TableColumn<StudentGrace, String> nameColumn;
    public TableColumn<StudentGrace, String> enrollmentColumn;
    public TableColumn<StudentGrace, Double> gradeColumn;
    public TableColumn<StudentGrace, Void> actionColumn;
    public TableView<StudentGrace> parentTable;


    public void initCourseDetail(Integer id) throws IOException {
        if (header.getChildren().size() > 1) {
            header.getChildren().remove(0);
        }
        FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.COURSE_ITEM.getValue()));
        Parent root = loader.load();
        CourseItemController controller = loader.getController();
        Course course = CourseBll.getInstance().getById(id);
        if (course != null) {
            controller.setHeaderDetail(course);
            header.getChildren().add(0, root);

            List<StudentGrace> studentGraces = StudentBll.getInstance().getStudentsInCourse(id);
            ObservableList<StudentGrace> students = FXCollections.observableArrayList(studentGraces);
            parentTable.setItems(students);

            idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().toString()));
            nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName() + " " + cellData.getValue().getFirstName()));
            enrollmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnrollmentDate()));
            gradeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getGrade()).asObject());
            actionColumn.setCellFactory(param -> new TableCellWithButton(id, this));

            parentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            parentTable.setFixedCellSize(30);
        }
    }
}

class TableCellWithButton extends TableCell<StudentGrace, Void> {
    private final Button button;

    public TableCellWithButton(Integer id, CourseDetailController courseDetailController) {
        this.button = new Button("Chỉnh sửa điểm");
        this.button.setStyle(
                "-fx-background-color: rgba(0,0,0,0); " +
                        "-fx-border-color: rgba(0,0,0,0); " +
                        "-fx-font-size: 14; " +
                        "-fx-text-fill: #1EB2A6; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 0 0 0 0;");
        button.setOnAction(event -> {
            StudentGrace person = getTableView().getItems().get(getIndex());
            System.out.println("Button clicked for: " + person.toString());
            FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.UPDATE_GRADE.getValue()));
            Parent root = null;
            try {
                root = loader.load();
                UpdateGradeController controller = loader.getController();
                controller.setStudentGrace(person);
                controller.setCourseId(id);
                controller.setController(courseDetailController);
                Stage stage = new Stage();
                controller.setStage(stage);
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
