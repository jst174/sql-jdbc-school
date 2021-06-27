package ua.com.foxminded.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ua.com.foxminded.dao.CourseDAO;
import ua.com.foxminded.dao.DAOException;
import ua.com.foxminded.dao.DAOFactory;
import ua.com.foxminded.dao.GroupDAO;
import ua.com.foxminded.dao.StudentDAO;

public class Admin {
    private DAOFactory daoFactory;
    private CourseDAO courseDAO;
    private GroupDAO groupDAO;
    private StudentDAO studentDAO;
    private DataSourse dataSourse;
    private List<Course> courses;
    private List<Group> groups;
    private List<Student> students;

    public Admin() throws IOException {
        daoFactory = DAOFactory.getInstance();
        courseDAO = daoFactory.getCourseDao();
        groupDAO = daoFactory.getGroupDAO();
        studentDAO = daoFactory.getStudentDAO();
        dataSourse = new DataSourse();
        courses = dataSourse.getCourses();
        groups = dataSourse.getGroups();
        students = new ArrayList<>(dataSourse.getStudents());
    }

    public void addStudent(String firstName, String lastName) throws DAOException, SQLException {
        Student student = new Student(firstName, lastName);
        students.add(student);
        assignGroup(student);
        studentDAO.create(student);
    }

    public void createCourses() throws DAOException, SQLException {
        for (int i = 0; i < courses.size(); i++) {
            courseDAO.create(courses.get(i));
        }

    }

    public void createGroups() throws DAOException {
        for (int i = 0; i < groups.size(); i++) {
            groupDAO.create(groups.get(i));
        }
    }

    public void createStudents() throws DAOException, SQLException {
        for (int i = 0; i < students.size(); i++) {
            assignGroup(students.get(i));
            studentDAO.create(students.get(i));
        }

    }

    private void assignGroup(Student student) {
        Random random = new Random();
        Group group = groups.get(random.nextInt(groups.size()));
        student.setGroup(group);
        group.getStudents().add(student);
    }

    public void assignCourses(Student student) {
        Random random = new Random();
        int minPerStudent = 1;
        int maxPerStudent = 3;
        int range = maxPerStudent - minPerStudent;
        for (int i = 0; i < (random.nextInt(range) + 1 + minPerStudent); i++) {
            student.setCourses(courses.get(random.nextInt(courses.size())));
        }
    }

}
