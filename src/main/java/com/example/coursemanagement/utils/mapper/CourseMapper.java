package com.example.coursemanagement.utils.mapper;

import com.example.coursemanagement.dtos.OnlineCourse;
import com.example.coursemanagement.dtos.OnsiteCourse;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseMapper {

    private static class CourseMapperHolder{
        private static final CourseMapper INSTANCE = new CourseMapper();
    }

    private CourseMapper() {

    }

    public static CourseMapper getInstance() {
        return CourseMapperHolder.INSTANCE;
    }
    public OnsiteCourse initOnsiteCourse(ResultSet resultSet) throws SQLException {
        return OnsiteCourse.builder()
                .id(resultSet.getInt("CourseID"))
                .credits(resultSet.getInt("Credits"))
                .department(resultSet.getString("Name"))
                .sumOfStudent(0)
                .teacher(resultSet.getString("Firstname") == null || resultSet.getString("Lastname") == null ?
                        " " : resultSet.getString("Lastname") + " " + resultSet.getString("Firstname"))
                .title(resultSet.getString("Title"))
                .location(resultSet.getString("Location"))
                .days(resultSet.getString("Days"))
                .time(resultSet.getTime("Time").toLocalTime())
                .type("Khóa tại chỗ")
                .sumOfStudent(resultSet.getInt("sumOfStudents"))
                .build();
    }

    public OnlineCourse initOnlineCourse(ResultSet resultSet) throws SQLException {
        return OnlineCourse.builder()
                .id(resultSet.getInt("CourseID"))
                .credits(resultSet.getInt("Credits"))
                .department(resultSet.getString("Name"))
                .sumOfStudent(0)
                .teacher(resultSet.getString("Firstname") == null || resultSet.getString("Lastname") == null ?
                        " " : resultSet.getString("Lastname") + " " + resultSet.getString("Firstname"))
                .title(resultSet.getString("Title"))
                .url(resultSet.getString("url"))
                .type("Khóa trực tuyến")
                .sumOfStudent(resultSet.getInt("sumOfStudents"))
                .build();
    }
}
