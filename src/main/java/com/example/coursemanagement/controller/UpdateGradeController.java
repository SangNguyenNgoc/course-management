package com.example.coursemanagement.controller;

import com.example.coursemanagement.bll.StudentBll;
import com.example.coursemanagement.dtos.StudentGrace;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateGradeController {
    public TextField inputGrade;
    private StudentGrace studentGrace;
    private Stage stage;

    private CourseDetailController controller;

    private Integer courseId;

    public void setStudentGrace(StudentGrace studentGrace) {
        this.studentGrace = studentGrace;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public void setController(CourseDetailController controller) {
        this.controller = controller;
    }

    public void updateGrade() {
        int result = StudentBll.getInstance().updateGrade(studentGrace.getId(), courseId, inputGrade.getText());
        if(result == 0) {
            DialogUtil.getInstance().showAlert("Lỗi", "Thay đổi điểm không thành công", Alert.AlertType.ERROR);
        } else {
            DialogUtil.getInstance().showAlert("Thông báo", "Thay đổi điểm thành công", Alert.AlertType.INFORMATION);
            this.stage.close();
            try {
                controller.initCourseDetail(courseId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
