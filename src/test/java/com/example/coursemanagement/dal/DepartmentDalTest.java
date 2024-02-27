package com.example.coursemanagement.dal;

import com.example.coursemanagement.dtos.Department;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentDalTest {

    @Test
    void getAll() {
        List<Department> departments = DepartmentDal.getInstance().getAll();
        assertNotEquals(0,departments.size());
        departments.forEach(item -> {
            System.out.println(item.getStartDate().toString());
        });
    }

    @Test
    void getId() {
        int a = DepartmentDal.getInstance().getId();
        System.out.println(a);
    }
}