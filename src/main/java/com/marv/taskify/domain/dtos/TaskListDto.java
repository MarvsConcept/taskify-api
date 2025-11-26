package com.marv.taskify.domain.dtos;


import com.marv.taskify.domain.TaskStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskListDto {

    private UUID id;

    private String title;

    private TaskStatus status;

    private Set<String> tags;

    private LocalDateTime dueDate;

}
