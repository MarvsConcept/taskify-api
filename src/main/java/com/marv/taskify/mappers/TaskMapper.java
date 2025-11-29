package com.marv.taskify.mappers;


import com.marv.taskify.domain.dtos.CreateTaskRequestDto;
import com.marv.taskify.domain.dtos.TaskDetailDto;
import com.marv.taskify.domain.dtos.TaskListDto;
import com.marv.taskify.domain.dtos.UpdateTaskRequestDto;
import com.marv.taskify.domain.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {


    TaskListDto toTaskListDto(Task task);
    List<TaskListDto> toTaskListDtos(List<Task> tasks);

    TaskDetailDto toTaskDetailDto(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    Task toTask(CreateTaskRequestDto dto);

    void updateTaskFromDto(UpdateTaskRequestDto dto, @MappingTarget Task task);

}
