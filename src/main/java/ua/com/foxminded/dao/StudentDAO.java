package ua.com.foxminded.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ua.com.foxminded.model.Group;
import ua.com.foxminded.model.Student;

public class StudentDao {

    private static final String INSERT = "insert into students ( group_id, first_name, last_name) values (?,?,?)";
    private static final String SELECT = "select * from students where student_id=?";
    private static final String DELETE = "delete from students where student_id=?";
    private DaoFactory daoFactory = new DaoFactory();

    public void create(Student student) throws DaoException, SQLException {
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, student.getGroup().getId());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    student.setId(resultSet.getInt(1));
                }
            } catch (SQLException e) {
                throw new DaoException();
            }
        }
    }

    public Student read(int studentId) throws SQLException, DaoException {
        Student student = null;
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                student = new Student(resultSet.getString("first_name"), resultSet.getString("last_name"));
                student.setId(resultSet.getInt("student_id"));
            }
        } catch (SQLException e) {
            throw new DaoException();
        }
        return student;
    }

    public void delete(int id) throws DaoException {
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException();
        }
    }

}
