package com.ljackowski.studentinternships.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserService")
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(@Qualifier("UserDao") UserDao userDao){
        this.userDao = userDao;
    }

    public int addUser(User user){
        return userDao.insertUser(user);
    }

    public List<User> getAllUsers(){
        return userDao.selectAllUsers();
    }

    public User getUserById(int userId){
        return userDao.selectUserById(userId);
    }

    public void deleteUserById(int userId){
        userDao.deleteUserById(userId);
    }

    public void updateUserById(int userId, User newUser){
        userDao.updateUserById(userId, newUser);
    }
}
