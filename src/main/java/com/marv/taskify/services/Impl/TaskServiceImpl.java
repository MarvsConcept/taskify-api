package com.marv.taskify.services.Impl;

import com.marv.taskify.domain.Role;
import com.marv.taskify.domain.TaskStatus;
import com.marv.taskify.domain.dtos.CreateTaskRequestDto;
import com.marv.taskify.domain.dtos.TaskDetailDto;
import com.marv.taskify.domain.dtos.TaskListDto;
import com.marv.taskify.domain.dtos.UpdateTaskRequestDto;
import com.marv.taskify.domain.entities.Task;
import com.marv.taskify.domain.entities.User;
import com.marv.taskify.mappers.TaskMapper;
import com.marv.taskify.repositories.TaskRepository;
import com.marv.taskify.services.CurrentUserService;
import com.marv.taskify.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final CurrentUserService currentUserService;

    @Override
    public TaskDetailDto createTask(CreateTaskRequestDto dto) {

        //Get Current User
        User currentUser = currentUserService.getCurrentUser();

        // Map the incoming Dto to entity
        Task task = taskMapper.toTask(dto);

        // Set the owner + default status
        task.setOwner(currentUser);

        // Set default status
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }

        // Save the task and map it back
        Task saved = taskRepository.save(task);
        return taskMapper.toTaskDetailDto(saved);
    }

    @Override
    public List<TaskListDto> getAllTasks() {

        // Get current user
        User currentUser = currentUserService.getCurrentUser();

        // Check role before listing the task
        List<Task> tasks;
        if (currentUser.getRole() == Role.ADMIN) {
            tasks = taskRepository.findAll();
        } else {
            tasks = taskRepository.findByOwnerId(currentUser.getId());
        }
        return taskMapper.toTaskListDtos(tasks);
    }

    @Override
    public TaskDetailDto getTaskById(UUID id) {

        // Get current user
        User currentUser = currentUserService.getCurrentUser();

        Task task;

        // Check role before response
        if (currentUser.getRole() == Role.ADMIN) {
            // Admin can see any task by id
             task = taskRepository.findById(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Task not found for Id: " + id));

        } else {
            // Normal user: only if they own the task
            task = taskRepository.findByIdAndOwnerId(id, currentUser.getId())
                    .orElseThrow(() ->
                            new EntityNotFoundException("Task not found or you do not have access"));
        }

        return taskMapper.toTaskDetailDto(task);
    }


    @Override
    public TaskDetailDto updateTask(UUID id, UpdateTaskRequestDto dto) {

        // Get current user
        User currentUser = currentUserService.getCurrentUser();

        Task existingTask;

        // load task based on role (ADMIN vs USER)
        if (currentUser.getRole() == Role.ADMIN){
            existingTask = taskRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with Id: " + id));
        } else {
            existingTask = taskRepository.findByIdAndOwnerId(id, currentUser.getId())
                    .orElseThrow(() ->
                            new EntityNotFoundException("Task not found or you do not have access"));
        }

        // update fields
        existingTask.setTitle(dto.getTitle());
        existingTask.setDescription(dto.getDescription());
        if (dto.getStatus() != null) {
            existingTask.setStatus(dto.getStatus());
        }
        existingTask.setTags(dto.getTags());

        // save and rturn TaskDetailDto
        Task updatedTask = taskRepository.save(existingTask);
        return taskMapper.toTaskDetailDto(updatedTask);
    }
}
