package com.example.coursemanagement.dal;
import java.sql.*;

import com.example.coursemanagement.bll.dtos.Teacher;
import com.example.coursemanagement.bll.utils.mapper.TeacherMapper;
import com.example.coursemanagement.bll.utils.DbConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TeacherDal {
    private static class TeacherDalHolder {
        private static final TeacherDal INSTANCE = new TeacherDal();
    }

    private TeacherDal() {
    }

    public static TeacherDal getInstance() {
        return TeacherDal.TeacherDalHolder.INSTANCE;
    }

    private static final Logger logger = Logger.getLogger(CourseDal.class.getName());

    public List<Teacher> getAll() {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                SELECT p.PersonID, p.Lastname, p.Firstname, p.HireDate
                FROM person p
                Where p.EnrollmentDate IS NULL
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            List<Teacher> teachers = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Teacher teacher = null;
                teacher = TeacherMapper.getInstance().initTeacher(resultSet);
                teachers.add(teacher);
            }
            return teachers;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return null;
        }
    }

    public List<Teacher> getAllByName(String name) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                SELECT p.PersonID, p.Lastname, p.Firstname, p.HireDate FROM person p
                WHERE CONCAT(p.Lastname, ' ', p.Firstname) LIKE ?
                AND p.EnrollmentDate IS NULL
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + name + "%");
            List<Teacher> teachers = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Teacher teacher = null;
                teacher = TeacherMapper.getInstance().initTeacher(resultSet);
                teachers.add(teacher);
            }
            return teachers;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return null;
        }
    }

    public Optional<Teacher> getById(Integer teacherId) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM person WHERE PersonID = ? AND person.HireDate IS NOT NULL";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, teacherId);
            Teacher teacher = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                teacher = Teacher.builder()
                        .id(resultSet.getInt("PersonID"))
                        .lastName(resultSet.getString("Lastname"))
                        .firstName(resultSet.getString("Firstname"))
                        .hireDate(resultSet.getDate("HireDate").toLocalDate().atStartOfDay())
                        .build();
            }
            return Optional.ofNullable(teacher);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return Optional.empty();
        }
    }
    public int addTeacher(Teacher teacher) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
            INSERT INTO person (Lastname, Firstname, HireDate)
            VALUES (?, ?, ?)
            """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set parameters for the prepared statement
            preparedStatement.setString(1, teacher.getLastName());
            preparedStatement.setString(2, teacher.getFirstName());
            preparedStatement.setDate(3, Date.valueOf(teacher.getHireDate().toLocalDate()));

            // Execute the statement
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Insertion failure: " + e.getMessage());
            return 0;
        }
    }
    public int updateTeacher(Teacher teacher) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
            UPDATE person
            SET Lastname = ?, Firstname = ?, HireDate = ?
            WHERE PersonID = ?
            """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set parameters for the prepared statement
            preparedStatement.setString(1, teacher.getLastName());
            preparedStatement.setString(2, teacher.getFirstName());
            preparedStatement.setDate(3, Date.valueOf(teacher.getHireDate().toLocalDate())); // Assuming HireDate is of type java.util.Date
            preparedStatement.setInt(4, teacher.getId()); // Assuming PersonID is the unique identifier for a teacher

            // Execute the statement
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Update failure: " + e.getMessage());
            return 0;
        }
    }
    public boolean checkTeacher(int personID) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT PersonID FROM courseinstructor WHERE PersonID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the parameter for the prepared statement
            preparedStatement.setInt(1, personID); // Assuming PersonID is the unique identifier for a teacher
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        return true;
    }
    public int deleteTeacher(int personID) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM person WHERE PersonID = ?";
        if(checkTeacher(personID)) return 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the parameter for the prepared statement
            preparedStatement.setInt(1, personID); // Assuming PersonID is the unique identifier for a teacher

            // Execute the statement
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Deletion failure: " + e.getMessage());
            return 0;
        }
    }

}
