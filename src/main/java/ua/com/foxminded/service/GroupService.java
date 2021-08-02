package ua.com.foxminded.service;

import java.util.List;

import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.GroupDao;
import ua.com.foxminded.model.Group;

public class GroupService {

    private GroupDao groupDao;

    public GroupService(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public List<Group> findGroupsByStudentsCount(int count) throws DaoException {
        return groupDao.findGroupsByStudentsCount(count);
    }

}
