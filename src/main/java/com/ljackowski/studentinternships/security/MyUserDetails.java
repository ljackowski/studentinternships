package com.ljackowski.studentinternships.security;

import com.ljackowski.studentinternships.models.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {
    private String email;
    private String password;
    private List<GrantedAuthority> authorityList;
    private long userId;
    private Company company;
    private String fieldOfStudy;

    public MyUserDetails() {
    }

    public MyUserDetails(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorityList = Arrays.stream(user.getRole().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        this.userId = user.getUserId();
    }

    public MyUserDetails(Student studentUser) {
        this.email = studentUser.getEmail();
        this.password = studentUser.getPassword();
        this.authorityList = Arrays.stream(studentUser.getRole().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        this.userId = studentUser.getUserId();
        this.company = studentUser.getCompany();
    }

    public MyUserDetails(Intern internUser) {
        this.email = internUser.getStudent().getEmail();
        this.password = internUser.getStudent().getPassword();
        this.authorityList = Arrays.stream(internUser.getStudent().getRole().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        this.userId = internUser.getStudent().getUserId();
        this.company = internUser.getCompany();
    }

    public MyUserDetails(Coordinator coordinatorUser) {
        this.email = coordinatorUser.getEmail();
        this.password = coordinatorUser.getPassword();
        this.authorityList = Arrays.stream(coordinatorUser.getRole().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        this.fieldOfStudy = coordinatorUser.getFieldOfStudy();
        this.userId = coordinatorUser.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public long getUserId() {
        return userId;
    }

    public Company getCompany(){
        return company;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }
}
