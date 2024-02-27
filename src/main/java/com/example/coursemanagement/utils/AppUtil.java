package com.example.coursemanagement.utils;

import javafx.scene.control.Alert;

import java.text.SimpleDateFormat;
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

}
