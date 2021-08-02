package ua.com.foxminded.service;

import java.util.List;

import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Student;

public class StudentService {

    private StudentDao studentDao;
    private CourseDao courseDao;

    public StudentService(StudentDao studentDao, CourseDao courseDao) {
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    public void addStudent(String firstName, String lastName) throws DaoException {
        Student student = new Student(firstName, lastName);
        studentDao.create(student);
    }

    public void deleteStudent(int id) throws DaoException {
        studentDao.delete(id);
    }

    public List<Student> findAllByCourse(int courseId) throws DaoException {
        Course course = courseDao.read(courseId);
        return studentDao.findAllByCourse(course);
    }
}
