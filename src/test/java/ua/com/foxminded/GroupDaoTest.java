package ua.com.foxminded;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ua.com.foxminded.dao.CourseDao;
import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.GroupDao;
import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Group;
import ua.com.foxminded.model.Student;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupDaoTest extends DataSourceBasedDBTestCase {

	private ConnectionProvider connectionProvider;
	private GroupDao groupDao;
	private Student student1;
	private Student student2;
	private Student student3;
	private Student student4;
	private Student student5;
	private Student student6;
	private Group group1;
	private Group group2;

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
		try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("groups.xml")) {
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
	public void setUp() {
		connectionProvider = new ConnectionProvider("db.properties");
		groupDao = new GroupDao(connectionProvider);
		student3 = new Student("John", "Sting");
		student4 = new Student("Robert", "Martin");
		student5 = new Student("Katy", "Miller");
		student6 = new Student("Anna", "Gray");
		group1 = new Group("MH-12");
		group2 = new Group("LR-48");
	}

	@AfterAll
	public void after() throws Exception {
		super.tearDown();
	}

	@Test
	public void create_ShouldCreateNewGroup() throws Exception {
		IDataSet expectedDataSet = getDataSet();
		ITable expectedTable = expectedDataSet.getTable("groups");

		groupDao.create(group1);
		groupDao.create(group2);

		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("groups");
		Assertion.assertEquals(expectedTable, actualTable);
	}

	@Test
	public void findGroupsByStudentsCount_shouldReturnGroupsWithLessOrEqualsStudentCount()
			throws DaoException, SQLException {
		student3.setGroup(group2);
		student4.setGroup(group1);
		student5.setGroup(group1);
		student6.setGroup(group1);

		StudentDao studentDao = new StudentDao(connectionProvider);
		studentDao.create(student3);
		studentDao.create(student4);
		studentDao.create(student5);
		studentDao.create(student6);
		List<Group> expected = new ArrayList<>();
		expected.add(group2);

		List<Group> actual = groupDao.findGroupsByStudentsCount(3);

		assertEquals(expected, actual);

	}

}
