CREATE TABLE IF NOT EXISTS groups(
group_id INTEGER NOT NULL AUTO_INCREMENT,
group_name VARCHAR(5),
PRIMARY KEY(group_id));

CREATE TABLE IF NOT EXISTS students(
student_id INTEGER NOT NULL AUTO_INCREMENT,
group_id INTEGER REFERENCES groups(group_id) ON UPDATE CASCADE ON DELETE CASCADE,
first_name VARCHAR(50),
last_name VARCHAR(50),
PRIMARY KEY(student_id));

CREATE TABLE IF NOT EXISTS COURSES(
COURSE_ID INTEGER NOT NULL AUTO_INCREMENT,
COURSE_NAME VARCHAR(50),
COURSE_DESCRIPTION VARCHAR(255),
PRIMARY KEY(COURSE_ID));


CREATE TABLE IF NOT EXISTS student_courses(
student_id INTEGER REFERENCES students(student_id) ON UPDATE CASCADE ON DELETE CASCADE,
course_id INTEGER REFERENCES courses(course_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE (student_id, course_id));