package com.example.coursemanagement.bll;

import com.example.coursemanagement.bll.dtos.Student;
import com.example.coursemanagement.dal.DepartmentDal;
import com.example.coursemanagement.bll.dtos.Department;
import com.example.coursemanagement.bll.utils.AppUtil;
import com.example.coursemanagement.dal.StudentDal;
import com.example.coursemanagement.gui.utils.DialogUtil;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DepartmentBll {
    private static class DepartmentBllHolder {
        private static final DepartmentBll INSTANCE = new DepartmentBll();
    }

    private DepartmentBll() {
    }

    public static DepartmentBll getInstance() {
        return DepartmentBll.DepartmentBllHolder.INSTANCE;
    }

    public List<Department> getAll() {
        List<Department> departments = DepartmentDal.getInstance().getAll();
        if (departments == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return new ArrayList<>();
        } else {
            return departments;
        }
    }

    public List<Department> getAllByName(String name) {
        List<Department> departments = DepartmentDal.getInstance().getAllByName(name);
        if (departments == null) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return new ArrayList<>();
        } else {
            return departments;
        }
    }

    public Optional<Department> getDepartmentById(Integer id) {
        Optional<Department> department = DepartmentDal.getInstance().getById(id);
        if (department.isEmpty()) {
            DialogUtil.getInstance().showAlert("Lỗi", "Đã xảy ra lỗi", Alert.AlertType.ERROR);
            return Optional.empty();
        } else {
            return department;
        }
    }

    public int createDepartment(String name, String budget, Date startDate, Integer administrator) throws Exception {
        Double budgetDouble = AppUtil.getInstance().validateDouble(budget, "Ngân sách");
        int maxId = DepartmentDal.getInstance().getId();
        if(maxId < 0) {
            throw new Exception();
        }
        if(startDate == null) {
            DialogUtil.getInstance().showAlert(
                    "Lỗi", "Ngày thành lập không được để trống", Alert.AlertType.ERROR);
            throw new Exception();
        }
        Department department = Department.builder()
                .id(maxId + 1)
                .name(name)
                .budget(budgetDouble)
                .startDate(startDate)
                .administratorId(administrator)
                .build();
        return DepartmentDal.getInstance().createDepartment(department);
    }

    public int updateDepartment(
            Integer departmentId, String name, String budget, Date startDate, Integer administrator
    ) throws Exception {
        Double budgetDouble = AppUtil.getInstance().validateDouble(budget, "Ngân sách");
        if(startDate == null) {
            DialogUtil.getInstance().showAlert(
                     "Lỗi", "Ngày thành lập không được để trống", Alert.AlertType.ERROR);
            throw new Exception();
        }
        Department department = Department.builder()
                .id(departmentId)
                .name(name)
                .budget(budgetDouble)
                .startDate(startDate)
                .administratorId(administrator)
                .build();
        return DepartmentDal.getInstance().updateDepartment(department);
    }

    public Optional<Department> getById(Integer id) {
        return DepartmentDal.getInstance().getById(id);
    }

    public int deleteDepartment(Integer id) throws Exception {
        if(DepartmentDal.getInstance().checkDepartment(id)) {
            return DepartmentDal.getInstance().deleteDepartment(id);
        } else {
            DialogUtil.getInstance().showAlert(
                    "Lỗi", "Đã có khóa học trực thuộc khoa, không thể xóa.", Alert.AlertType.ERROR);
            throw new Exception();
        }
    }
}
