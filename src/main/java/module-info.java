module com.example.coursemanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.coursemanagement.dtos to javafx.base;
    opens com.example.coursemanagement to javafx.fxml;
    exports com.example.coursemanagement;
    exports com.example.coursemanagement.gui.controller;
    exports com.example.coursemanagement.gui.page;
    opens com.example.coursemanagement.gui.controller to javafx.fxml;
}
///