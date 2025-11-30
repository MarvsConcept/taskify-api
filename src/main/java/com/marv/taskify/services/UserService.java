package com.marv.taskify.services;

import com.marv.taskify.domain.dtos.UserProfileDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserService{

    UserProfileDto getCurrentUserProfile();

    List<UserProfileDto> getAllUsers();
}
