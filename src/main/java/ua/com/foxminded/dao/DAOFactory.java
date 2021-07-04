package ua.com.foxminded.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DaoFactory {

    private String host;
    private String login;
    private String password;

    public CourseDao getCourseDao() {
        return new CourseDao();
    }

    public GroupDao getGroupDAO() {
        return new GroupDao();
    }

    public StudentDao getStudentDAO() {
        return new StudentDao();
    }

    public Connection getConnection() throws DaoException {
        ClassLoader classLoader = getClass().getClassLoader();
        Properties properties = new Properties();
        try (InputStream file = classLoader.getResourceAsStream("db.properties")) {
            properties.load(file);
            host = properties.getProperty("db.host");
            login = properties.getProperty("db.login");
            password = properties.getProperty("db.password");

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection(host, login, password);
            return connection;
        } catch (SQLException e) {
            throw new DaoException("No connection", e);
        }

    }

}
