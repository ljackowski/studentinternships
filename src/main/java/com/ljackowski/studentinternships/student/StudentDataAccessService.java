package com.ljackowski.studentinternships.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository("StudentDao")
@Transactional
public class StudentDataAccessService implements StudentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    class StudentRowMapper implements RowMapper<Student> {

        @Override
        public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Student student = new Student();
            student.setUserId(resultSet.getInt("user_id"));
            student.setFirstName(resultSet.getString("first_name"));
            student.setLastName(resultSet.getString("last_name"));
            student.setEmail(resultSet.getString("email"));
            student.setTelephoneNumber(resultSet.getString("telephone_number"));
            student.setStudentIndex(resultSet.getInt("student_index"));
            student.setFieldOfStudy(resultSet.getString("field_of_study"));
            student.setDegree(resultSet.getString("degree"));
            student.setCoordinatorId(resultSet.getInt("coordinator_id"));
            student.setEmployerId(resultSet.getInt("employer_id"));
            student.setAverageGrade(resultSet.getDouble("average_grade"));
            return student;
        }
    }

    @Override
    public int insertStudent(Student student, int user_id) {
        String sql = "INSERT INTO student (user_id, first_name, last_name, email, telephone_number, student_index, field_of_study, degree, coordinator_id, employer_id, average_grade) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                user_id,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getTelephoneNumber(),
                student.getStudentIndex(),
                student.getFieldOfStudy(),
                student.getDegree(),
                student.getCoordinatorId(),
                student.getEmployerId(),
                student.getAverageGrade());
    }

    @Override
    public List<Student> selectAllStudents() {
        String sql = "SELECT * FROM student";
        List<Student> studentsList = jdbcTemplate.query(sql, new StudentRowMapper());
        return studentsList;
    }

    @Override
    public Optional<Student> selectStudentByIndex(int studentIndex) {
        String sql = "SELECT * FROM student WHERE student_index=?";
        Optional<Student> studentByIndex = Optional.ofNullable(jdbcTemplate.queryForObject(sql, new StudentRowMapper(), studentIndex));
        return studentByIndex;
    }

    @Override
    public int deleteStudentById(int nrIndeksu) {
        String sql = "DELETE FROM student WHERE nrIndeksu = ?";
        jdbcTemplate.update(sql, nrIndeksu);
        return 0;
    }

    @Override
    public int updateStudentById(int nrIndeksu, Student studentToUpdate) {
        String sql = "UPDATE student SET imie = ? WHERE id = ?";
        jdbcTemplate.update(sql, studentToUpdate.getFirstName(), nrIndeksu);
        return 0;
    }

}
