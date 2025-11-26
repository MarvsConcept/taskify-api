package com.marv.taskify.domain.dtos;

import com.marv.taskify.domain.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTaskRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be at most {max} characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private TaskStatus status;

    private Set<String> tags;

    @FutureOrPresent(message = "Due date must be in the future or today")
    private LocalDateTime dueDate;
}
