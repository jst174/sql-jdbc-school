package ua.com.foxminded.service;

import java.util.List;

import ua.com.foxminded.ConnectionProvider;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.GroupDao;
import ua.com.foxminded.model.Group;

public class GroupService {

    private ConnectionProvider connectionProvider;

    public GroupService() {
        this.connectionProvider = new ConnectionProvider("db.properties");
    }
    
    public List<Group> findGroupsByStudentsCount(int count) throws DaoException {
        GroupDao groupDao = new GroupDao(connectionProvider);
        return groupDao.findGroupsByStudentsCount(count);
    }

}
