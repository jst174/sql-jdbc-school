package ua.com.foxminded.service;

import java.util.List;

import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Student;

public class CourseService {

    private CourseDao courseDao;
    private StudentDao studentDao;

    public CourseService(CourseDao courseDao, StudentDao studentDao) {
        this.courseDao = courseDao;
        this.studentDao = studentDao;
    }

    public void addStudentToCourse(int courseId, int studentId) throws DaoException {
        Student student = studentDao.read(studentId);
        Course course = courseDao.read(courseId);
        student.setCourses(course);
        studentDao.addStudentToCourse(student);
    }

    public void removeStudentFromCourse(int studentId, int courseId) throws DaoException {
        Student student = studentDao.read(studentId);
        Course course = courseDao.read(courseId);
        studentDao.deleteStudentFromCourse(student, course);
    }

}
