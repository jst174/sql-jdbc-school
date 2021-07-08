package ua.com.foxminded;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.dao.ConnectionProvider;
import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Student;

class CourseDaoTest {

    @BeforeEach
    void generateTestData() throws DaoException {
        SqlScript sqlScript = new SqlScript();
        sqlScript.executeScript("schemaTest.sql");
    }

    @Test
    public void create_ShouldAddNewCourseIntoDataBase() throws Exception {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        CourseDao courseDao = new CourseDao(connectionProvider);
        Course expected = new Course("History", "course of History");
        courseDao.create(expected);

        Course actual = courseDao.read("History");
       
        assertEquals(expected, actual);

    }

}
