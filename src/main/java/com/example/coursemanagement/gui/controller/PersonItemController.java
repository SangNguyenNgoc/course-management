package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.StudentBll;
import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.dtos.Teacher;
import com.example.coursemanagement.gui.page.Component;
import com.example.coursemanagement.utils.AppUtil;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class PersonItemController {
    public Label col1;
    public Label col2;
    public Label col3;
    public Label col4;

    private String state;

    private Student student;

    private Teacher teacher;

    private ListPersonController controller;

    public void setState(String state) {
        this.state = state;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setController(ListPersonController controller) {
        this.controller = controller;
    }

    public void initPerson(Student student) {
        setState("student");
        setStudent(student);
        col1.setText(student.getFirstName());
        col2.setText(student.getLastName());
        col3.setText(AppUtil.getInstance().formatDate(student.getEnrollmentDate()));
    }

    public void initPerson(Teacher teacher) {
        setState("teacher");
        setTeacher(teacher);
        col1.setText(teacher.getFirstName());
        col2.setText(teacher.getLastName());
        col3.setText(AppUtil.getInstance().formatDate(teacher.getHireDate().toLocalDate()));
    }

    public void updateAction(ActionEvent actionEvent) {
        if(state.equals("student")) {
            FXMLLoader loader = new FXMLLoader(HomeApplication.class.getResource(Component.ADD_PERSON.getValue()));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            AddPersonController<PersonItemController> controller = loader.getController();
            controller.setStudent(student);
            controller.setState("updateStudent");
            controller.setController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } else {
            System.out.println("updateTeacher");
        }
    }

    public void deleteAction(ActionEvent actionEvent) {
        int result = StudentBll.getInstance().deleteStudent(student.getId());
        if(result != 0) {
            controller.initListStudent();
            DialogUtil.getInstance().showAlert("Thông báo", "Xóa thành công.", Alert.AlertType.INFORMATION);
        } else {
            DialogUtil.getInstance().showAlert("Lỗi", "Xóa không thành công.", Alert.AlertType.ERROR);
        }
    }
}
