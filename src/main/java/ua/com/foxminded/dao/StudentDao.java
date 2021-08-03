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

public class StudentDao {

    private static final String INSERT_STUDENT = "insert into students ( group_id, first_name, last_name) values (null,?,?)";
    private static final String INSERT_STUDENT_AND_COURSE = "insert into student_courses(student_id, course_id) "
            + "values((select student_id from students where student_id=?),(select course_id from courses where course_id=?))";
    private static final String SELECT_STUDENT_BY_ID = "select * from students where student_id=?";
    private static final String SELECT_STUDENTS_BY_COURSE = "select student_id from student_courses where course_id = ?";
    private static final String UPDATE_GROUP_FOR_STUDENT = "update students set group_id=? where student_id=?";
    private static final String DELETE_STUDENT_BY_ID = "delete from students where student_id=?";
    private static final String DELETE_STUDENT_FROM_COURSE = "delete from student_courses where student_id=? and course_id=?";

    private ConnectionProvider connectionProvider;

    public StudentDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void create(Student student) throws DaoException {
        try (Connection connection = connectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_STUDENT,
                        Statement.RETURN_GENERATED_KEYS);
                PreparedStatement statement2 = connection.prepareStatement(INSERT_STUDENT_AND_COURSE)) {
            connection.setAutoCommit(false);
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    student.setId(resultSet.getInt(1));
                    List<Course> courses = new ArrayList<>(student.getCourses());
                    for (Course course : courses) {
                        statement2.setInt(1, student.getId());
                        statement2.setInt(2, course.getId());
                        statement2.addBatch();
                    }
                    statement2.executeBatch();
                    connection.commit();
                }
            }

        } catch (SQLException e) {
            throw new DaoException();
        }
    }

    public void addStudentToCourse(Student student) throws DaoException {
        try (Connection connection = connectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_STUDENT_AND_COURSE)) {
            connection.setAutoCommit(false);
            List<Course> courses = new ArrayList<>(student.getCourses());
            for (Course course : courses) {
                statement.setInt(1, student.getId());
                statement.setInt(2, course.getId());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException();
        }
    }

    public void assignGroup(Student student) throws DaoException {
        try (Connection connection = connectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_GROUP_FOR_STUDENT)) {
            statement.setInt(1, student.getGroup().getId());
            statement.setInt(2, student.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException();
        }
    }

    public Student read(int studentId) throws DaoException {
        Student student = null;
        try (Connection connection = connectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_STUDENT_BY_ID)) {
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
        try (Connection connection = connectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_STUDENT_BY_ID)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException();
        }
    }

    public void deleteStudentFromCourse(Student student, Course course) throws DaoException {
        try (Connection connection = connectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_STUDENT_FROM_COURSE)) {
            statement.setInt(1, student.getId());
            statement.setInt(2, course.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException();
        }
    }

    public List<Student> findAllByCourse(Course course) throws DaoException {
        try (Connection connection = connectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_STUDENTS_BY_COURSE)) {
            statement.setInt(1, course.getId());
            ResultSet resultSet = statement.executeQuery();
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                Student student = read(resultSet.getInt("student_id"));
                students.add(student);
            }
            return students;

        } catch (SQLException e) {
            throw new DaoException("Error reading", e);
        }

    }

}
