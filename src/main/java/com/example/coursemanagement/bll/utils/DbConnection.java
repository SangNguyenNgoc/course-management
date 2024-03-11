package com.example.coursemanagement.bll.utils;

import com.example.coursemanagement.gui.utils.DialogUtil;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/school";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private Connection c;

    private static class DbConnectionHolder{
        private static final DbConnection INSTANCE = new DbConnection();
    }

    private DbConnection() {
    }

    public static DbConnection getInstance() {
        return DbConnectionHolder.INSTANCE;
    }

    public Connection getConnection() {
        try {
            if(c != null && !c.isClosed()) {
                return c;
            }
            c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Logger.getLogger(DbConnection.class.getName()).log(Level.INFO, "Connection successfully");
        } catch (Exception e) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, "Connection failure", e.getMessage());
            DialogUtil.getInstance().showAlert("Lỗi","Lỗi kết nối cơ sở dữ liệu", Alert.AlertType.ERROR);
        }
        return c;
    }

    public void closeConnection() {
        try {
            if (c != null) {
                c.close();
                Logger.getLogger(DbConnection.class.getName()).log(Level.INFO, "Close connection successfully");            }
        } catch (Exception e) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, "Close connection failure", e);
        }
    }

    public void printInfo(java.sql.Connection c) {
        try {
            if (c != null) {
                DatabaseMetaData metaData = c.getMetaData();
                System.out.println("Database Product Name: " + metaData.getDatabaseProductName());
                System.out.println("Database Version: " + metaData.getDatabaseProductVersion());
                System.out.println("Driver Name: " + metaData.getDriverName());
                System.out.println("Driver Version: " + metaData.getDriverVersion());
            }
        } catch (Exception e) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
