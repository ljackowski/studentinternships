package com.ljackowski.studentinternships.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage1(){
        return "login";
    }

    @GetMapping("/")
    public String loginPage2(){
        return "login";
    }
}
