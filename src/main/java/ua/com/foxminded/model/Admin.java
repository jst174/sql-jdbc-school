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
import ua.com.foxminded.dao.StudentDao;

public class Admin {
    private DaoFactory daoFactory;
    private CourseDao courseDAO;
    private GroupDao groupDAO;
    private StudentDao studentDAO;
    private DataSourse dataSourse;
    private List<Course> courses;
    private List<Group> groups;
    private List<Student> students;

    public Admin() throws IOException {
        daoFactory = new DaoFactory();
        courseDAO = daoFactory.getCourseDao();
        groupDAO = daoFactory.getGroupDAO();
        studentDAO = daoFactory.getStudentDAO();
        dataSourse = new DataSourse();
        courses = dataSourse.getCourses("courses.txt");
        groups = dataSourse.getGroups();
        students = new ArrayList<>(dataSourse.getStudents("firstName.txt", "lastName.txt"));
    }

    public void addStudent(String firstName, String lastName) throws DaoException, SQLException {
        Student student = new Student(firstName, lastName);
        studentDAO.create(student);
    }

    public void deleteStudent(int id) throws DaoException {
        studentDAO.delete(id);
    }

    public void addStudentToCourse(int courseId, int studentId) throws DaoException, SQLException {
        Student student = studentDAO.read(studentId);
        Course course = courseDAO.read(courseId);
        studentDAO.createStudentCourses(student, course);
    }

    public void fillCoursesTable() throws DaoException, SQLException {
        for (int i = 0; i < courses.size(); i++) {
            courseDAO.create(courses.get(i));
        }

    }

    public void fillGroupsTable() throws DaoException {
        for (int i = 0; i < groups.size(); i++) {
            groupDAO.create(groups.get(i));
        }
    }

    public void fillStudentsTable() throws DaoException, SQLException {
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            studentDAO.create(student);
            assignGroup(student);
            assignCourses(student);
            List<Course> courses = new ArrayList<>(students.get(i).getCourses());
            for (int j = 0; j < courses.size(); j++) {
                studentDAO.createStudentCourses(students.get(i), courses.get(j));
            }

        }

    }

    public void removeStudentFromCourse(int student_id, String courseName) throws DaoException, SQLException {
        int courseId = getCourseId(courseName);
        Student student = studentDAO.read(student_id);
        Course course = courseDAO.read(courseId);
        courseDAO.deleteStudent(student, course);
    }

    public List<Student> findStudentsFromCourse(String courseName) throws DaoException, SQLException {
        int courseId = getCourseId(courseName);
        List<Integer> studentId = studentDAO.getStudentsId(courseDAO.read(courseId));
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < studentId.size(); i++) {
            students.add(studentDAO.read(studentId.get(i)));
        }
        return students;
    }

    public List<Group> findGroups(int count) throws DaoException {
        return groupDAO.read(count);
    }

    private void assignGroup(Student student) throws DaoException {
        Random random = new Random();
        Group group = groups.get(random.nextInt(groups.size()));
        student.setGroup(group);
        group.getStudents().add(student);
        studentDAO.assignGroup(student);
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

    private int getCourseId(String courseName) {
        int courseId = 0;
        for (Course course : courses) {
            if (course.getName().equals(courseName)) {
                courseId = course.getId();
            }
        }
        if (courseId == 0) {
            throw new IllegalArgumentException("id cannot be equal to 0");
        }
        return courseId;
    }

}
