package com.ljackowski.studentinternships.employer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository("EmployerDao")
@Transactional
public class EmployerDataAccessService implements EmployerDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    class EmployerRowMapper implements RowMapper<Employer>{
        @Override
        public Employer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Employer employer = new Employer();
            employer.setEmployerId(resultSet.getInt("employer_id"));
            employer.setUserId(resultSet.getInt("user_id"));
            employer.setFirstName(resultSet.getString("first_name"));
            employer.setLastName(resultSet.getString("last_name"));
            employer.setEmail(resultSet.getString("email"));
            employer.setTelephoneNumber(resultSet.getInt("telephone_number"));
            employer.setCompanyId(resultSet.getInt("company_id"));
            return employer;
        }
    }

    @Override
    public int insertEmploer(Employer employer) {
        String sql = "INSERT INTO employer (employer_id, user_id, first_name, last_name, email, telephone_number, company_id) VALUES (?,?,?,?,?,?,?) ";
        return jdbcTemplate.update(sql,
                employer.getEmployerId(),
                employer.getUserId(),
                employer.getFirstName(),
                employer.getLastName(),
                employer.getEmail(),
                employer.getTelephoneNumber(),
                employer.getCompanyId());
    }

    @Override
    public List<Employer> selectAllEmployers() {
        String sql = "SELECT * FROM employer";
        List<Employer> employerList = jdbcTemplate.query(sql, new EmployerRowMapper());
        return employerList;
    }

    @Override
    public Optional<Employer> selectEmployerById(int employerId) {
        String sql = "SELECT * FROM employer WHERE employer_id=?";
        Optional<Employer> employerById = Optional.of(jdbcTemplate.queryForObject(sql, new EmployerRowMapper(), employerId));
        return employerById;
    }

    @Override
    public int deleteEmployerById(int employerId) {
        String sql = "DELETE FROM employer WHERE employer_id=?";
        jdbcTemplate.update(sql, employerId);
        return 0;
    }

    @Override
    public int updateEmployerById(int employerId, Employer employerToUpdate) {
        String sql = "UPDATE employer SET first_name=? WHERE employer_id=?";
        jdbcTemplate.update(sql, employerToUpdate.getFirstName(), employerId);
        return 0;
    }
}
