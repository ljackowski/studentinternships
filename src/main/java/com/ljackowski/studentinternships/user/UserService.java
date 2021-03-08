package com.ljackowski.studentinternships.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<User> getUserById(String userId){
        return userDao.selectUserById(userId);
    }

    public int deleteUserById(String userId){
        return userDao.deleteUserById(userId);
    }

    public int updateUserById(String userId, User newUser){
        return userDao.updateUserById(userId, newUser);
    }
}
