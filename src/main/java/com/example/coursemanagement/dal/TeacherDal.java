package com.example.coursemanagement.dal;
import java.sql.*;

import com.example.coursemanagement.dal.interfaces.ITeacherDal;
import com.example.coursemanagement.dtos.Teacher;
import com.example.coursemanagement.mapper.TeacherMapper;
import com.example.coursemanagement.utils.DbConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TeacherDal implements ITeacherDal {
    private static class TeacherDalHolder{
        private static final TeacherDal INSTANCE = new TeacherDal();
    }

    private TeacherDal(){}

    public static TeacherDal getInstance() {
        return TeacherDal.TeacherDalHolder.INSTANCE;
    }

    private static final Logger logger = Logger.getLogger(CourseDal.class.getName());
    @Override
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
    public void addTeacher(Teacher teacher) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
            INSERT INTO person (Lastname, Firstname, HireDate)
            VALUES (?, ?, ?)
            """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set parameters for the prepared statement
            preparedStatement.setString(1, teacher.getLastName());
            preparedStatement.setString(2, teacher.getFirstName());
            preparedStatement.setDate(3, Date.valueOf(teacher.getHireDate().toLocalDate())); // Assuming HireDate is of type java.util.Date

            // Execute the statement
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Insertion failure: " + e.getMessage());
        }
    }
    public void updateTeacher(Teacher teacher) {
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
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Update failure: " + e.getMessage());
        }
    }
    public boolean checkTeacher(int personID) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT PersonID FROM courseinstructor WHERE PersonID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the parameter for the prepared statement
            preparedStatement.setInt(1, personID); // Assuming PersonID is the unique identifier for a teacher
            ResultSet resultSet = preparedStatement.executeQuery();
            return !resultSet.next();
        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        return false;
    }
    public void deleteTeacher(int personID) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM person WHERE PersonID = ?";
        if(!checkTeacher(personID)) return;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the parameter for the prepared statement
            preparedStatement.setInt(1, personID); // Assuming PersonID is the unique identifier for a teacher

            // Execute the statement
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Deletion failure: " + e.getMessage());
        }
    }

}
