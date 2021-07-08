package ua.com.foxminded.model;

import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.dao.ConnectionProvider;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.GroupDao;

public class Group {

    private int id;
    private String name;
    private List<Student> students = new ArrayList<>();
    private ConnectionProvider connectionProvider;

    public Group() {
        connectionProvider = new ConnectionProvider();
    }

    public Group(String name) {
        this.name = name;
    }

    public List<Group> findGroups(int count) throws DaoException {
        GroupDao groupDao = new GroupDao(connectionProvider);
        return groupDao.findGroups(count);
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<Student> getStudents() {
        return students;
    }

}
