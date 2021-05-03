package com.ljackowski.studentinternships.services;

import com.ljackowski.studentinternships.models.User;
import com.ljackowski.studentinternships.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserService")
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(long userId){
        return userRepository.findById(userId).get();
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public void deleteUserById(long userId){
        userRepository.deleteById(userId);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
