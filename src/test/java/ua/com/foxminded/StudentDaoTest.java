package ua.com.foxminded;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ua.com.foxminded.dao.DaoException;
import ua.com.foxminded.dao.GroupDao;
import ua.com.foxminded.dao.StudentDao;
import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Group;
import ua.com.foxminded.model.Student;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentDaoTest extends DataSourceBasedDBTestCase {

	private ConnectionProvider connectionProvider;
	private Student student1;
	private Student student2;
	private StudentDao studentDao;
	private Course course1;
	private Course course2;

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
		try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("data.xml")) {
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
		student1 = new Student("Alex", "Brown");
		student2 = new Student("Mike", "King");
		studentDao = new StudentDao(connectionProvider);
		course1 = new Course("Chemistry", "course of Chemistry");
		course1.setId(5);
		course2 = new Course("Music", "course of Music");
		course2.setId(2);
		group1 = new Group("MH-12");
		group2 = new Group("LR-48");
	}

	@AfterAll
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void create_ShouldCreateNewStudent() throws Exception {
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("students.xml")) {
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
			ITable expectedTable = expectedDataSet.getTable("students");

			studentDao.create(student1);
			studentDao.create(student2);

			IDataSet actualDataSet = getConnection().createDataSet();
			ITable actualTable = actualDataSet.getTable("students");
			String[] ignore = { "student_id", "group_id" };
			Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
		}
	}

	@Test
	public void read_ShouldReturnStudentFromDatabase() throws DaoException {
		Student expectedStudent = student1;

		Student actualStudent = studentDao.read(1);

		actualStudent.setCourses(course1);
		assertEquals(expectedStudent, actualStudent);
	}

	@Test
	public void delete_ShouldDeleteStudentFromDatabase() throws Exception {
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("student_delete.xml")) {

			studentDao.delete(2);

			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
			ITable expectedTable = expectedDataSet.getTable("students");
			IDataSet actualDataSet = getConnection().createDataSet();
			ITable actualTable = actualDataSet.getTable("students");
			String[] ignore = { "student_id", "group_id" };
			Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
		}
	}

	@Test
	public void addStudentToCourse_shouldAddStudentToCourse() throws Exception {
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("studentCourses.xml")) {
			student1.setCourses(course1);
			student2.setCourses(course2);

			studentDao.addStudentToCourse(student1);

			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
			ITable expectedTable = expectedDataSet.getTable("student_courses");

			ITable actualTable = getConnection().createQueryTable("result",
					"select*from student_courses where course_id='5'");
			String[] ignore = { "student_id" };

			Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
		}
	}

}
