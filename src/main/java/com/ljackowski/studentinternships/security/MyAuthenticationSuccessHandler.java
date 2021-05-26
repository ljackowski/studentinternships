package com.ljackowski.studentinternships.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Configuration
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/usersList");
        } else if (roles.contains("ROLE_STUDENT")){
            response.sendRedirect("/student/" + getUserId());
        }
        else if (roles.contains("ROLE_INTERN")){
            response.sendRedirect("/intern/" + getUserId());
        }
        else if (roles.contains("ROLE_COORDINATOR")){
            response.sendRedirect("/coordinator/" + getUserId());
        }
    }

    public long getUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        long userId = 0;
        if (authentication != null){
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            userId = userDetails.getUserId();
        }
        return userId;
    }
}
