package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.HomeApplication;
import com.example.coursemanagement.bll.StudentBll;
import com.example.coursemanagement.bll.TeacherBll;
import com.example.coursemanagement.bll.dtos.Student;
import com.example.coursemanagement.bll.dtos.Teacher;
import com.example.coursemanagement.gui.page.Component;
import com.example.coursemanagement.bll.utils.AppUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListPersonController {
    public VBox content;

    private String state;

    public void setState(String state) {
        this.state = state;
    }

    public ListPersonController() {

    }

    public void initList() {
        content.getChildren().clear();
        if(state.equals("student")) {
            initListStudent();
        } else {
            initListTeacher();
        }
    }

    public void initList(String key) {
        content.getChildren().clear();
        if(state.equals("student")) {
            initListStudent(key);
        } else {
            initListTeacher(key);
        }
    }


    public void initListStudent() {
        content.getChildren().clear();
        List<Student> students = StudentBll.getInstance().getAllStudents();
        renderListStudents(students);
    }

    public void initListStudent(String key) {
        content.getChildren().clear();
        List<Student> students = new ArrayList<>();
        if(AppUtil.getInstance().isInteger(key)) {
            StudentBll.getInstance().getStudentById(Integer.parseInt(key)).ifPresent(students::add);
        } else {
            students.addAll(StudentBll.getInstance().   getAllStudentsByName(key));
        }
        renderListStudents(students);
    }

    private void renderListStudents(List<Student> students) {
        if(!students.isEmpty()) {
            for (Student student : students) {
                FXMLLoader loader = new FXMLLoader(
                        HomeApplication.class.getResource(Component.PERSON_ITEM.getValue())
                );
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                PersonItemController controller = loader.getController();
                controller.initPerson(student);
                controller.setController(this);
                content.getChildren().add(root);
            }
        } else {
            FXMLLoader loader = new FXMLLoader(
                    HomeApplication.class.getResource(Component.EMPTY.getValue())
            );
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            EmptyController controller = loader.getController();
            controller.setText("Không có học sinh !!!");
            content.getChildren().add(root);
        }
    }

    public void initListTeacher() {
        content.getChildren().clear();
        List<Teacher> teachers = TeacherBll.getInstance().getAllTeacher();
        renderList(teachers);
    }

    private void renderList(List<Teacher> teachers) {
        if(!teachers.isEmpty()) {
            for (Teacher teacher : teachers) {
                FXMLLoader loader = new FXMLLoader(
                        HomeApplication.class.getResource(Component.PERSON_ITEM.getValue()));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                PersonItemController controller = loader.getController();
                controller.initPerson(teacher);
                controller.setController(this);
                content.getChildren().add(root);
            }
        } else {
            FXMLLoader loader = new FXMLLoader(
                    HomeApplication.class.getResource(Component.EMPTY.getValue()));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            EmptyController controller = loader.getController();
            controller.setText("Không có giáo viên !!!");
            content.getChildren().add(root);
        }
    }

    public void initListTeacher(String key) {
        content.getChildren().clear();
        List<Teacher> teachers = new ArrayList<>();
        if(AppUtil.getInstance().isInteger(key)) {
            TeacherBll.getInstance().getTeacherById(Integer.parseInt(key)).ifPresent(teachers::add);
        } else {
            teachers.addAll(TeacherBll.getInstance().getAllTeacherByName(key));
        }
        renderList(teachers);
    }
}
