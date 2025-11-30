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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

        // Validate due date (cannot be in the past)
        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }

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
    public List<TaskListDto> getTasks(TaskStatus status) {

        // Get current user
        User currentUser = currentUserService.getCurrentUser();

        List<Task> tasks;

        // Check role before listing the task

        if (currentUser.getRole() == Role.ADMIN) {
            tasks = taskRepository.findAll();
        } else {
            tasks = taskRepository.findByOwnerId(currentUser.getId());
        }

        // If status is provided, filter in-memory
        if (status != null) {
            tasks = tasks.stream()
                    .filter(t -> t.getStatus() == status)
                    .toList();
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

        // keep old status to compare later
        TaskStatus oldStatus = existingTask.getStatus();

        // update basic fields
        existingTask.setTitle(dto.getTitle());
        existingTask.setDescription(dto.getDescription());
        existingTask.setTags(dto.getTags());

        // Validate due date if provided
        if (dto.getDueDate() != null) {
            if (dto.getDueDate().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Due date cannot be in the past");
            }
            existingTask.setDueDate(dto.getDueDate());
        }


        // update status if provided
        if (dto.getStatus() != null) {
            TaskStatus newStatus = dto.getStatus();
            existingTask.setStatus(newStatus);

            // moved into Done
            if (newStatus == TaskStatus.DONE && oldStatus != TaskStatus.DONE) {
                existingTask.setCompletedAt(LocalDateTime.now());
            }

            // moved out of DONE (reopened) – optional but usually what you want
            if (newStatus != TaskStatus.DONE && oldStatus == TaskStatus.DONE) {
                existingTask.setCompletedAt(null);
            }

        }

        // save and return TaskDetailDto
        Task updatedTask = taskRepository.save(existingTask);
        return taskMapper.toTaskDetailDto(updatedTask);
    }

    @Override
    public void deleteTask(UUID id) {

        // Get current user
        User currentUser = currentUserService.getCurrentUser();

        Task task;

        // load task based on role (ADMIN vs USER)
        if (currentUser.getRole() == Role.ADMIN){
            // admin can delete any task
            task = taskRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with Id: " + id));
        } else {
            // normal user - only their own tasks
            task = taskRepository.findByIdAndOwnerId(id, currentUser.getId())
                    .orElseThrow(() ->
                            new EntityNotFoundException("Task not found or you do not have access"));
        }

        taskRepository.delete(task);
    }


    public List<TaskListDto> getTasksForUser(UUID userId) {
        List<Task> tasks = taskRepository.findByOwnerId(userId);
        return taskMapper.toTaskListDtos(tasks);
    }

}
