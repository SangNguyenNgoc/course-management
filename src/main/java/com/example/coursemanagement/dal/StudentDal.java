package com.example.coursemanagement.dal;

import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.dtos.StudentGrade;
import com.example.coursemanagement.utils.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDal {

    private static final Logger logger = Logger.getLogger(CourseDal.class.getName());

    private static class StudentDalHolder {
        private static final StudentDal INSTANCE = new StudentDal();
    }

    private StudentDal() {
    }

    public static StudentDal getInstance() {
        return StudentDalHolder.INSTANCE;
    }

    public Optional<Student> getById(Integer studentId) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM person WHERE PersonID = ? AND EnrollmentDate IS NOT NULL";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            Student student = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                student = Student.builder()
                        .id(resultSet.getInt("PersonID"))
                        .lastName(resultSet.getString("Lastname"))
                        .firstName(resultSet.getString("Firstname"))
                        .enrollmentDate(resultSet.getDate("EnrollmentDate"))
                        .build();
            }
            return Optional.ofNullable(student);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<StudentGrade> getStudentsInCourse(Integer courseId) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                SELECT p.PersonID ,p.Firstname, p.Lastname, p.EnrollmentDate, d.Grade
                FROM  person p
                JOIN studentgrade d
                ON p.PersonID = d.StudentID
                WHERE d.CourseID = %s
                """.formatted(courseId);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            List<StudentGrade> studentGrades = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                StudentGrade studentGrade = null;
                Double grade = resultSet.getDouble("Grade");
                if (resultSet.wasNull()) {
                    grade = null;
                }
                studentGrade = StudentGrade.builder()
                        .id(resultSet.getInt("PersonID"))
                        .firstName(resultSet.getString("Firstname"))
                        .lastName(resultSet.getString("Lastname"))
                        .enrollmentDate(resultSet.getDate("EnrollmentDate"))
                        .grade(grade)
                        .build();
                studentGrades.add(studentGrade);
            }
            return studentGrades;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return null;
        }
    }

    public int updateGrade(Integer personId, Integer courseId, Double grade) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE studentgrade s SET s.Grade = ? WHERE s.StudentID = ? AND s.CourseID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, grade);
            preparedStatement.setInt(2, personId);
            preparedStatement.setInt(3, courseId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return 0;
        }
    }

    public int deleteGrade(Integer courseId, Integer studentId) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM studentgrade WHERE StudentID = ? AND CourseID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return 0;
        }
    }
}
