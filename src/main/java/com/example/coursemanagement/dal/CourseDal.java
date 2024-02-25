package com.example.coursemanagement.dal;

import com.example.coursemanagement.dal.interfaces.ICourseDal;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.utils.mapper.CourseMapper;
import com.example.coursemanagement.utils.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseDal implements ICourseDal {
    private final java.sql.Connection connection = DbConnection.getInstance().getConnection();

    private static class CourseDalHolder{
        private static final CourseDal INSTANCE = new CourseDal();
    }

    private CourseDal(){}

    public static CourseDal getInstance() {
        return CourseDalHolder.INSTANCE;
    }

    private static final Logger logger = Logger.getLogger(CourseDal.class.getName());

    @Override
    public List<Course> getAll() {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                SELECT c.*, d.Name, c1.PersonID, p.Firstname, p.Lastname, o.url, o1.Location, Days, Time, COUNT(s.StudentID) as sumOfStudents
                FROM course c
                LEFT JOIN department d
                ON c.DepartmentID = d.DepartmentID
                LEFT JOIN courseinstructor c1
                ON c.CourseID = c1.CourseID
                LEFT JOIN person p
                ON c1.PersonID = p.PersonID
                LEFT JOIN onlinecourse o
                ON c.CourseID = o.CourseID
                LEFT JOIN onsitecourse o1
                ON c.CourseID = o1.CourseID
                LEFT JOIN school.studentgrade s
                ON c.CourseID = s.CourseID GROUP BY s.CourseID
                ORDER BY c.CourseID
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            List<Course> courses = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Course course = null;
                    String url = resultSet.getString("url");
                    if(url != null) {
                        course = CourseMapper.getInstance().initOnlineCourse(resultSet);
                    } else {
                        course = CourseMapper.getInstance().initOnsiteCourse(resultSet);
                    }
                    courses.add(course);
                }
                return courses;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Course> getById(Integer courseId) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = """
                SELECT c.*, d.Name, c1.PersonID, p.Firstname, p.Lastname, o.url, o1.Location, Days, Time, COUNT(s.StudentID) as sumOfStudents
                FROM course c
                LEFT JOIN department d
                ON c.DepartmentID = d.DepartmentID
                LEFT JOIN courseinstructor c1
                ON c.CourseID = c1.CourseID
                LEFT JOIN person p
                ON c1.PersonID = p.PersonID
                LEFT JOIN onlinecourse o
                ON c.CourseID = o.CourseID
                LEFT JOIN onsitecourse o1
                ON c.CourseID = o1.CourseID
                LEFT JOIN school.studentgrade s
                ON c.CourseID = s.CourseID
                WHERE c.CourseID = %s
                """.formatted(courseId);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            Course course = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String url = resultSet.getString("url");
                if(url != null) {
                    course = CourseMapper.getInstance().initOnlineCourse(resultSet);
                } else {
                    course = CourseMapper.getInstance().initOnsiteCourse(resultSet);
                }
            }
            return Optional.ofNullable(course);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public int registerStudentForCourse(Integer studentId, Integer courseId) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO studentgrade VALUE (null, ?, ?, null);";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.setInt(2, studentId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public Optional<Course> createCourse(Course course) {
        if (course == null || course.getTitle() == null || course.getCredits() == null) {
            return Optional.empty();
        }
        String sql = "INSERT INTO `course` (`Title`, `Credits`, `DepartmentID`) VALUES (?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, course.getTitle());
            preparedStatement.setInt(2, course.getCredits());
            preparedStatement.setInt(3, 1);

            int isSuccess = preparedStatement.executeUpdate();
            if (isSuccess > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    course.setId(newId);
                    return Optional.of(course);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public int deleteCourse(Integer courseId) {
        if(courseId == null || courseId <= 0) return 0;
        String sql = "DELETE FROM `course` WHERE  CourseID=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, courseId.toString());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public int updateCourse(Course course) {
        if(course == null || course.getId() == null || course.getCredits() == null)
            return 0;
//        String sql = "UPDATE `course` SET `Title`=? AND `Credits`=? AND `DepartmentID`=? WHERE  `CourseID`=?;";
        String sql = "UPDATE `course` SET `Title`=? AND `Credits`=? WHERE  `CourseID`=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, course.getTitle().toString());
            preparedStatement.setString(2, course.getCredits().toString());
//            preparedStatement.setString(3, course.getDepartment().toString());
            preparedStatement.setString(3, course.getId().toString());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return 0;
        }
    }


    @Override
    public Boolean isStudentInCourse(Integer studentId, Integer courseId) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM studentgrade WHERE CourseID = ? AND StudentID = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.setInt(2, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
            return null;
        }
    }
}
