package ua.com.foxminded.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Student;

public class StudentDao {

    private static final String INSERT_STUDENT = "insert into students ( group_id, first_name, last_name) values (null,?,?)";
    private static final String INSERT_STUDENT_AND_COURSE = "insert into student_courses(student_id, course_id) "
            + "values((select student_id from students where student_id=?),(select course_id from courses where course_id=?))";
    private static final String SELECT = "select * from students where student_id=?";
    private static final String SELECT_STUDENT_ID = "select student_id from student_courses where course_id=?";
    private static final String UPDATE = "update students set group_id=? where student_id=?";
    private static final String DELETE = "delete from students where student_id=?";
    private DaoFactory daoFactory = new DaoFactory();

    public void create(Student student) throws DaoException, SQLException {
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_STUDENT,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
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

    public void createStudentCourses(Student student, Course course) throws DaoException {
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_STUDENT_AND_COURSE,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, student.getId());
            statement.setInt(2, course.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException();
        }
    }

    public void assignGroup(Student student) throws DaoException {
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, student.getGroup().getId());
            statement.setInt(2, student.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException();
        }
    }

    public List<Integer> getStudentsId(Course course) throws DaoException {
        List<Integer> studentsId = new ArrayList<>();
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_STUDENT_ID,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, course.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                studentsId.add(resultSet.getInt("student_id"));
            }

        } catch (SQLException e) {
            throw new DaoException();
        }
        return studentsId;
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
