package org.pizzeria.dashboard.repository;

import java.time.LocalDate;
import java.util.List;

import org.pizzeria.dashboard.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer>{
    List<Schedule> findByDayBetween(LocalDate start, LocalDate end);
}
