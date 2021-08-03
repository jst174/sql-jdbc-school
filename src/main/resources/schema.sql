DROP TABLE IF EXISTS groups CASCADE;
CREATE TABLE groups(
group_id SERIAL NOT NULL,
group_name VARCHAR(5),
PRIMARY KEY(group_id));

DROP TABLE IF EXISTS students CASCADE;
CREATE TABLE students(
student_id SERIAL NOT NULL,
group_id INTEGER REFERENCES groups(group_id) ON UPDATE CASCADE ON DELETE CASCADE,
first_name VARCHAR(50),
last_name VARCHAR(50),
PRIMARY KEY(student_id));

DROP TABLE IF EXISTS courses CASCADE;
CREATE TABLE courses(
course_id SERIAL NOT NULL,
course_name VARCHAR(50),
course_description VARCHAR(255),
PRIMARY KEY(course_id));


DROP TABLE IF EXISTS student_courses CASCADE;
CREATE TABLE student_courses(
student_id INTEGER REFERENCES students(student_id) ON UPDATE CASCADE ON DELETE CASCADE,
course_id INTEGER REFERENCES courses(course_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE (student_id, course_id));