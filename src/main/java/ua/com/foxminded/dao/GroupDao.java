package ua.com.foxminded.dao;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ua.com.foxminded.ConnectionProvider;
import ua.com.foxminded.model.Group;

public class GroupDao {

	private static final String INSERT_NEW_GROUP = "insert into groups (group_name) values (?)";
	private static final String SELECT_GROUPS_BY_STUDENT_COUNT = "select g.group_name, count(s.student_id) from "
			+ "groups g left join students s on s.group_id=g.group_id "
			+ "group by g.group_name having count(s.student_id)<?;";

	private ConnectionProvider connectionProvider;

	public GroupDao(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	public void create(Group group) throws DaoException {
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_NEW_GROUP,
						Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, group.getName());
			statement.executeUpdate();
			try (ResultSet resultSet = statement.getGeneratedKeys()) {
				if (resultSet.next()) {
					group.setId(resultSet.getInt(1));
				}
			}

		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public List<Group> findGroupsByStudentsCount(int count) throws DaoException {
		List<Group> groups = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_GROUPS_BY_STUDENT_COUNT)) {
			statement.setInt(1, count);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				groups.add(new Group(resultSet.getString("group_name")));
			}

		} catch (SQLException e) {
			throw new DaoException();
		}
		return groups;
	}

}
