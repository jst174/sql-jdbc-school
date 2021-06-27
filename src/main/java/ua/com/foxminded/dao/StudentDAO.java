package ua.com.foxminded.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ua.com.foxminded.model.Group;
import ua.com.foxminded.model.Student;

public class StudentDAO {
    private DAOFactory daoFactory = DAOFactory.getInstance();

    public void create(Student student) throws DAOException, SQLException {
        String sql = "insert into students ( group_id, first_name, last_name) values (?,?,?);";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, student.getGroup().getId());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            statement.execute();
            try(ResultSet resultSet = statement.getGeneratedKeys()){
                if(resultSet.next()) {
                    student.setId(resultSet.getInt(1));
                }
            }
        } 
    }

}
