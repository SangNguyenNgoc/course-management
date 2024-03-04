package com.example.coursemanagement.gui.controller;

import com.example.coursemanagement.bll.CourseBll;
import com.example.coursemanagement.bll.DepartmentBll;
import com.example.coursemanagement.bll.TeacherBll;
import com.example.coursemanagement.bll.dtos.Course;
import com.example.coursemanagement.bll.dtos.Department;
import com.example.coursemanagement.bll.dtos.OnsiteCourse;
import com.example.coursemanagement.bll.dtos.Teacher;
import com.example.coursemanagement.gui.utils.DialogUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OnsiteCourseFormController implements Initializable {

    public CheckBox mon;
    public CheckBox thu;
    public CheckBox fri;
    public CheckBox sat;
    public CheckBox wed;
    public CheckBox tue;
    @FXML
    private Button onsiteSubmitButton;
    @FXML
    private TextField onsiteNameInput;
    @FXML
    private TextField onsiteCreditInput;
    @FXML
    private ComboBox<String> onsiteTeacherInput;
    @FXML
    private TextField onsiteLocationInput;
    @FXML
    private ComboBox<String> onsiteDepartmentInput;
    @FXML
    private TextField onsiteTimeInput;

    private List<Teacher> teachers;

    private List<Department> departments;

    private OnsiteCourse onsiteCourse;

    private DashboardController dashboardController;

    public OnsiteCourseFormController() {
    }

    public OnsiteCourseFormController(OnsiteCourse onsiteCourse, DashboardController dashboardController) {
        this.onsiteCourse = onsiteCourse;
        this.dashboardController = dashboardController;
    }

    private void setDataTeacherInput() {
        if (!teachers.isEmpty())
            teachers.forEach(item -> onsiteTeacherInput.getItems().add(item.getLastName() + " " + item.getFirstName()));
    }

    private void setDataDepartmentInput() {
        if (!departments.isEmpty())
            departments.forEach(item -> onsiteDepartmentInput.getItems().add(item.getName()));
    }

    private void setEventSubmitBtn() {
        onsiteSubmitButton.setOnMouseClicked(event -> {
            if (checkCombobox()) {
                try {
                    Optional<Course> newCourse = CourseBll.getInstance().createCourse(
                            onsiteNameInput.getText(),
                            onsiteCreditInput.getText(),
                            departments.get(onsiteDepartmentInput.getSelectionModel().getSelectedIndex()).getId(),
                            onsiteLocationInput.getText(),
                            createDay(),
                            onsiteTimeInput.getText(),
                            teachers.get(onsiteTeacherInput.getSelectionModel().getSelectedIndex()).getId()
                    );
                    if (newCourse.isPresent()) {
                        DialogUtil.getInstance().showAlert(
                                "Thành công", "Đã thêm thành công", Alert.AlertType.CONFIRMATION);
                        clearInput();
                    } else {
                        DialogUtil.getInstance().showAlert(
                                "Lỗi", "Lỗi không xác định", Alert.AlertType.ERROR);
                    }
                } catch (Exception e) {
                    DialogUtil.getInstance().showAlert(
                            "Lỗi", "Thêm không thành công.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void clearInput() {
        onsiteCreditInput.setText("");
        onsiteDepartmentInput.setValue(null);
        onsiteNameInput.setText("");
        onsiteLocationInput.setText("");
        onsiteTeacherInput.setValue(null);
        onsiteTimeInput.setText("");
        createCheckbox("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teachers = TeacherBll.getInstance().getAllTeacher();
        departments = DepartmentBll.getInstance().getAll();
        setDataDepartmentInput();
        setDataTeacherInput();
        if (onsiteCourse != null) {
            initData();
        } else {
            setEventSubmitBtn();
        }
    }

    private void initData() {
        String formattedTime = onsiteCourse.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        onsiteCreditInput.setText(onsiteCourse.getCredits() + "");
        onsiteDepartmentInput.setValue(null);
        onsiteNameInput.setText(onsiteCourse.getTitle());
        onsiteLocationInput.setText(onsiteCourse.getLocation());
        onsiteTeacherInput.setValue(null);
        onsiteTimeInput.setText(formattedTime);
        createCheckbox(onsiteCourse.getDays());
        onsiteSubmitButton.setText("Cập nhật");
        onsiteDepartmentInput.getSelectionModel().select(onsiteCourse.getDepartment());
        onsiteTeacherInput.getSelectionModel().select(onsiteCourse.getTeacher());
        onsiteSubmitButton.setOnMouseClicked(event -> {
            try {
                CourseBll.getInstance().updateCourse(
                        onsiteCourse.getId(),
                        onsiteNameInput.getText(),
                        onsiteCreditInput.getText(),
                        departments.get(onsiteDepartmentInput.getSelectionModel().getSelectedIndex()).getId(),
                        onsiteLocationInput.getText(),
                        createDay(),
                        onsiteTimeInput.getText(),
                        teachers.get(onsiteTeacherInput.getSelectionModel().getSelectedIndex()).getId()
                );
                DialogUtil.getInstance().showAlert(
                        "Thông báo", "Cập nhật thành công.", Alert.AlertType.INFORMATION);
                dashboardController.initListCourses(dashboardController.getStage());
            } catch (Exception e) {
                DialogUtil.getInstance().showAlert(
                        "Lỗi", "Cập nhật không thành công.", Alert.AlertType.ERROR);
            }
        });
    }

    private boolean checkCombobox() {
        if (onsiteDepartmentInput.getSelectionModel().getSelectedIndex() == -1) {
            DialogUtil.getInstance().showAlert("Cảnh báo", "Chưa chọn khoa ", Alert.AlertType.WARNING);
            return false;
        } else {
            if (onsiteTeacherInput.getSelectionModel().getSelectedIndex() == -1) {
                DialogUtil.getInstance().showAlert("Cảnh báo", "Chưa chọn giáo viên ", Alert.AlertType.WARNING);
                return false;
            }
        }
        return true;
    }

    public String createDay() {
        String result = "";
        if(mon.isSelected()) {
            result += "M";
        }
        if(tue.isSelected()) {
            result += "T";
        }
        if(wed.isSelected()) {
            result += "W";
        }
        if(thu.isSelected()) {
            result += "H";
        }
        if(fri.isSelected()) {
            result += "F";
        }
        if(sat.isSelected()) {
            result += "S";
        }
        return result;
    }

    public void createCheckbox(String days) {
        mon.setSelected(days.contains("M"));
        tue.setSelected(days.contains("T"));
        wed.setSelected(days.contains("W"));
        thu.setSelected(days.contains("H"));
        fri.setSelected(days.contains("F"));
        sat.setSelected(days.contains("S"));
    }
}
