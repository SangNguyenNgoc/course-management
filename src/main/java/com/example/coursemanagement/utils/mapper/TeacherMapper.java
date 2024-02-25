package com.example.coursemanagement.mapper;

import com.example.coursemanagement.dtos.Teacher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

public class TeacherMapper {
    private static class TeacherMapperHolder{
        private static final TeacherMapper INSTANCE = new TeacherMapper();
    }

    private TeacherMapper() {

    }
    public static TeacherMapper getInstance() {
        return TeacherMapper.TeacherMapperHolder.INSTANCE;
    }

    public Teacher initTeacher(ResultSet resultSet) throws SQLException{
        return Teacher.builder()
                .id(resultSet.getInt("PersonID"))
                .firstName(resultSet.getString("Firstname"))
                .lastName(resultSet.getString("Lastname"))
                .hireDate(resultSet.getDate("HireDate").toLocalDate().atStartOfDay())
                .build();
    }
}
