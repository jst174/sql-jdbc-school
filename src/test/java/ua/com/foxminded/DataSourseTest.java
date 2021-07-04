package ua.com.foxminded;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.DataSourse;
import ua.com.foxminded.model.Group;
import ua.com.foxminded.model.Student;

class DataSourseTest {

    private DataSourse dataSourse;

    @BeforeEach
    void setUp() {
        dataSourse = new DataSourse();
    }

    @Test
    void getCourses_ShouldReturnListWithCorses() throws IOException {
        dataSourse = new DataSourse();
        List<Course> expected = new ArrayList<>();
        expected.add(new Course("History", "course of History"));
        expected.add(new Course("Music", "course of Music"));
        expected.add(new Course("Math", "course of Math"));

        assertEquals(expected, dataSourse.getCourses("coursesTest.txt"));
    }

    @Test
    void getCourses_argumentIsNull_ShouldThrowException() {
        assertThrows(RuntimeException.class, () -> dataSourse.getCourses(null));
    }

    @Test
    void getGroups_groupNameShouldMatchesWithPattern() {

        List<Group> groups = dataSourse.getGroups();

        String groupName = groups.get(0).getName();
        assertTrue(groupName.matches("[A-Z][A-Z]-\\d\\d"));

    }

    @Test
    void getStudents_StudentNameShouldMatchesPattern() throws IOException {

        List<Student> students = new ArrayList<>(dataSourse.getStudents("firstNameTest.txt", "lastNameTest.txt"));

        String fullName = students.get(0).getFirstName() + " " + students.get(0).getLastName();
        assertTrue(fullName.matches("[A-Z][a-z]*\\s[A-Z][a-z]*"));
    }

    @Test
    void getStudent_NumberOfStudentsShouldBe200() throws IOException {
        assertTrue(dataSourse.getStudents("firstNameTest.txt", "lastNameTest.txt").size() == 200);
    }

    @Test
    void getStudent_firstNameIsNull_ShoulThrowException() {
        assertThrows(RuntimeException.class, () -> dataSourse.getStudents(null, "lastNameTest.txt"));
    }

    @Test
    void getStudent_lastNameIsNull_ShoulThrowException() {
        assertThrows(RuntimeException.class, () -> dataSourse.getStudents("firstNameTest.txt", null));
    }

    @Test
    void getStudent_lastNameAndFirstNameIsNull_ShoulThrowException() {
        assertThrows(RuntimeException.class, () -> dataSourse.getStudents(null, null));
    }

}
