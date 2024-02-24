package com.example.coursemanagement.bll;

import com.example.coursemanagement.bll.interfaces.IDepartmentBll;
import com.example.coursemanagement.dal.DepartmentDal;
import com.example.coursemanagement.dtos.Department;
import com.example.coursemanagement.utils.DialogUtil;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class DepartmentBll implements IDepartmentBll {
    private static class DepartmentBllHolder {
        private static final DepartmentBll INSTANCE = new DepartmentBll();
    }

    private DepartmentBll() {
    }

    public static DepartmentBll getInstance() {
        return DepartmentBll.DepartmentBllHolder.INSTANCE;
    }
    @Override
    public List<Department> getAllDepartment() {
        List<Department> departments = DepartmentDal.getInstance().getAll();
        if (departments == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return new ArrayList<>();
        } else {
            return departments;
        }
    }
}
