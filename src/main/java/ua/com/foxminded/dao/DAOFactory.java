package ua.com.foxminded.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOFactory {

    private static final String USER = "admin";
    private static final String PASSWORD = "1234";
    private static final String URL = "jdbc:postgresql://localhost:5432/school";
    private Connection connection;

    public static DAOFactory getInstance() {
        return new DAOFactory();
    }
    
    public CourseDAO getCourseDao() {
        return new CourseDAO();
    }
    
    public GroupDAO getGroupDAO() {
        return new GroupDAO();
    }
    
    public StudentDAO getStudentDAO() {
        return new StudentDAO();
    }

    public Connection getConnection() throws DAOException {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DAOException("No connection", e);
        }
        return connection;
    }

}
