package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.StudentBll;
import com.example.coursemanagement.bll.TeacherBll;
import com.example.coursemanagement.bll.dtos.Student;
import com.example.coursemanagement.bll.dtos.Teacher;
import com.example.coursemanagement.gui.page.Component;
import com.example.coursemanagement.bll.utils.AppUtil;
import com.example.coursemanagement.gui.utils.DialogUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;

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
        col1.setText(student.getId() + "");
        col2.setText(student.getLastName() + " " + student.getFirstName());
        col3.setText(AppUtil.getInstance().formatDate(student.getEnrollmentDate()));
    }

    public void initPerson(Teacher teacher) {
        setState("teacher");
        setTeacher(teacher);
        col1.setText(teacher.getId() + "");
        col2.setText(teacher.getLastName() + " " + teacher.getFirstName());
        col3.setText(AppUtil.getInstance().formatDate(teacher.getHireDate().toLocalDate()));
    }

    public void updateAction(ActionEvent actionEvent) {
        if(state.equals("student")) {
            Optional<Student> s = StudentBll.getInstance().getStudentById(student.getId());
            if(s.isPresent()) {
                FXMLLoader loader = new FXMLLoader(
                        HomeApplication.class.getResource(Component.ADD_PERSON.getValue())
                );
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
            }
        } else {
            Optional<Teacher> t = TeacherBll.getInstance().getTeacherById(teacher.getId());
            if(t.isPresent()) {
                FXMLLoader loader = new FXMLLoader(
                        HomeApplication.class.getResource(Component.ADD_PERSON.getValue()));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                AddPersonController<PersonItemController> controller = loader.getController();
                controller.setTeacher(teacher);
                controller.setState("updateTeacher");
                controller.setController(this);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.UTILITY);
                stage.show();
            }
        }
    }

    public void deleteAction(ActionEvent actionEvent) {
        if(state.equals("student")) {
            if(DialogUtil.getInstance().showConfirm("Xóa sinh viên", "Bạn có chăc chắn muốn xóa sinh viên này?")) {
                int result = StudentBll.getInstance().deleteStudent(student.getId());
                if(result != 0) {
                    controller.initListStudent();
                    DialogUtil.getInstance().showAlert(
                            "Thông báo",
                            "Xóa thành công.", Alert.AlertType.INFORMATION);
                } else {
                    DialogUtil.getInstance().showAlert(
                            "Lỗi", "Xóa không thành công.", Alert.AlertType.ERROR);
                }
            }
        } else {
            if(DialogUtil.getInstance().showConfirm("Xóa giảng viên", "Bạn có chăc chắn muốn xóa giảng viên này?")) {
                int result = TeacherBll.getInstance().deleteTeacher(teacher.getId());
                if(result != 0) {
                    controller.initListTeacher();
                    DialogUtil.getInstance().showAlert(
                            "Thông báo", "Xóa thành công.", Alert.AlertType.INFORMATION);
                } else {
                    DialogUtil.getInstance().showAlert(
                            "Lỗi", "Xóa không thành công.", Alert.AlertType.ERROR);
                }
            }
        }
    }
}
