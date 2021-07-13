package ua.com.foxminded.service;

import java.util.List;

import ua.com.foxminded.ConnectionProvider;
import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Student;

public class CourseService {

    private ConnectionProvider connectionProvider;
    
    public CourseService() {
        this.connectionProvider = new ConnectionProvider("db.properties");
    }
    
    public List<Student> findStudentsFromCourse(int courseId) throws DaoException {
        CourseDao courseDao = new CourseDao(connectionProvider);
        Course course = courseDao.read(courseId);
        return course.getStudents();
    }

    public void addStudentToCourse(int courseId, int studentId) throws DaoException {
        CourseDao courseDao = new CourseDao(connectionProvider);
        StudentDao studentDao = new StudentDao(connectionProvider);
        Student student = studentDao.read(studentId);
        Course course = courseDao.read(courseId);
        student.setCourses(course);
        studentDao.addStudentToCourse(student);
    }

    public void removeStudentFromCourse(int studentId, int courseId) throws DaoException {
        CourseDao courseDao = new CourseDao(connectionProvider);
        StudentDao studentDao = new StudentDao(connectionProvider);
        Student student = studentDao.read(studentId);
        Course course = courseDao.read(courseId);
        studentDao.deleteStudentFromCourse(student, course);
    }
    
}
