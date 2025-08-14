package org.pizzeria.dashboard.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.pizzeria.dashboard.model.Task;
import org.pizzeria.dashboard.model.TaskCategory;
import org.pizzeria.dashboard.repository.TaskRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    private final TaskRepository taskRepository;

    DashboardController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        LocalDate today = LocalDate.now();
        LocalDate selected = (date != null) ? date : today;

        // settimana (lun → dom) della data selezionata
        LocalDate weekStart = selected.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        List<LocalDate> weekDays = IntStream.range(0, 7)
                .mapToObj(weekStart::plusDays)
                .collect(Collectors.toList());

        // conteggio task per ciascun giorno della settimana
        Map<LocalDate, Long> counts = new HashMap<>();
        for (LocalDate d : weekDays) {
            counts.put(d, taskRepository.countByDueDate(d));
        }

        List<Task> tasksOfDay = taskRepository.findByDueDate(selected);

         Map<TaskCategory, List<Task>> tasksByCategory = new LinkedHashMap<>();
            for (TaskCategory cat : TaskCategory.values()) {
                List<Task> filtered = new ArrayList<>();
                for (Task t : tasksOfDay) {
                    if (t.getCategory() == cat) {
                        filtered.add(t);
                    }
                }
                tasksByCategory.put(cat, filtered);
            }

        model.addAttribute("selectedDate", selected);
        model.addAttribute("today", today);
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("counts", counts);
        model.addAttribute("tasks", tasksOfDay);
        model.addAttribute("tasksByCategory", tasksByCategory);
        model.addAttribute("categories", tasksByCategory.keySet());

        return "dashboard";
    }
    
}
