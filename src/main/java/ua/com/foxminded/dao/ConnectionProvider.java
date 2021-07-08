package ua.com.foxminded.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionProvider {

    private String host;
    private String login;
    private String password;

    public ConnectionProvider() {
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
    }

    public Connection getConnection() throws DaoException {
        try {
            Connection connection = DriverManager.getConnection(host, login, password);
            return connection;
        } catch (SQLException e) {
            throw new DaoException("No connection", e);
        }

    }

}
