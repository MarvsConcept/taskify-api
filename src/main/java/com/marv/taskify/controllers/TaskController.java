package com.marv.taskify.controllers;

import com.marv.taskify.domain.TaskStatus;
import com.marv.taskify.domain.dtos.CreateTaskRequestDto;
import com.marv.taskify.domain.dtos.TaskDetailDto;
import com.marv.taskify.domain.dtos.TaskListDto;
import com.marv.taskify.domain.dtos.UpdateTaskRequestDto;
import com.marv.taskify.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDetailDto> createTask(
            @Valid @RequestBody CreateTaskRequestDto dto
    ) {
        TaskDetailDto created = taskService.createTask(dto);
        // 201 CREATED with body
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<TaskListDto>> getTasks(
            @RequestParam(required = false) TaskStatus status
            ) {
        List<TaskListDto> tasks = taskService.getTasks(status);
        return ResponseEntity.ok(tasks); // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDetailDto> getTaskById(@PathVariable UUID id) {
        TaskDetailDto task = taskService.getTaskById(id);
        return ResponseEntity.ok(task); // 200 OK
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDetailDto> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequestDto dto
    ) {
        TaskDetailDto updated = taskService.updateTask(id, dto);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build(); // 204 NO CONTENT
    }
}
