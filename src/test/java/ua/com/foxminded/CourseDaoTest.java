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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.model.Course;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CourseDaoTest extends DataSourceBasedDBTestCase {

	private ConnectionProvider connectionProvider;
	private CourseDao courseDao;
	private Course course;

	@Override
	protected DataSource getDataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL(
				"jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;init=runscript from 'classpath:schemaTest.sql'");
		dataSource.setUser("sa");
		dataSource.setPassword("sa");
		return dataSource;
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("courses.xml")) {
			return new FlatXmlDataSetBuilder().build(resourceAsStream);
		}
	}

	@Override
	protected DatabaseOperation getSetUpOperation() {
		return DatabaseOperation.REFRESH;
	}

	@Override
	protected DatabaseOperation getTearDownOperation() {
		return DatabaseOperation.DELETE_ALL;
	}

	@BeforeAll
	public void setUp() throws Exception {
		connectionProvider = new ConnectionProvider("db.properties");
		courseDao = new CourseDao(connectionProvider);
		course = new Course("History", "course of History");
	}

	@AfterAll
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void create_ShouldCreateNewCourseIntoDatabase() throws Exception {
		IDataSet expectedDataSet = getDataSet();
		ITable expectedTable = expectedDataSet.getTable("courses");

		courseDao.create(course);

		ITable actualData = getConnection().createQueryTable("result",
				"SELECT*FROM COURSES WHERE course_name='History'");

		String[] ignore = { "course_id" };
		Assertion.assertEqualsIgnoreCols(expectedTable, actualData, ignore);
	}

	@Test
	public void read_ShouldReturnCourseFromDatabase() throws DaoException {
		Course expectedCourse = course;

		Course actualCourse = courseDao.read(1);

		assertEquals(expectedCourse, actualCourse);
	}

}
