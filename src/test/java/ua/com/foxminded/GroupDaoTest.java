package ua.com.foxminded;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.GroupDao;
import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Group;
import ua.com.foxminded.model.Student;

class GroupDaoTest extends DataSourceBasedDBTestCase {

    private ConnectionProvider connectionProvider;
    private GroupDao groupDao;

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
    public void setUp() {
        connectionProvider = new ConnectionProvider("db.properties");
        groupDao = new GroupDao(connectionProvider);
    }

    @AfterEach
    public void after() throws DaoException {
        SqlScript sqlScript = new SqlScript();
        sqlScript.executeScript("deleteSchema.sql");
    }

    @Test
    public void create_ShouldCreateNewGroup() throws Exception {
        IDataSet expectedDataSet = getDataSet();
        ITable expectedTable = expectedDataSet.getTable("groups");

        groupDao.create(new Group("MH-12"));

        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("groups");
        Assertion.assertEquals(expectedTable, actualTable);
    }

}
