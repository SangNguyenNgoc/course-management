package com.example.coursemanagement.dal;

import com.example.coursemanagement.dal.interfaces.IDepartmentDal;
import com.example.coursemanagement.dtos.Department;
import com.example.coursemanagement.dtos.Teacher;
import com.example.coursemanagement.mapper.TeacherMapper;
import com.example.coursemanagement.utils.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepartmentDal implements IDepartmentDal {

    private static final Logger logger = Logger.getLogger(DepartmentDal.class.getName());


    private static class DepartmentDalHolder {
        private static final DepartmentDal INSTANCE = new DepartmentDal();
    }

    private DepartmentDal() {
    }

    public static DepartmentDal getInstance() {
        return DepartmentDal.DepartmentDalHolder.INSTANCE;
    }

    @Override
    public List<Department> getAll() {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                SELECT d.*, p.Lastname,p.Firstname
                FROM department d
                JOIN person p ON d.Administrator = p.PersonID
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            List<Department> departments = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Department department = Department.builder()
                        .id(resultSet.getInt("DepartmentID"))
                        .name(resultSet.getString("Name"))
                        .budget(resultSet.getDouble("Budget"))
                        .startDate(resultSet.getDate("StartDate"))
                        .administrator(resultSet.getString("Firstname") + " " + resultSet.getString("Lastname"))
                        .administratorId(resultSet.getInt("Administrator"))
                        .build();
                departments.add(department);
            }
            return departments;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return null;
        }
    }
}
