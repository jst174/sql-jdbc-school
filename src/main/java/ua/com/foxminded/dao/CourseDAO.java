package ua.com.foxminded.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Group;

public class CourseDAO {

    private DAOFactory daoFactory = DAOFactory.getInstance();

    public void create(Course course) throws DAOException, SQLException {
        String sql = "insert into courses (course_name, course_description) values (?,?);";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()){
                if(resultSet.next()) {
                    course.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error creating", e);
        }
    }
}
