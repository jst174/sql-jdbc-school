package ua.com.foxminded.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ua.com.foxminded.model.Group;

public class GroupDAO {

    private DAOFactory daoFactory = DAOFactory.getInstance();

    public void create(Group group) throws DAOException {
        String sql = "insert into groups (group_name) values (?);";
        
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, group.getName());
            statement.execute();
            try(ResultSet resultSet = statement.getGeneratedKeys()){
                if(resultSet.next()) {
                    group.setId(resultSet.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new DAOException();
        } 
    }

}
