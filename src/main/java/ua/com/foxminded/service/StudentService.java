package ua.com.foxminded.service;

import ua.com.foxminded.ConnectionProvider;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.model.Student;

public class StudentService {

    private ConnectionProvider connectionProvider;

    public StudentService() {
        this.connectionProvider = new ConnectionProvider("db.properties");
    }

    public void addStudent(String firstName, String lastName) throws DaoException {
        StudentDao studentDao = new StudentDao(connectionProvider);
        Student student = new Student(firstName, lastName);
        studentDao.create(student);
    }

    public void deleteStudent(int id) throws DaoException {
        StudentDao studentDao = new StudentDao(connectionProvider);
        studentDao.delete(id);
    }
}
