package com.marv.taskify.repositories;

import com.marv.taskify.domain.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByOwnerId(UUID ownerId);

    Optional<Task> findByIdAndOwnerId(UUID id, UUID ownerId);


}
