package com.marv.taskify.controllers;

import com.marv.taskify.domain.dtos.AuthResponseDto;
import com.marv.taskify.domain.dtos.LoginRequestDto;
import com.marv.taskify.domain.dtos.RegisterRequestDto;
import com.marv.taskify.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponseDto register(
            @Valid @RequestBody RegisterRequestDto dto) {

        return authService.register(dto);
    }

    @PostMapping("/login")
    public AuthResponseDto login(
            @Valid @RequestBody LoginRequestDto dto) {
        return authService.login(dto);
    }
}
