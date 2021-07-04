package ua.com.foxminded.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ua.com.foxminded.model.Main;

public class SqlScript {

    public void executeScript() throws SQLException, DaoException {
        DaoFactory daoFactory = new DaoFactory();
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(getSqlScript())) {
            statement.execute();
        } catch (SQLException e) {
            throw new DaoException("Unable to execute init script", e);
        }
    }

    private String getSqlScript() {
        try {
            ClassLoader classLoader = Main.class.getClassLoader();
            File file = new File(classLoader.getResource("schema.sql").getFile());
            return new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            throw new RuntimeException("File is not found", e);
        }

    }

}
