package com.ljackowski.studentinternships.user;


import com.ljackowski.studentinternships.coordinator.Coordinator;
import com.ljackowski.studentinternships.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository("UserDao")
@Transactional
public class UserDataAccessService implements UserDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(resultSet.getString("user_id"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setRole(resultSet.getString("role"));
            return user;
        }
    }

    @Override
    public int insertUser(User user) {
        String sql = "INSERT INTO users(user_id, email, password, role) VALUES (?,?,?,?)";
        return jdbcTemplate.update(sql,
                user.getUserId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }

    @Override
    public List<User> selectAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> coordinatorList = jdbcTemplate.query(sql, new UserRowMapper());
        return coordinatorList;
    }

    @Override
    public Optional<User> selectUserById(String userId) {
        return Optional.empty();
    }

    @Override
    public int deleteUserById(String userId) {
        String sql = "DELETE FROM users WHERE user_id=?";
        jdbcTemplate.update(sql, userId);
        return 0;
    }

    @Override
    public int updateUserById(String userId, User user) {
        return 0;
    }
}
