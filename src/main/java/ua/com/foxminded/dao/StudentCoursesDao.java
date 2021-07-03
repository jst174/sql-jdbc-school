package ua.com.foxminded.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Group;
import ua.com.foxminded.model.Student;

public class StudentCoursesDao {

    private static final String INSERT = "insert into student_courses(student_id, course_id) "
            + "values((select student_id from students where student_id=?),(select course_id from courses where course_id=?))";
    private static final String SELECT = "select student_id from student_courses where course_id=?";
    private static final String DELETE = "delete from student_courses where student_id=? and course_id=?";
    private DaoFactory daoFactory = new DaoFactory();

    public void create(Student student, Course course) throws DaoException {
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, student.getId());
            statement.setInt(2, course.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException();
        }
    }
    
    public List<Integer> getStudentsId(Course course) throws DaoException {
        List<Integer> studentsId = new ArrayList<>();
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, course.getId());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                studentsId.add(resultSet.getInt("student_id"));
            }

        } catch (SQLException e) {
            throw new DaoException();
        }
        return studentsId;
    }

    public void delete(Student student, Course course) throws DaoException {
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, student.getId());
            statement.setInt(2, course.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException();
        }
    }
}
