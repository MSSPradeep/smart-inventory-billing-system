package com.firstproject.smartinventory.security;

import com.firstproject.smartinventory.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public User getCurrUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new RuntimeException("No authentication user found");


        Object principle = authentication.getPrincipal();

        if (!(principle instanceof CustomeUserDetails userDetails))
            throw new RuntimeException("Invalid Authentication Details");

        return userDetails.getUser();
    }

    public String getCurrentUserId(){
        return getCurrUser().getId();
    }
}
