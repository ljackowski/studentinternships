package com.ljackowski.studentinternships.user;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    int insertUser(User user);

    List<User> selectAllUsers();

    Optional<User> selectUserById(String userId);

    int deleteUserById(String userId);

    int updateUserById(String userId, User user );

}
