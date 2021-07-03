package ua.com.foxminded.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.DaoFactory;
import ua.com.foxminded.dao.GroupDao;
import ua.com.foxminded.dao.StudentCoursesDao;
import ua.com.foxminded.dao.StudentDao;

public class Admin {
    private DaoFactory daoFactory;
    private CourseDao courseDAO;
    private GroupDao groupDAO;
    private StudentDao studentDAO;
    private StudentCoursesDao studentCoursesDAO;
    private DataSourse dataSourse;
    private List<Course> courses;
    private List<Group> groups;
    private List<Student> students;

    public Admin() throws IOException {
        daoFactory = new DaoFactory();
        courseDAO = daoFactory.getCourseDao();
        groupDAO = daoFactory.getGroupDAO();
        studentDAO = daoFactory.getStudentDAO();
        studentCoursesDAO = daoFactory.getStudentCoursesDAO();
        dataSourse = new DataSourse();
        courses = dataSourse.getCourses();
        groups = dataSourse.getGroups();
        students = new ArrayList<>(dataSourse.getStudents());
    }

    public void addStudent(String firstName, String lastName) throws DaoException, SQLException {
        Student student = new Student(firstName, lastName);
        students.add(student);
        assignGroup(student);
        studentDAO.create(student);
    }

    public void deleteStudent(int id) throws DaoException {
        studentDAO.delete(id);
    }

    public void addStudentToCourse(String courseName, int studentId) throws DaoException, SQLException {
        Student student = studentDAO.read(studentId);
        Course course = courseDAO.read(courseName);
        studentCoursesDAO.create(student, course);
    }

    public void createCourses() throws DaoException, SQLException {
        for (int i = 0; i < courses.size(); i++) {
            courseDAO.create(courses.get(i));
        }

    }

    public void createGroups() throws DaoException {
        for (int i = 0; i < groups.size(); i++) {
            groupDAO.create(groups.get(i));
        }
    }

    public void createStudents() throws DaoException, SQLException {
        for (int i = 0; i < students.size(); i++) {
            assignGroup(students.get(i));
            assignCourses(students.get(i));
            studentDAO.create(students.get(i));
            List<Course> courses = new ArrayList<>(students.get(i).getCourses());
            for (int j = 0; j < courses.size(); j++) {
                studentCoursesDAO.create(students.get(i), courses.get(j));
            }

        }

    }

    public void removeStudentFromCourse(int student_id, String courseName) throws DaoException, SQLException {
        Student student = studentDAO.read(student_id);
        Course course = courseDAO.read(courseName);
        studentCoursesDAO.delete(student, course);
    }

    public List<Student> findStudentsFromCourse(String courseName) throws DaoException, SQLException {
        Course course = courseDAO.read(courseName);
        List<Integer> studentId = studentCoursesDAO.getStudentsId(course);
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < studentId.size(); i++) {
            students.add(studentDAO.read(studentId.get(i)));
        }
        return students;
    }

    public List<Group> findGroups(int count) throws DaoException {
        return groupDAO.read(count);
    }

    private void assignGroup(Student student) {
        Random random = new Random();
        Group group = groups.get(random.nextInt(groups.size()));
        student.setGroup(group);
        group.getStudents().add(student);
    }

    private void assignCourses(Student student) {
        Random random = new Random();
        int minPerStudent = 1;
        int maxPerStudent = 3;
        int range = maxPerStudent - minPerStudent;
        for (int i = 0; i < (random.nextInt(range) + 1 + minPerStudent); i++) {
            student.setCourses(courses.get(random.nextInt(courses.size())));
        }
    }

}
