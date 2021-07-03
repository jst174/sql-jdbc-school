package ua.com.foxminded.model;

import static java.lang.System.lineSeparator;

import java.io.IOException;
import java.util.Scanner;

import ua.com.foxminded.dao.DaoException;

public class Menu {

    public void getMenu(Admin admin) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            createItems();
            System.out.println("choose item");
            String input = scanner.nextLine();
            while (true) {
                if (input.equals("quit")) {
                    break;
                }
                if (input.equals("a")) {
                    System.out.println("enter the number of students");
                    int number = scanner.nextInt();
                    for (Group group : admin.findGroups(number)) {
                        System.out.println(group.getName());
                    }
                }
                if (input.equals("b")) {
                    System.out.println("enter of course name");
                    String courseName = scanner.nextLine();
                    for (Student student : admin.findStudentsFromCourse(courseName)) {
                        System.out.println(student.getFirstName() + " " + student.getLastName());
                    }
                }
                if (input.equals("c")) {
                    System.out.println("enter first name");
                    String firstName = scanner.nextLine();
                    System.out.println("enter last name");
                    String lastName = scanner.nextLine();
                    admin.addStudent(firstName, lastName);
                }
                if (input.equals("d")) {
                    System.out.println("enter student's id");
                    int id = scanner.nextInt();
                    admin.deleteStudent(id);
                }
                if (input.equals("e")) {
                    createCoursesItems();
                    String courseName = scanner.nextLine();
                    System.out.println("enter student's id");
                    int id = scanner.nextInt();
                    admin.addStudentToCourse(courseName, id);
                }
                if (input.equals("f")) {
                    System.out.println("enter name of course");
                    String courseName = scanner.nextLine();
                    System.out.println("enter student's id");
                    int id = scanner.nextInt();
                    admin.removeStudentFromCourse(id, courseName);
                }
                input = scanner.nextLine();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
