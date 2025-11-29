package com.marv.taskify.services.Impl;

import com.marv.taskify.domain.entities.User;
import com.marv.taskify.security.CustomUserDetails;
import com.marv.taskify.services.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {
    @Override
    public User getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getPrincipal().equals("anonymousUser")) {
            throw new IllegalArgumentException("No authenticated user found");
        }

        CustomUserDetails userDetails = (CustomUserDetails)  auth.getPrincipal();
        return userDetails.getUser();
    }

    @Override
    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
