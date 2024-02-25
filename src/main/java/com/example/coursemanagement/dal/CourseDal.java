package com.example.coursemanagement.dal;

import com.example.coursemanagement.dal.interfaces.ICourseDal;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.utils.mapper.CourseMapper;
import com.example.coursemanagement.dtos.OnlineCourse;
import com.example.coursemanagement.mapper.CourseMapper;
import com.example.coursemanagement.utils.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

                // Thực hiện câu lệnh SQL
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating course failed, no rows affected.");
                }

                // Lấy thông tin về khóa học vừa được tạo
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int courseID = generatedKeys.getInt(1);
                        String insertCourseInstructorSql = "INSERT INTO `courseinstructor` (`CourseID`, `PersonID`) VALUES (?, ?)";
                        try (PreparedStatement courseInstructorStatement = connection.prepareStatement(insertCourseInstructorSql)) {
                            // Thiết lập các tham số cho bảng courseinstructor
                            courseInstructorStatement.setInt(1, courseID);
                            courseInstructorStatement.setInt(2, teacher);

                            // Thực hiện câu lệnh INSERT cho bảng courseinstructor
                            courseInstructorStatement.executeUpdate();
                        }
                        // Tạo và trả về Optional<Course>
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
            e.printStackTrace(); // Hoặc xử lý lỗi theo nhu cầu của bạn
        }

        return Optional.empty();
    }

    @Override
    public int deleteCourse(Integer courseId) {
        return 0;
    }

    @Override
    public int updateCourse(Course course) {
        return 0;
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

    public Boolean createCourseOnline(Integer courseId, String url) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();

        // Câu lệnh SQL để thêm khóa học trực tuyến
        String sql = "INSERT INTO `onlinecourse` (`CourseID`, `url`) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Thiết lập các tham số cho bảng onlinecourse
            statement.setInt(1, courseId);
            statement.setString(2, url);

            // Thực hiện câu lệnh INSERT cho bảng onlinecourse
            int affectedRows = statement.executeUpdate();

            // Kiểm tra xem có bản ghi nào bị ảnh hưởng hay không
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Hoặc xử lý lỗi theo nhu cầu của bạn
        }

        return false;
    }

    public Boolean createCourseOnsite(Integer courseId, String location, LocalDate date, LocalTime time) {
        java.sql.Connection connection = DbConnection.getInstance().getConnection();

        // Câu lệnh SQL để thêm khóa học trực tuyến
        String sql = "INSERT INTO `onsitecourse` (`CourseID`, `Location`, `Days`, `Time`) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Thiết lập các tham số cho bảng onsitecourse
            statement.setInt(1, courseId);
            statement.setString(2, location);

            // Chuyển đổi LocalDate và LocalTime thành định dạng phù hợp
            String formattedDate = date.toString(); // Đơn giản lấy chuỗi của LocalDate
            String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm")); // Định dạng LocalTime thành chuỗi "HH:mm"

            // Thiết lập giá trị cho trường Days và Time
            statement.setString(3, formattedDate);
            statement.setString(4, formattedTime);

            // Thực hiện câu lệnh INSERT cho bảng onsitecourse
            int affectedRows = statement.executeUpdate();

            // Kiểm tra xem có bản ghi nào bị ảnh hưởng hay không
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Hoặc xử lý lỗi theo nhu cầu của bạn
        }

        return false;
    }


}
