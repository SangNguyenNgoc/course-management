package com.example.coursemanagement.dal.interfaces;

import com.example.coursemanagement.dtos.Teacher;

import java.util.List;

public interface ITeacherDal {
    List<Teacher> getAll();
}
