package ua.com.foxminded.model;

import java.util.HashSet;
import java.util.Set;

import ua.com.foxminded.dao.ConnectionProvider;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.StudentDao;

public class Student {

    private int id;
    private Group group;
    private String firstName;
    private String lastName;
    private Set<Course> courses = new HashSet<>();
    private ConnectionProvider connectionProvider;

    public Student() {
        connectionProvider = new ConnectionProvider();
    }

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

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

    @Override
    public String toString() {
        return "Student [group=" + group + ", firstName=" + firstName + ", lastName=" + lastName + ", courses="
                + courses + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        return true;
    }

    public void setCourses(Course course) {
        this.courses.add(course);
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
