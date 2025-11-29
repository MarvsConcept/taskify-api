package com.marv.taskify.controllers;

import com.marv.taskify.domain.dtos.CreateTaskRequestDto;
import com.marv.taskify.domain.dtos.TaskDetailDto;
import com.marv.taskify.domain.dtos.TaskListDto;
import com.marv.taskify.domain.dtos.UpdateTaskRequestDto;
import com.marv.taskify.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskDetailDto createTask(
            @Valid @RequestBody CreateTaskRequestDto dto
            ) {
        return taskService.createTask(dto);
    }

    @GetMapping
    public List<TaskListDto> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskDetailDto getTaskById(@PathVariable UUID id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskDetailDto updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody
            UpdateTaskRequestDto dto
            ) {
        return taskService.updateTask(id, dto);
    }
}
