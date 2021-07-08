package ua.com.foxminded;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ua.com.foxminded.dao.ConnectionProvider;
import ua.com.foxminded.dao.DaoException;

public class SqlScript {

    public void executeScript(String fileName) throws DaoException {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        try (Connection connection = connectionProvider.getConnection();
                PreparedStatement statement = connection.prepareStatement(getSqlScript(fileName))) {
            statement.execute();
        } catch (SQLException e) {
            throw new DaoException("Unable to execute init script", e);
        }
    }

    private String getSqlScript(String fileName) {
        try {
            ClassLoader classLoader = Main.class.getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            return new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            throw new RuntimeException("File is not found", e);
        }

    }

}
