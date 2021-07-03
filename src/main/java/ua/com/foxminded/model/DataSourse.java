package ua.com.foxminded.model;

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
import ua.com.foxminded.dao.DaoFactory;
import ua.com.foxminded.dao.GroupDao;
import ua.com.foxminded.dao.StudentDao;

public class DataSourse {

    private static final char HYPHEN = '-';

    public List<Group> getGroups() {
        List<Group> groups = Arrays.asList(new Group[10]);
        return groups.stream().map(s -> new Group(generateGroupName())).collect(Collectors.toList());
    }

    public List<Course> getCourses() throws IOException {
        try (Stream<String> coursesStream = Files.lines(Paths.get(getFile("courses.txt").getAbsolutePath()))) {
            return coursesStream.map(s -> new Course(s, "course of " + s)).collect(Collectors.toList());
        }
    }

    public Set<Student> getStudents() throws IOException {
        Set<Student> students = new HashSet<>();
        List<String> firstNames = getFirstNames();
        List<String> lastNames = getLastNames();
        Random random = new Random();
        while (students.size() != 200) {
            students.add(new Student(firstNames.get(random.nextInt(firstNames.size())),
                    lastNames.get(random.nextInt(lastNames.size()))));
        }
        return students;
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

    private List<String> getLastNames() throws IOException {
        try (Stream<String> lastNamesStream = Files.lines(Paths.get(getFile("lastName.txt").getAbsolutePath()))) {
            return lastNamesStream.collect(Collectors.toList());
        }
    }

    private List<String> getFirstNames() throws IOException {
        try (Stream<String> firstNamesStream = Files.lines(Paths.get(getFile("firstName.txt").getAbsolutePath()))) {
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
