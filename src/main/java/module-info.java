module com.example.coursemanagement {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.coursemanagement.bll.dtos to javafx.base;
    opens com.example.coursemanagement to javafx.fxml;
    exports com.example.coursemanagement;
}