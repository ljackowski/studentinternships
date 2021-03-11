package com.ljackowski.studentinternships.user;

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

    @GetMapping("/addUser")
    public String addUserForm(Model model) {
        model.addAttribute("addUserForm", new User());
        return "addUserForm";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute User user) {
        userService.addUser(user);
        return "redirect:/users/list";
    }

    @RequestMapping("/list")
    public String usersList(Model model) {
        List<User> usersList = userService.getAllUsers();
        model.addAttribute("users", usersList);
        return "usersList";
    }

    @RequestMapping("/getOne/{userId}")
    @ResponseBody
    public User getUserById(@PathVariable(name = "userId") int userId){
        return userService.getUserById(userId);
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam(name="userId") int userId){
        userService.deleteUserById(userId);
        return "redirect:/users/list";
    }

    @GetMapping("/edit/{userId}")
    public String updateUserForm(@PathVariable(name = "userId") int userId, Model model){
        User user = userService.getUserById(userId);
        model.addAttribute("updateUserForm", user);
        return "editUserForm";
    }

    @PostMapping("/edit/{userId}")
    public String updateUser(@ModelAttribute User user, @RequestParam(name = "userId") int userId){
        userService.updateUserById(userId, user);
        return "redirect:/users/list";
    }



}
