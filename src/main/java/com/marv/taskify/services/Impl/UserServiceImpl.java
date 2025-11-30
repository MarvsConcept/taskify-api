package com.marv.taskify.services.Impl;

import com.marv.taskify.domain.dtos.UserProfileDto;
import com.marv.taskify.domain.entities.User;
import com.marv.taskify.repositories.UserRepository;
import com.marv.taskify.services.CurrentUserService;
import com.marv.taskify.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;

    @Override
    public UserProfileDto getCurrentUserProfile() {

        User currentUser = currentUserService.getCurrentUser();
        return toUserProfileDto(currentUser);
    }

    @Override
    public List<UserProfileDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toUserProfileDto)
                .toList();
    }

    private UserProfileDto toUserProfileDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
