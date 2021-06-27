package ua.com.foxminded.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Group {

    private int id;
    private String name;
    private List<Student> students = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Group [id=" + id + ", name=" + name + "]";
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
