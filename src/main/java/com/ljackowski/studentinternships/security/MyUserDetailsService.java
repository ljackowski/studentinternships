package com.ljackowski.studentinternships.security;

import com.ljackowski.studentinternships.models.User;
import com.ljackowski.studentinternships.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final StudentService studentService;
    private final InternService internService;
    private final CoordinatorService coordinatorService;

    @Autowired
    public MyUserDetailsService(UserService userService, StudentService studentService, InternService internService, CoordinatorService coordinatorService, CompanyService companyService) {
        this.userService = userService;
        this.studentService = studentService;
        this.internService = internService;
        this.coordinatorService = coordinatorService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);
        if (user.getRole().equals("ROLE_STUDENT")) {
            return new MyUserDetails(studentService.getStudentByEmail(email));
        } else if (user.getRole().equals("ROLE_INTERN")) {
            return new MyUserDetails(internService.getInternByStudent(studentService.getStudentByEmail(email)));
        }else if (user.getRole().equals("ROLE_COORDINATOR")) {
            return new MyUserDetails(coordinatorService.getCoordinatorByEmail(email));
        } else {
            return new MyUserDetails(userService.getUserByEmail(email));
        }

    }
}
