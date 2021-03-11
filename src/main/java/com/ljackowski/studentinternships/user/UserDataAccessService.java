package com.ljackowski.studentinternships.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("UserDao")
@Transactional
public class UserDataAccessService implements UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(resultSet.getInt("user_id"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setRole(resultSet.getString("role"));
            return user;
        }
    }

    @Override
    public int insertUser(User user) {
        String sql = "INSERT INTO users(email, password, role) VALUES (?,?,?) RETURNING user_id";
        int user_id = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPassword(),
                user.getRole());
        return user_id;
    }

    @Override
    public List<User> selectAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public User selectUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id=?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), userId);
    }

    @Override
    public void deleteUserById(int userId) {
        String sql = "DELETE FROM users WHERE user_id=?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public void updateUserById(int userId, User user) {
        String sql = "UPDATE users SET email=?, password=?, role=? WHERE user_id=?";
        jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getRole(), userId);
    }
}
