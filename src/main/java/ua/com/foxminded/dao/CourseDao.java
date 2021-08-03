package ua.com.foxminded.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ua.com.foxminded.ConnectionProvider;
import ua.com.foxminded.model.Course;

public class CourseDao {

    private static final String INSERT_NEW_COURSE = "insert into courses (course_name, course_description) values (?,?)";
    private static final String SELECT_COURSE_BY_ID = "select * from courses where course_id = ?";

    private ConnectionProvider connectionProvider;

    public CourseDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void create(Course course) throws DaoException {
        try (Connection connection = connectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_NEW_COURSE,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    course.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error creating", e);
        }
    }

    public Course read(int id) throws DaoException {
        Course course = null;
        try (Connection connection = connectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_COURSE_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("course_name");
                String description = resultSet.getString("course_description");
                course = new Course(name, description);
                course.setId(resultSet.getInt("course_id"));
            }

        } catch (SQLException e) {
            throw new DaoException("Error reading", e);
        }
        return course;
    }

}
