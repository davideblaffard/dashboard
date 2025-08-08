package org.pizzeria.dashboard.repository;

import java.time.LocalDate;
import java.util.List;

import org.pizzeria.dashboard.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByDueDateBetween(LocalDate start, LocalDate end);
}
