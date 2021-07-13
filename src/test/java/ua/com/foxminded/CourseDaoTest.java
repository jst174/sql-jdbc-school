package ua.com.foxminded;

import org.dbunit.Assertion;

import java.io.InputStream;

import javax.sql.DataSource;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.model.Course;

class CourseDaoTest extends DataSourceBasedDBTestCase {

    private ConnectionProvider connectionProvider;
    private CourseDao courseDao;

    @Override
    protected DataSource getDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(connectionProvider.getHost());
        dataSource.setUser(connectionProvider.getLogin());
        dataSource.setPassword(connectionProvider.getPassword());
        return dataSource;
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data.xml")) {
            return new FlatXmlDataSetBuilder().build(resourceAsStream);
        }
    }

    @BeforeEach
    public void setUp() throws DaoException {
        connectionProvider = new ConnectionProvider("db.properties");
        courseDao = new CourseDao(connectionProvider);
    }

    @AfterEach
    public void after() throws DaoException {
        SqlScript sqlScript = new SqlScript();
        sqlScript.executeScript("deleteSchema.sql");
    }

    @Test
    public void create_ShouldCreateNewCourse() throws Exception {
        IDataSet expectedDataSet = getDataSet();
        ITable expectedTable = expectedDataSet.getTable("COURSES");

        courseDao.create(new Course("History", "course of History"));

        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("COURSES");
        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Test
    public void read_ShouldReturnCourseFromDatabase() throws DaoException {
        Course expected = new Course("History", "course of History");
        courseDao.create(expected);

        Course actual = courseDao.read(1);

        assertEquals(expected, actual);
    }

    @Test
    public void create_CourseShouldHaveStudents() throws DaoException {
        courseDao.create(new Course("History", "course of History"));

        Course course = courseDao.read(1);

        assertNotNull(course.getStudents());
    }
}
