package org.pizzeria.dashboard.controller;

import org.pizzeria.dashboard.model.Recurrence;
import org.pizzeria.dashboard.model.Task;
import org.pizzeria.dashboard.model.TaskCategory;
import org.pizzeria.dashboard.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping
    public String createQuick(
            @RequestParam String title,
            @RequestParam TaskCategory category,
            @RequestParam LocalDate dueDate,
            @RequestParam(defaultValue = "NONE") Recurrence recurrence,
            @RequestParam(required = false) String redirect,
            RedirectAttributes ra) {

        Task t = new Task();
        t.setTitle(title);
        t.setCategory(category);
        t.setDueDate(dueDate);
        t.setRecurrence(recurrence);
        t.setDone(false);
        taskRepository.save(t);

        if (redirect != null && !redirect.isBlank()) return "redirect:" + redirect;
        return "redirect:/dashboard?date=" + dueDate;
    }

    @PostMapping("/{id}/toggle")
    public String toggleDone(
            @PathVariable Integer id,
            @RequestParam(required = false) String redirect) {

        Task t = taskRepository.findById(id).orElse(null);
        if (t != null) {
            boolean newState = !t.isDone();
            t.setDone(newState);
            taskRepository.save(t);

            // se è ricorrente e l’abbiamo appena completata → crea prossima
            if (newState && t.getRecurrence() != null && t.getRecurrence() != Recurrence.NONE) {
                Task next = new Task();
                next.setTitle(t.getTitle());
                next.setCategory(t.getCategory());
                next.setRecurrence(t.getRecurrence());
                next.setDone(false);
                next.setDueDate(nextDateFrom(t.getDueDate(), t.getRecurrence()));
                taskRepository.save(next);
            }

            String back = (redirect != null && !redirect.isBlank())
                    ? redirect
                    : "/dashboard?date=" + t.getDueDate();
            return "redirect:" + back;
        }
        return "redirect:/dashboard";
    }

    private LocalDate nextDateFrom(LocalDate base, Recurrence r) {
        switch (r) {
            case DAILY: return base.plusDays(1);
            case WEEKLY: return base.plusWeeks(1);
            case MONTHLY: return base.plusMonths(1);
            case WEEKDAYS:
                DayOfWeek dow = base.getDayOfWeek();
                if (dow == DayOfWeek.FRIDAY) return base.plusDays(3); // va a lun
                if (dow == DayOfWeek.SATURDAY) return base.plusDays(2); // va a lun
                return base.plusDays(1); // mar-ven
            default:
                return base;
        }
    }
}

