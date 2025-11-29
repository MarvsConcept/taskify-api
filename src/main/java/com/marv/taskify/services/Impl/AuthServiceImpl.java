package com.marv.taskify.services.Impl;

import com.marv.taskify.domain.Role;
import com.marv.taskify.domain.dtos.AuthResponseDto;
import com.marv.taskify.domain.dtos.LoginRequestDto;
import com.marv.taskify.domain.dtos.RegisterRequestDto;
import com.marv.taskify.domain.entities.User;
import com.marv.taskify.repositories.UserRepository;
import com.marv.taskify.security.CustomUserDetails;
import com.marv.taskify.security.JwtService;
import com.marv.taskify.services.AuthService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDto register(RegisterRequestDto dto) {

        // Check if email already exists
        if (userRepository.existsByEmail(dto.getEmail())){
                throw new  IllegalArgumentException("Email already in use");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already in use");
        }

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(hashedPassword)
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return AuthResponseDto.builder()
                .token(token)
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    @Override
    public AuthResponseDto login(LoginRequestDto dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        String token = jwtService.generateToken(user);

        return AuthResponseDto.builder()
                .token(token)
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();

    }

}
