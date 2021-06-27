package ua.com.foxminded.model;

import ua.com.foxminded.dao.SqlScript;

public class Main {

    public static void main(String[] args) throws Exception {
        SqlScript sqlScript = new SqlScript();
        sqlScript.executeScript();

        Admin admin = new Admin();
        
        admin.createCourses();
        admin.createGroups();
        admin.createStudents();
        
        admin.addStudent("Robert", "Martin");
       
    }

}
