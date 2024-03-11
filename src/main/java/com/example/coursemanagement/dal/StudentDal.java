package com.example.coursemanagement.dal;

import com.example.coursemanagement.bll.dtos.Student;
import com.example.coursemanagement.bll.dtos.StudentGrade;
import com.example.coursemanagement.bll.dtos.Teacher;
import com.example.coursemanagement.bll.utils.DbConnection;
import com.example.coursemanagement.bll.utils.mapper.TeacherMapper;

import java.sql.Connection;
import java.sql.Date;
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

    public List<Student> getAll() {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM person WHERE EnrollmentDate IS NOT NULL";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            List<Student> students = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student = Student.builder()
                        .id(resultSet.getInt("PersonID"))
                        .lastName(resultSet.getString("Lastname"))
                        .firstName(resultSet.getString("Firstname"))
                        .enrollmentDate(resultSet.getDate("EnrollmentDate"))
                        .build();
                students.add(student);
            }
            return students;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            DbConnection.getInstance().closeConnection();
        }
    }

    public List<Student> getAllByName(String name) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                SELECT p.PersonID, p.Lastname, p.Firstname, p.EnrollmentDate FROM person p
                WHERE CONCAT(p.Lastname, ' ', p.Firstname) LIKE ?
                AND p.EnrollmentDate IS NOT NULL
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,"%" + name + "%");
            List<Student> students = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student = Student.builder()
                        .id(resultSet.getInt("PersonID"))
                        .lastName(resultSet.getString("Lastname"))
                        .firstName(resultSet.getString("Firstname"))
                        .enrollmentDate(resultSet.getDate("EnrollmentDate"))
                        .build();
                students.add(student);
            }
            return students;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return null;
        } finally {
            DbConnection.getInstance().closeConnection();
        }
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
        } finally {
            DbConnection.getInstance().closeConnection();
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
        } finally {
            DbConnection.getInstance().closeConnection();
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
        } finally {
            DbConnection.getInstance().closeConnection();
        }
    }


    public int addStudent(Student student) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO person (Lastname, Firstname, EnrollmentDate) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, student.getLastName());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setDate(3, (Date) student.getEnrollmentDate());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return 0;
        } finally {
            DbConnection.getInstance().closeConnection();
        }
    }


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
        } finally {
            DbConnection.getInstance().closeConnection();
        }
    }


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
        } finally {
            DbConnection.getInstance().closeConnection();
        }
        return false;
    }


    public int deleteStudent(Integer studentId) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM person WHERE PersonID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return 0;
        } finally {
            DbConnection.getInstance().closeConnection();
        }
    }

}
