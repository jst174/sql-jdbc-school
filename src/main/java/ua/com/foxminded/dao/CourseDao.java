package ua.com.foxminded.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.ConnectionProvider;
import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Student;

public class CourseDao {

    private static final String INSERT = "insert into courses (course_name, course_description) values (?,?)";
    private static final String SELECT = "select * from courses where course_id = ?";
    private static final String SELECT_STUDENTS_ID = "select student_id from student_courses where course_id = ?";
    private ConnectionProvider connectionProvider;

    public CourseDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void create(Course course) throws DaoException {

        try (Connection connection = connectionProvider.getConnection();
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

    public Course read(int id) throws DaoException {
        Course course = null;
        StudentDao studentDao = new StudentDao(connectionProvider);
        try (Connection connection = connectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("course_name");
                String description = resultSet.getString("course_description");
                course = new Course(name, description);
                course.setId(resultSet.getInt("course_id"));
            }
            for (int studentId : getStudentsIdFromCourse(course)) {
                Student student = studentDao.read(studentId);
                course.setStudents(student);
            }

        } catch (SQLException e) {
            throw new DaoException("Error reading", e);
        }
        return course;
    }

    private List<Integer> getStudentsIdFromCourse(Course course) throws DaoException {
        try (Connection connection = connectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_STUDENTS_ID)) {
            List<Integer> studentsId = new ArrayList<>();
            statement.setInt(1, course.getId());
            ResultSet resultSet2 = statement.executeQuery();
            while (resultSet2.next()) {
                studentsId.add(resultSet2.getInt("student_id"));
            }
            return studentsId;

        } catch (SQLException e) {
            throw new DaoException("Error reading", e);
        }
    }

}
