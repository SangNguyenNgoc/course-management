module com.example.coursemanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.coursemanagement to javafx.fxml;
    exports com.example.coursemanagement;
    exports com.example.coursemanagement.controller;
    exports com.example.coursemanagement.page;
    opens com.example.coursemanagement.controller to javafx.fxml;
}