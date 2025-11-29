package com.marv.taskify.services;

import com.marv.taskify.domain.dtos.CreateTaskRequestDto;
import com.marv.taskify.domain.dtos.TaskDetailDto;
import com.marv.taskify.domain.dtos.TaskListDto;
import com.marv.taskify.domain.dtos.UpdateTaskRequestDto;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    List<TaskListDto> getAllTasks();

    TaskDetailDto getTaskById(UUID id);

    TaskDetailDto createTask(CreateTaskRequestDto dto);

    TaskDetailDto updateTask(UUID id, UpdateTaskRequestDto dto);

    void deleteTask(UUID id);
}
