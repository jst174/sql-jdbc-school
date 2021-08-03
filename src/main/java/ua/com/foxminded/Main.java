package ua.com.foxminded;

import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.dao.GroupDao;
import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

public class Main {

    public static void main(String[] args) throws Exception {
        SqlScript sqlScript = new SqlScript();
        sqlScript.executeScript("schema.sql");
        ConnectionProvider connectionProvider = new ConnectionProvider("db.properties");
        GroupDao groupDao = new GroupDao(connectionProvider);
        StudentDao studentDao = new StudentDao(connectionProvider);
        CourseDao courseDao = new CourseDao(connectionProvider);
        DataSource dataSourse = new DataSource(studentDao, courseDao, groupDao);
        StudentService studentService = new StudentService(studentDao, courseDao);
        GroupService groupService = new GroupService(groupDao);
        CourseService courseService = new CourseService(courseDao, studentDao);
        Menu menu = new Menu();
        dataSourse.fillCoursesTable();
        dataSourse.fillGroupsTable();
        dataSourse.fillStudentsTable();
        menu.getMenu(studentService, courseService, groupService);

    }

}
