package com.marv.taskify.controllers;

import com.marv.taskify.domain.dtos.TaskListDto;
import com.marv.taskify.domain.dtos.UserProfileDto;
import com.marv.taskify.services.TaskService;
import com.marv.taskify.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    @GetMapping("/users/me")
    public ResponseEntity<UserProfileDto> getMe() {
        UserProfileDto me = userService.getCurrentUserProfile();
        return ResponseEntity.ok(me);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<UserProfileDto>> getAllusers() {
        List<UserProfileDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/admin/users/{id}/tasks")
    public ResponseEntity<List<TaskListDto>> getUserTasks(
            @PathVariable UUID id
    ) {
        List<TaskListDto> tasks = taskService.getTasksForUser(id);
        return ResponseEntity.ok(tasks);
    }


}
