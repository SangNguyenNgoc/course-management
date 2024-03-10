package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.bll.StudentBll;
import com.example.coursemanagement.bll.dtos.Course;
import com.example.coursemanagement.bll.dtos.StudentGrade;
import com.example.coursemanagement.gui.button.UnregisterButton;
import com.example.coursemanagement.gui.page.Component;
import com.example.coursemanagement.bll.utils.AppUtil;
import com.example.coursemanagement.gui.utils.DialogUtil;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CourseDetailController {
    public VBox header;
    public TableColumn<StudentGrade, String> idColumn;
    public TableColumn<StudentGrade, String> nameColumn;
    public TableColumn<StudentGrade, Object> resultColumn;
    public TableColumn<StudentGrade, String> gradeColumn;
    public TableColumn<StudentGrade, Void> actionColumn;
    public TableView<StudentGrade> parentTable;


    public void initCourseDetail(Integer id) throws IOException {
        if (header.getChildren().size() > 1) {
            header.getChildren().remove(0);
        }
        FXMLLoader loader = new FXMLLoader(
                HomeApplication.class.getResource(Component.COURSE_ITEM.getValue()));
        Parent root = loader.load();
        CourseItemController controller = loader.getController();
        Course course = CourseBll.getInstance().getById(id);
        if (course != null) {
            controller.setHeaderDetail(course);
            header.getChildren().add(0, root);
            parentTable.setEditable(true);
            List<StudentGrade> studentGrades = StudentBll.getInstance().getStudentsInCourse(id);
            renderTable(id, studentGrades);
        }
    }

    public void initCourseDetail(Integer id, String search) throws IOException {
        if (header.getChildren().size() > 1) {
            header.getChildren().remove(0);
        }
        FXMLLoader loader = new FXMLLoader(
                HomeApplication.class.getResource(Component.COURSE_ITEM.getValue()));
        Parent root = loader.load();
        CourseItemController controller = loader.getController();
        Course course = CourseBll.getInstance().getById(id);
        if (course != null) {
            controller.setHeaderDetail(course);
            header.getChildren().add(0, root);
            parentTable.setEditable(true);
            List<StudentGrade> studentGrades = StudentBll.getInstance().getStudentsInCourse(id, search);
            renderTable(id, studentGrades);
        }
    }

    private void renderTable(Integer id, List<StudentGrade> studentGrades) {
        ObservableList<StudentGrade> students = FXCollections.observableArrayList(studentGrades);
        parentTable.setItems(students);

        idColumn.setCellValueFactory(cellData -> initTableCell(cellData.getValue().getId().toString()));
        nameColumn.setCellValueFactory(cellData -> initTableCell(
                        cellData.getValue().getLastName() + " " + cellData.getValue().getFirstName()));
        gradeColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getGrade() == null) {
                return initTableCell("X");
            }
            return initTableCell(cellData.getValue().getGrade().toString());
        });
        gradeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        gradeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StudentGrade, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<StudentGrade, String> studentGraceDoubleCellEditEvent) {
                StudentGrade studentGrade = studentGraceDoubleCellEditEvent.getRowValue();
                String input = studentGraceDoubleCellEditEvent.getNewValue();
                try {
                    StudentBll.getInstance().updateGrade(studentGrade.getId(), id, input);
                    DialogUtil.getInstance().showAlert(
                            "Thông báo", "Thay đổi điểm thành công", Alert.AlertType.INFORMATION);
                    studentGrade.setGrade(Double.parseDouble(input));
                    studentGraceDoubleCellEditEvent.getTableView().refresh();
                } catch (Exception e) {
                    DialogUtil.getInstance().showAlert(
                            "Lỗi", "Thay đổi điểm không thành công", Alert.AlertType.ERROR);
                }
            }
        });
        resultColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getGrade() == null) {
                return new SimpleObjectProperty<>(createLabel("X", "black"));
            } else {
                if (cellData.getValue().getGrade() < 4) {
                    return new SimpleObjectProperty<>(createLabel("X", "#cd4242"));
                }
                return new SimpleObjectProperty<>(createLabel("Đạt", "green"));
            }
        });
        actionColumn.setCellFactory(param -> new UnregisterButton(id, this));
        parentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        parentTable.setFixedCellSize(30);
    }

    public StringProperty initTableCell(String input) {
        return new SimpleStringProperty(input);
    }

    private Label createLabel(String text, String color) {
        Label label = new Label(text);
        label.setMinWidth(160);
        label.setStyle("-fx-text-fill: " + color + ";");
        label.setAlignment(Pos.CENTER);
        return label;
    }

}

