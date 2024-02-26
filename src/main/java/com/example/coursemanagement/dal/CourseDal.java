package com.example.coursemanagement.dal;

import com.example.coursemanagement.dal.interfaces.ICourseDal;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.utils.mapper.CourseMapper;
import com.example.coursemanagement.utils.DbConnection;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseDal implements ICourseDal {

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
                ON c.CourseID = s.CourseID GROUP BY c.CourseID
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
    public Optional<Course> createCourse(Course course, Integer departmentId, Integer teacher) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();

        // Câu lệnh SQL để thêm khóa học trực tuyến
        String sql = "INSERT INTO `course` (`Title`, `Credits`, `DepartmentID`) VALUES (?, ?, ?)";

        try {
            // Tạo prepared statement và thiết lập giá trị tham số
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, course.getTitle());
                preparedStatement.setInt(2, course.getCredits());
                preparedStatement.setInt(3, departmentId);
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating course failed, no rows affected.");
                }
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int courseID = generatedKeys.getInt(1);
                        String insertCourseInstructorSql = "INSERT INTO `courseinstructor` (`CourseID`, `PersonID`) VALUES (?, ?)";
                        try (PreparedStatement courseInstructorStatement = connection.prepareStatement(insertCourseInstructorSql)) {
                            courseInstructorStatement.setInt(1, courseID);
                            courseInstructorStatement.setInt(2, teacher);

                            courseInstructorStatement.executeUpdate();
                        }
                        return Optional.of(Course.builder()
                                .id(courseID)
                                .title(course.getTitle())
                                .credits(course.getCredits())
                                .build()
                        );
                    } else {
                        throw new SQLException("Creating course failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());        }

        return Optional.empty();
    }

    @Override
    public void deleteCourse(Integer courseId) {
        Connection connection = DbConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM onlinecourse WHERE CourseID = ?");
            preparedStatement.setInt(1, courseId);
            int rowsAffectedOnlineCourse = preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("DELETE FROM onsitecourse WHERE CourseID = ?");
            preparedStatement.setInt(1, courseId);
            int rowsAffectedOnsiteCourse = preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("DELETE FROM courseinstructor WHERE CourseID = ?");
            preparedStatement.setInt(1, courseId);
            int rowsAffectedCourseInstructor = preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("DELETE FROM course WHERE CourseID = ?");
            preparedStatement.setInt(1, courseId);
            int rowsAffectedCourse = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());

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

    @Override
    public Boolean createCourseOnline(Integer courseId, String url) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO `onlinecourse` (`CourseID`, `url`) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            statement.setString(2, url);

            int affectedRows = statement.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());        }

        return false;
    }

    @Override
    public Boolean createCourseOnsite(Integer courseId, String location, String date, LocalTime time) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO `onsitecourse` (`CourseID`, `Location`, `Days`, `Time`) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            statement.setString(2, location);

            String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm")); // Định dạng LocalTime thành chuỗi "HH:mm"

            statement.setString(3, date);
            statement.setString(4, formattedTime);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
        }
        return false;
    }

    @Override
    public int updateCourse(Course course, Integer departmentId, Integer teacher) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE course SET Title = ?, Credits = ?, DepartmentID = ? WHERE CourseID = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, course.getTitle());
            preparedStatement.setInt(2, course.getCredits());
            preparedStatement.setInt(3, departmentId);
            preparedStatement.setInt(4, course.getId());
            int result = preparedStatement.executeUpdate();
            if(result != 0) {
                String updateCourseInstructor = "UPDATE courseinstructor SET PersonID = ? WHERE CourseID =?";
                PreparedStatement courseInstructorPrepare = connection.prepareStatement(updateCourseInstructor);
                courseInstructorPrepare.setInt(1, teacher);
                courseInstructorPrepare.setInt(2, course.getId());
                return courseInstructorPrepare.executeUpdate();
            } else {
                return 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int updateCourseOnsite(Integer courseId, String location, String date, LocalTime time) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE onsitecourse SET Days = ?, Location = ?, Time = ? WHERE CourseID = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
            preparedStatement.setString(1, date);
            preparedStatement.setString(2, location);
            preparedStatement.setString(3, formattedTime);
            preparedStatement.setInt(4, courseId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int updateCourseOnline(Integer courseId, String url) {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE onlinecourse SET url = ? WHERE CourseID = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, url);
            preparedStatement.setInt(2, courseId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Query failure: " + e.getMessage());
        }
        return 0;
    }
}
