package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.bll.StudentBll;
import com.example.coursemanagement.bll.TeacherBll;
import com.example.coursemanagement.bll.dtos.Student;
import com.example.coursemanagement.bll.dtos.Teacher;
import com.example.coursemanagement.gui.utils.DialogUtil;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class AddPersonController<T> {
    public TextField firstName;

    public Label title;
    public DatePicker dateField;
    public TextField lastName;
    public Button submitBtn;

    private String state;

    private T controller;

    private Student student;

    private Teacher teacher;

    public void setState(String state) {
        this.state = state;
        initView();
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setController(T controller) {
        this.controller = controller;
    }

    public void initView() {
        switch (state) {
            case "addStudent": {
                title.setText("Thêm sinh viên");
                break;
            }

            case "addTeacher": {
                title.setText("Thêm giảng viên");
                break;
            }

            case "updateStudent": {
                submitBtn.setText("Cập nhật");
                title.setText("Chỉnh sửa sinh viên");
                firstName.setText(student.getFirstName());
                lastName.setText(student.getLastName());
                dateField.setValue(convertToLocalDate((java.sql.Date) student.getEnrollmentDate()));
                break;
            }

            case "updateTeacher": {
                submitBtn.setText("Cập nhật");
                title.setText("Chỉnh sửa giảng viên");
                firstName.setText(teacher.getFirstName());
                lastName.setText(teacher.getLastName());
                dateField.setValue(teacher.getHireDate().toLocalDate());
                break;
            }
        }
    }

    public void initStudent(Student student) {
        setState("updateStudent");
    }

    public void initTeacher(Teacher teacher) {
        setState("updateTeacher");
    }

    public void onAction() {
        switch (state) {
            case "addStudent": {
                int result = StudentBll.getInstance().addStudent(
                        firstName.getText(),
                        lastName.getText(),
                        dateField.getValue()
                );
                if (result != 0) {
                    DialogUtil.getInstance().showAlert(
                            "Thông báo",
                            "Thêm thành công",
                            Alert.AlertType.INFORMATION);
                    DashboardController c = (DashboardController) controller;
                    try {
                        c.initListStudent(null);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    DialogUtil.getInstance().showAlert(
                            "Lỗi",
                            "Thêm không thành công",
                            Alert.AlertType.ERROR);
                }
                break;
            }

            case "addTeacher": {
                int result = TeacherBll.getInstance().addTeacher(
                        firstName.getText(),
                        lastName.getText(),
                        dateField.getValue()
                );
                if (result != 0) {
                    DialogUtil.getInstance().showAlert(
                            "Thông báo", "Thêm thành công", Alert.AlertType.INFORMATION);
                    DashboardController c = (DashboardController) controller;
                    try {
                        c.initListTeacher(null);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    DialogUtil.getInstance().showAlert(
                            "Lỗi", "Thêm không thành công", Alert.AlertType.ERROR);
                }
                break;
            }

            case "updateStudent": {
                int result = StudentBll.getInstance().updateStudent(
                        student.getId(),
                        firstName.getText(),
                        lastName.getText(),
                        dateField.getValue()
                );
                if (result != 0) {
                    DialogUtil.getInstance().showAlert(
                            "Thông báo",
                            "Chỉnh sửa thành công",
                            Alert.AlertType.INFORMATION);
                    PersonItemController c = (PersonItemController) controller;
                    c.initPerson(Student.builder()
                            .id(student.getId())
                            .firstName(firstName.getText())
                            .lastName(lastName.getText())
                            .enrollmentDate(convertToUtilDate(dateField.getValue()))
                            .build());
                } else {
                    DialogUtil.getInstance().showAlert(
                            "Lỗi",
                            "Chỉnh sửa không thành công",
                            Alert.AlertType.ERROR);
                }
                break;
            }

            case "updateTeacher": {
                int result = TeacherBll.getInstance().updateTeacher(
                        teacher.getId(),
                        firstName.getText(),
                        lastName.getText(),
                        dateField.getValue()
                );
                if (result != 0) {
                    DialogUtil.getInstance().showAlert(
                            "Thông báo",
                            "Chỉnh sửa thành công", Alert.AlertType.INFORMATION);
                    PersonItemController c = (PersonItemController) controller;
                    c.initPerson(Teacher.builder()
                            .id(teacher.getId())
                            .firstName(firstName.getText())
                            .lastName(lastName.getText())
                            .hireDate(LocalDateTime.of(dateField.getValue(), LocalTime.now()))
                            .build());
                } else {
                    DialogUtil.getInstance().showAlert(
                            "Lỗi",
                            "Chỉnh sửa không thành công", Alert.AlertType.ERROR);
                }
                break;
            }
        }
    }

    public LocalDate convertToLocalDate(java.sql.Date sqlDate) {
        if (sqlDate == null) {
            return null;
        }
        return sqlDate.toLocalDate();
    }
    public static Date convertToUtilDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return java.sql.Date.valueOf(localDate);
    }

}
