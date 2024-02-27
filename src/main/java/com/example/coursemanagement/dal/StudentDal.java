package com.example.coursemanagement.dal;

import com.example.coursemanagement.dal.interfaces.IStudentDal;
import com.example.coursemanagement.dtos.Student;
import com.example.coursemanagement.dtos.StudentGrace;
import com.example.coursemanagement.utils.DbConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDal implements IStudentDal {

    private static final Logger logger = Logger.getLogger(CourseDal.class.getName());

    private static class StudentDalHolder{
        private static final StudentDal INSTANCE = new StudentDal();
    }

    private StudentDal(){}

    public static StudentDal getInstance() {
        return StudentDalHolder.INSTANCE;
    }

    @Override
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

    @Override
    public List<StudentGrace> getStudentsInCourse(Integer courseId) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                SELECT p.PersonID ,p.Firstname, p.Lastname, p.EnrollmentDate, d.Grade
                FROM  person p
                JOIN studentgrade d
                ON p.PersonID = d.StudentID
                WHERE d.CourseID = %s
                """.formatted(courseId);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            List<StudentGrace> studentGraces = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                StudentGrace studentGrace = null;
                Double grade = resultSet.getDouble("Grade");
                if(resultSet.wasNull()) {
                    grade = null;
                }
                studentGrace = StudentGrace.builder()
                        .id(resultSet.getInt("PersonID"))
                        .firstName(resultSet.getString("Firstname"))
                        .lastName(resultSet.getString("Lastname"))
                        .enrollmentDate(resultSet.getDate("EnrollmentDate"))
                        .grade(grade)
                        .build();
                studentGraces.add(studentGrace);
            }
            return studentGraces;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return null;
        }
    }

    @Override
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

    @Override
    public int addStudent(Student student) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO person (PersonID, Lastname, Firstname, EnrollmentDate) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, student.getId());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getFirstName());
            preparedStatement.setDate(4, (Date) student.getEnrollmentDate());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public int updateStudent(Student student) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE person SET Lastname = ?, Firstname = ?, EnrollmentDate = ? WHERE PersonID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, student.getLastName());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setDate(3, (Date) student.getEnrollmentDate());
            preparedStatement.setInt(4, student.getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean isStudentInGradeTable(Integer studentId) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) AS count FROM studentgrade WHERE StudentID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
        }
        return false;
    }

    @Override
    public int deleteStudent(Integer studentId) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM person WHERE PersonID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return 0;
        }
    }

}
