package com.example.coursemanagement.dal.interfaces;

import com.example.coursemanagement.dtos.Department;

import java.util.List;

public interface IDepartmentDal {
    List<Department> getAll();
}
