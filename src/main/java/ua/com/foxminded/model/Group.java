package ua.com.foxminded.model;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private int id;
    private String name;
    private List<Student> students = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<Student> getStudents() {
        return students;
    }

}
