package ua.com.foxminded;

import static java.lang.System.lineSeparator;

import java.io.IOException;
import java.util.Scanner;

import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Group;
import ua.com.foxminded.model.Student;

public class Menu {

    public Menu() throws DaoException, IOException {
        DataSourse dataSourse = new DataSourse();
        dataSourse.fillCoursesTable();
        dataSourse.fillGroupsTable();
        dataSourse.fillStudentsTable();
    }

    public void getMenu() throws DaoException {
        Student students = new Student();
        Group groups = new Group();
        Course courses = new Course();
        Scanner scanner = new Scanner(System.in);
        createItems();
        System.out.println("choose item");
        String input = scanner.nextLine();
        while (!input.equals("quit")) {
            if (input.equals("a")) {
                System.out.println("enter the number of students");
                int number = scanner.nextInt();
                for (Group group : groups.findGroups(number)) {
                    System.out.println(group.getName());
                }
            } else if (input.equals("b")) {
                System.out.println("enter course's name");
                String courseName = scanner.nextLine();
                for (Student student : courses.findStudentsFromCourse(courseName)) {
                    System.out.println(student.getFirstName() + " " + student.getLastName());
                }
            } else if (input.equals("c")) {
                System.out.println("enter first name");
                String firstName = scanner.nextLine();
                System.out.println("enter last name");
                String lastName = scanner.nextLine();
                students.addStudent(firstName, lastName);
            } else if (input.equals("d")) {
                System.out.println("enter student's id");
                int id = scanner.nextInt();
                students.deleteStudent(id);
            } else if (input.equals("e")) {
                createCoursesItems();
                String courseName = scanner.nextLine();
                System.out.println("enter student's id");
                int studentId = scanner.nextInt();
                courses.addStudentToCourse(courseName, studentId);
            } else if (input.equals("f")) {
                System.out.println("enter course's name");
                String courseName = scanner.nextLine();
                System.out.println("enter student's id");
                int studentId = scanner.nextInt();
                courses.removeStudentFromCourse(studentId, courseName);
            }
            input = scanner.nextLine();

        }
        scanner.close();
    }

    private void createItems() {
        StringBuilder menu = new StringBuilder();
        menu.append("a. Find all groups with less or equals student count" + lineSeparator());
        menu.append("b. Find all students related to course with given name" + lineSeparator());
        menu.append("c. Add new student" + lineSeparator());
        menu.append("d. Delete student by SRUDENT_ID" + lineSeparator());
        menu.append("e. Add a student to the course (from list)" + lineSeparator());
        menu.append("f. Remove the student from one of his or her courses" + lineSeparator());
        System.out.println(menu.toString());
    }

    private void createCoursesItems() {
        StringBuilder courses = new StringBuilder();
        courses.append("list of courses: " + lineSeparator());
        courses.append("1. History" + lineSeparator());
        courses.append("2. Music" + lineSeparator());
        courses.append("3. Math" + lineSeparator());
        courses.append("4. Biology" + lineSeparator());
        courses.append("5. Chemistry" + lineSeparator());
        courses.append("6. Psychology" + lineSeparator());
        courses.append("7. Art" + lineSeparator());
        courses.append("8. Physics" + lineSeparator());
        courses.append("9. Sociology" + lineSeparator());
        courses.append("10. Economics" + lineSeparator());
        courses.append("enter name of course" + lineSeparator());
        System.out.println(courses.toString());
    }

}
