package com.example.coursemanagement.dal;

import com.example.coursemanagement.dal.interfaces.ICourseDal;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.dtos.OnlineCourse;
import com.example.coursemanagement.dtos.OnsiteCourse;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDal implements ICourseDal {

    private static class CourseDalHolder{
        private static final CourseDal INSTANCE = new CourseDal();
    }

    private CourseDal(){}

    public static CourseDal getInstance() {
        return CourseDalHolder.INSTANCE;
    }

    @Override
    public List<Course> getAllCourses() {
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
                        course = OnlineCourse.builder()
                                .id(resultSet.getInt("CourseID"))
                                .credits(resultSet.getInt("Credits"))
                                .department(resultSet.getString("Name"))
                                .sumOfStudent(0)
                                .teacher(resultSet.getString("Firstname") == null || resultSet.getString("Lastname") == null ?
                                        " " : resultSet.getString("Firstname") + " " + resultSet.getString("Lastname"))
                                .title(resultSet.getString("Title"))
                                .url(url)
                                .type("Khóa trực tuyến")
                                .sumOfStudent(resultSet.getInt("sumOfStudents"))
                                .build();
                    } else {
                        course = OnsiteCourse.builder()
                                .id(resultSet.getInt("CourseID"))
                                .credits(resultSet.getInt("Credits"))
                                .department(resultSet.getString("Name"))
                                .sumOfStudent(0)
                                .teacher(resultSet.getString("Firstname") == null || resultSet.getString("Lastname") == null ?
                                        " " : resultSet.getString("Firstname") + " " + resultSet.getString("Lastname"))
                                .title(resultSet.getString("Title"))
                                .location(resultSet.getString("Location"))
                                .days(resultSet.getString("Days"))
                                .time(resultSet.getTime("Time").toLocalTime())
                                .type("Khóa tại chỗ")
                                .sumOfStudent(resultSet.getInt("sumOfStudents"))
                                .build();
                    }
                    courses.add(course);
                }
                return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
