package com.example.coursemanagement.dal;

import com.example.coursemanagement.bll.dtos.Department;
import com.example.coursemanagement.bll.utils.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepartmentDal {

    private static final Logger logger = Logger.getLogger(DepartmentDal.class.getName());


    private static class DepartmentDalHolder {
        private static final DepartmentDal INSTANCE = new DepartmentDal();
    }

    private DepartmentDal() {
    }

    public static DepartmentDal getInstance() {
        return DepartmentDal.DepartmentDalHolder.INSTANCE;
    }

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
                        .administrator(
                                resultSet.getString("Lastname") + " " +
                                        resultSet.getString("Firstname"))
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

    public Optional<Department> getById(Integer id) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                SELECT d.*, p.Lastname,p.Firstname
                FROM department d
                JOIN person p ON d.Administrator = p.PersonID
                WHERE DepartmentID = ?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Department department = null;
            while (resultSet.next()) {
                department = Department.builder()
                        .id(resultSet.getInt("DepartmentID"))
                        .name(resultSet.getString("Name"))
                        .budget(resultSet.getDouble("Budget"))
                        .startDate(resultSet.getDate("StartDate"))
                        .administrator(
                                resultSet.getString("Lastname") + " " +
                                        resultSet.getString("Firstname"))
                        .administratorId(resultSet.getInt("Administrator"))
                        .build();
            }
            return Optional.ofNullable(department);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return Optional.empty();
        }
    }

    public int createDepartment(Department department) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                INSERT INTO department (DepartmentID, Name, Budget, StartDate, Administrator)
                VALUE (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, department.getId());
            preparedStatement.setString(2, department.getName());
            preparedStatement.setDouble(3, department.getBudget());
            preparedStatement.setDate(4, (Date) department.getStartDate());
            preparedStatement.setInt(5, department.getAdministratorId());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return 0;
        }
    }

    public int getId() {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                SELECT MAX(d.DepartmentID) as id FROM department d;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt("id");
            }
            return -1;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return -1;
        }
    }

    public int updateDepartment(Department department) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                UPDATE department SET
                Name = ?,
                Budget = ?,
                StartDate = ?,
                Administrator =?
                WHERE DepartmentID = ?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, department.getName());
            preparedStatement.setDouble(2, department.getBudget());
            preparedStatement.setDate(3, (Date) department.getStartDate());
            preparedStatement.setInt(4, department.getAdministratorId());
            preparedStatement.setInt(5, department.getId());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return 0;
        }
    }

    public int deleteDepartment(Integer id) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                DELETE  FROM department WHERE DepartmentID = ?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return 0;
        }
    }

    public boolean checkDepartment(Integer id) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                SELECT c.DepartmentID FROM course c WHERE DepartmentID = ?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return !resultSet.next();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return false;
        }
    }
}
