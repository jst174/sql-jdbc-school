package ua.com.foxminded.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Group;

public class CourseDao {

    private static final String INSERT = "insert into courses (course_name, course_description) values (?,?)";
    private static final String SELECT = "select * from courses where course_name = ?";
    private DaoFactory daoFactory = new DaoFactory();

    public void create(Course course) throws DaoException, SQLException {

        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
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

    public Course read(String courseName) throws DaoException {
        Course course = null;
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, courseName);
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
