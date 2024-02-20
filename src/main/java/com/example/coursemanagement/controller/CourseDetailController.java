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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class CourseDetailController {
    public VBox header;
    public TableColumn<StudentGrace, String> firstnameColumn;
    public TableColumn<StudentGrace, String> lastnameColumn;
    public TableColumn<StudentGrace, String> enrollmentColumn;
    public TableColumn<StudentGrace, Double> gradeColumn;
    public TableColumn<StudentGrace, String> actionColumn;
    public TableView<StudentGrace> parentTable;

    public void initCourseDetail(Integer id) throws IOException {
        FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.COURSE_ITEM.getValue()));
        Parent root = loader.load();
        CourseItemController controller = loader.getController();
        Course course = CourseBll.getInstance().getById(id);
        controller.setHeaderDetail(course);
        header.getChildren().add(0, root);

        List<StudentGrace> studentGraces = StudentBll.getInstance().getStudentsInCourse(id);
        ObservableList<StudentGrace> students = FXCollections.observableArrayList(studentGraces);
        parentTable.setItems(students);

        firstnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        enrollmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnrollmentDate()));
        gradeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getGrade()).asObject());

    }
}
