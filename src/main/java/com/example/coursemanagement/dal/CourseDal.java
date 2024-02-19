package com.example.coursemanagement.dal;

import com.example.coursemanagement.dal.interfaces.ICourseDal;
import com.example.coursemanagement.dtos.Course;
import com.example.coursemanagement.mapper.CourseMapper;
import com.example.coursemanagement.utils.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
       return Optional.empty();
    }

    @Override
    public int registerStudentForCourse(Integer personId, Integer courseId) {
        return 0;
    }

    @Override
    public Optional<Course> createCourse(Course course) {
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
}
