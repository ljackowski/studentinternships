package com.ljackowski.studentinternships.coordinator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository("CoordinatorDao")
@Transactional
public class CoordinatorDataAccessService implements CoordinatorDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    class CoordinatorRowMapper implements RowMapper<Coordinator>{
        @Override
        public Coordinator mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Coordinator coordinator = new Coordinator();
            coordinator.setCoordinatorId(resultSet.getInt("coordinator_id"));
            coordinator.setUserId(resultSet.getInt("user_id"));
            coordinator.setFirstName(resultSet.getString("first_name"));
            coordinator.setLastName(resultSet.getString("last_name"));
            coordinator.setEmail(resultSet.getString("email"));
            coordinator.setTelephoneNumber(resultSet.getInt("telephone_number"));
            coordinator.setFieldOfStudy(resultSet.getString("field_of_study"));
            return coordinator;
        }
    }

    @Override
    public int insertCoordinator(Coordinator coordinator){
        String sql = "INSERT INTO coordinator (coordinator_id, user_id, first_name, last_name, email, telephone_number, field_of_study) VALUES(?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                coordinator.getCoordinatorId(),
                coordinator.getUserId(),
                coordinator.getFirstName(),
                coordinator.getLastName(),
                coordinator.getEmail(),
                coordinator.getTelephoneNumber(),
                coordinator.getFieldOfStudy());
    }

    @Override
    public List<Coordinator> selectAllCoordinators(){
        String sql = "SELECT * FROM  coordinator";
        List<Coordinator> coordinatorList = jdbcTemplate.query(sql, new CoordinatorRowMapper());
        return coordinatorList;
    }

    @Override
    public Optional<Coordinator> selectCoordinatorById(int coordinatorId) {
        String sql = "SELECT * FROM coordinator WHERE coordinator_id=?";
        Optional<Coordinator> coordinatorById = Optional.of(jdbcTemplate.queryForObject(sql, new CoordinatorRowMapper(), coordinatorId));
        return coordinatorById;
    }

    @Override
    public int deleteCoordinatorById(int coordinatorId) {
        String sql = "DELETE FROM coordinator WHERE coordinator_id=?";
        jdbcTemplate.update(sql,coordinatorId);
        return 0;
    }

    @Override
    public int updateCoordinatorById(int coordinatorId, Coordinator coordinatorToUpdate) {
        String sql = "UPDATE coordinator SET first_name=? WHERE coordinator_id=?";
        jdbcTemplate.update(sql, coordinatorToUpdate.getFirstName(), coordinatorId);
        return 0;
    }
}
