package com.marv.taskify.services;


import com.marv.taskify.domain.dtos.AuthResponseDto;
import com.marv.taskify.domain.dtos.LoginRequestDto;
import com.marv.taskify.domain.dtos.RegisterRequestDto;

public interface AuthService {
    AuthResponseDto register(RegisterRequestDto dto);

    AuthResponseDto login(LoginRequestDto dto);



}
