package com.example.coursemanagement.utils;

import javafx.scene.control.Alert;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AppUtil {
    private static class AppUtilHolder{
        private static final AppUtil INSTANCE = new AppUtil();
    }

    private AppUtil() {

    }

    public static AppUtil getInstance() {
        return AppUtil.AppUtilHolder.INSTANCE;
    }

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public Integer validateInteger(String str, String message) throws Exception {
        try {
            return Integer.parseInt(str);
        } catch(NumberFormatException e){
            DialogUtil.getInstance().showAlert("Lỗi",message + " phải là một số nguyên.", Alert.AlertType.ERROR);
            throw new Exception();
        }
    }

    public Double validateDouble(String str, String message) throws Exception {
        try {
            return Double.parseDouble(str);
        } catch(NumberFormatException e){
            DialogUtil.getInstance().showAlert("Lỗi",message + " phải là một số thực.", Alert.AlertType.ERROR);
            throw new Exception();
        }
    }

    public boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public boolean isValidDateFormat(String dateStr, String formatStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatStr);
        try {
            formatter.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
