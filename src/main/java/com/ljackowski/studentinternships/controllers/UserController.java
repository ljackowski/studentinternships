package com.ljackowski.studentinternships.controllers;

import com.ljackowski.studentinternships.models.User;
import com.ljackowski.studentinternships.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/list")
    public String usersList(Model model) {
        List<User> usersList = userService.getUsers();
        model.addAttribute("users", usersList);
        return "lists/usersList";
    }

    @RequestMapping("/getOne/{userId}")
    @ResponseBody
    public User getUserById(@PathVariable(name = "userId") Long userId){
        return userService.getUserById(userId);
    }

    @GetMapping("/addUser")
    public String addUserForm(Model model) {
        model.addAttribute("addUserForm", new User());
        return "forms/addUserForm";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute User user) {
        userService.addUser(user);
        return "redirect:/users/list";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam(name="userId") long userId){
        userService.deleteUserById(userId);
        return "redirect:/users/list";
    }

    @GetMapping("/edit/{userId}")
    public String updateUserForm(@PathVariable(name = "userId") int userId, Model model){
        User user = userService.getUserById(userId);
        model.addAttribute("updateUserForm", user);
        return "forms/editUserForm";
    }

    @PostMapping("/edit/{userId}")
    public String updateUser(@ModelAttribute User user){
        userService.updateUserById(user);
        return "redirect:/users/list";
    }

}
