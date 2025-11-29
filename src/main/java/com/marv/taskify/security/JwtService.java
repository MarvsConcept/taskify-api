package com.marv.taskify.security;

import com.marv.taskify.domain.entities.User;

public interface JwtService {

    String generateToken(User user);

    String extractEmail(String token);

    String extractUserId(String token);

//    String extractUserId(String token);
//    String extractRole(String token);
}
