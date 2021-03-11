package com.ljackowski.studentinternships.user;

import java.util.List;

public interface UserDao {

    int insertUser(User user);

    List<User> selectAllUsers();

    User selectUserById(int userId);

    void deleteUserById(int userId);

    void updateUserById(int userId, User user );

}
