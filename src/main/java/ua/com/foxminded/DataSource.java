package ua.com.foxminded;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.GroupDao;
import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Group;
import ua.com.foxminded.model.Student;

public class DataSource {

    private static final char HYPHEN = '-';

    private StudentDao studentDao;
    private CourseDao courseDao;
    private GroupDao groupDao;
    private List<Course> courses;
    private List<Student> students;
    private List<Group> groups;

    public DataSource(StudentDao studentDao, CourseDao courseDao, GroupDao groupDao) throws IOException {
        this.studentDao = studentDao;
        this.groupDao = groupDao;
        this.courseDao = courseDao;
        courses = getCourses("courses.txt");
        students = new ArrayList<>(getStudents("firstName.txt", "lastName.txt"));
        groups = getGroups();
    }

    public void fillStudentsTable() throws DaoException, IOException {

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            assignCourses(student);
            studentDao.create(student);
            assignGroup(student);
        }

    }

    public void fillCoursesTable() throws DaoException {
        for (int i = 0; i < courses.size(); i++) {
            courseDao.create(courses.get(i));
        }

    }

    public void fillGroupsTable() throws DaoException {
        for (int i = 0; i < groups.size(); i++) {
            groupDao.create(groups.get(i));
        }
    }

    public void assignGroup(Student student) throws DaoException {
        Random random = new Random();
        Group group = groups.get(random.nextInt(groups.size()));
        student.setGroup(group);
        group.getStudents().add(student);
        studentDao.assignGroup(student);
    }

    private void assignCourses(Student student) throws IOException {
        Random random = new Random();
        int minPerStudent = 1;
        int maxPerStudent = 3;
        int range = maxPerStudent - minPerStudent;
        for (int i = 0; i < (random.nextInt(range) + 1 + minPerStudent); i++) {
            Course course = courses.get(random.nextInt(courses.size()));
            student.setCourses(course);
            course.setStudents(student);
        }
    }

    private List<Group> getGroups() {
        List<Group> groupsList = Arrays.asList(new Group[10]);
        return groupsList.stream().map(s -> new Group(generateGroupName())).collect(Collectors.toList());
    }

    private List<Course> getCourses(String coursesFile) throws IOException {
        try (Stream<String> coursesStream = Files.lines(Paths.get(getFile(coursesFile).getAbsolutePath()))) {
            return coursesStream.map(s -> new Course(s, "course of " + s)).collect(Collectors.toList());
        }
    }

    private Set<Student> getStudents(String firstNameFile, String lastNameFile) throws IOException {
        Set<Student> studentsList = new HashSet<>();
        List<String> firstNames = getFirstNames(firstNameFile);
        List<String> lastNames = getLastNames(lastNameFile);
        Random random = new Random();
        while (studentsList.size() != 200) {
            studentsList.add(new Student(firstNames.get(random.nextInt(firstNames.size())),
                    lastNames.get(random.nextInt(lastNames.size()))));
        }
        return studentsList;
    }

    private String generateGroupName() {
        char nextChar;
        int nextInt;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            if ((i == 0) || (i == 1)) {
                nextChar = (char) (random.nextInt(26) + 65);
                sb.append(nextChar);
            } else if ((i == 3)) {
                nextInt = random.nextInt(90) + 10;
                sb.append(nextInt);
            } else {
                sb.append(HYPHEN);
            }
        }
        return sb.toString();
    }

    private List<String> getLastNames(String lastNameFile) throws IOException {
        try (Stream<String> lastNamesStream = Files.lines(Paths.get(getFile(lastNameFile).getAbsolutePath()))) {
            return lastNamesStream.collect(Collectors.toList());
        }
    }

    private List<String> getFirstNames(String firstNameFile) throws IOException {
        try (Stream<String> firstNamesStream = Files.lines(Paths.get(getFile(firstNameFile).getAbsolutePath()))) {
            return firstNamesStream.collect(Collectors.toList());
        }
    }

    private File getFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            File file = new File(classLoader.getResource(fileName).getFile());
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
