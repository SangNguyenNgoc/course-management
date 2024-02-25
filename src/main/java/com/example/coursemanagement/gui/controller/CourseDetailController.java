package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.bll.PersonBll;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.StudentGrace;
import com.example.coursemanagement.gui.model.TableCellWithButton;
import com.example.coursemanagement.gui.page.Component;
import com.example.coursemanagement.utils.AppUtil;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class CourseDetailController {
    public VBox header;
    public TableColumn<StudentGrace, String> idColumn;
    public TableColumn<StudentGrace, String> nameColumn;
    public TableColumn<StudentGrace, String> enrollmentColumn;
    public TableColumn<StudentGrace, String> gradeColumn;
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

            List<StudentGrace> studentGraces = PersonBll.getInstance().getStudentsInCourse(id);
            ObservableList<StudentGrace> students = FXCollections.observableArrayList(studentGraces);
            parentTable.setItems(students);

            idColumn.setCellValueFactory(cellData -> initTableCell(cellData.getValue().getId().toString()));
            nameColumn.setCellValueFactory(cellData -> initTableCell(cellData.getValue().getLastName() + " " + cellData.getValue().getFirstName()));
            enrollmentColumn.setCellValueFactory(cellData -> initTableCell(AppUtil.getInstance().formatDate(cellData.getValue().getEnrollmentDate())));
            gradeColumn.setCellValueFactory(cellData -> {
                if(cellData.getValue().getGrade() == null) {
                    return initTableCell("X");
                }
                return initTableCell(cellData.getValue().getGrade().toString());
            });
            actionColumn.setCellFactory(param -> new TableCellWithButton(id, this));

            parentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            parentTable.setFixedCellSize(30);
        }

    }

    public StringProperty initTableCell(String input) {
        return new SimpleStringProperty(input);
    }



}

