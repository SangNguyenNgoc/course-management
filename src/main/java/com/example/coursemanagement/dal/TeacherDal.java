package com.example.coursemanagement.dal;

import com.example.coursemanagement.dal.interfaces.ITeacherDal;
import com.example.coursemanagement.dtos.Teacher;
import com.example.coursemanagement.mapper.TeacherMapper;
import com.example.coursemanagement.utils.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
