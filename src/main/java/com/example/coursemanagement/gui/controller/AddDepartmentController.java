package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.bll.DepartmentBll;
import com.example.coursemanagement.bll.TeacherBll;
import com.example.coursemanagement.dal.DepartmentDal;
import com.example.coursemanagement.dtos.Department;
import com.example.coursemanagement.dtos.Teacher;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AddDepartmentController<T> implements Initializable {
    public DatePicker dateField;
    public TextField budgetField;
    public ComboBox<String> teacherField;
    public TextField nameField;
    public Button submitBtn;

    public Label title;

    private Department department;

    private T controller;

    List<Teacher> teachers;

    public void initDepartment() {
        initialize(null, null);
        title.setText("Chỉnh sửa khoa");
        teacherField.getSelectionModel().select(department.getAdministrator());
        nameField.setText(department.getName());
        budgetField.setText(department.getBudget().toString());
        dateField.setValue(convertToLocalDate((java.sql.Date) department.getStartDate()));
        submitBtn.setText("Cập nhật");
    }

    public void setController(T controller) {
        this.controller = controller;
    }

    public void setDepartment(Department department) {
        this.department = department;
        initDepartment();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teachers = TeacherBll.getInstance().getAllTeacher();
        teachers.forEach(item -> teacherField.getItems().add(item.getLastName() + " " + item.getFirstName()));
    }

    public void onAction() {
        if(department == null) {
            try {
                int result = DepartmentBll.getInstance().createDepartment(
                        nameField.getText(),
                        budgetField.getText(),
                        java.sql.Date.valueOf(dateField.getValue()),
                        teachers.get(teacherField.getSelectionModel().getSelectedIndex()).getId()
                );
                if(result != 0) {
                    DialogUtil.getInstance().showAlert("Thông báo", "Tạo thành công", Alert.AlertType.INFORMATION);
                    clearInput();
                    DashboardController c = (DashboardController) controller;
                    c.initListDepartment(null);
                } else {
                    DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi, vui lòng thử lại", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                DialogUtil.getInstance().showAlert("Lỗi", "Tạo không thành công", Alert.AlertType.ERROR);
            }
        } else {
            try {
                int result = DepartmentBll.getInstance().updateDepartment(
                        department.getId(),
                        nameField.getText(),
                        budgetField.getText(),
                        java.sql.Date.valueOf(dateField.getValue()),
                        teachers.get(teacherField.getSelectionModel().getSelectedIndex()).getId()
                );
                if(result != 0) {
                    DialogUtil.getInstance().showAlert("Thông báo", "Cập nhật thành công", Alert.AlertType.INFORMATION);
                    DepartmentItemController c = (DepartmentItemController) controller;
                    c.initDepartment(department.getId());
                } else {
                    DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi, vui lòng thử lại", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                DialogUtil.getInstance().showAlert("Lỗi", "Cập nhật không thành công", Alert.AlertType.ERROR);
            }
        }

    }

    public LocalDate convertToLocalDate(java.sql.Date sqlDate) {
        if (sqlDate == null) {
            return null;
        }
        return sqlDate.toLocalDate();
    }

    public void clearInput() {
        nameField.setText("");
        dateField.setValue(null);
        teacherField.getSelectionModel().select(0);
        budgetField.setText("");
    }
}
