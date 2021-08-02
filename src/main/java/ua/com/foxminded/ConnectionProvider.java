package ua.com.foxminded;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionProvider {

    private static final String HOST = "db.host";
    private static final String LOGIN = "db.login";
    private static final String PASSWORD = "db.password";

    private String host;
    private String login;
    private String password;

    public ConnectionProvider(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        Properties properties = new Properties();
        try (InputStream file = classLoader.getResourceAsStream(fileName)) {
            properties.load(file);
            host = properties.getProperty(HOST);
            login = properties.getProperty(LOGIN);
            password = properties.getProperty(PASSWORD);

        } catch (IOException e) {
            throw new RuntimeException("config file not found");
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(host, login, password);
    }
}
