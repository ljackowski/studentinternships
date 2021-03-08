package com.ljackowski.studentinternships.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping("/delete")
    public String deleteUser(@RequestParam(name="userId") String userId){
        userService.deleteUserById(userId);
        return "redirect:/users/list";
    }

}
