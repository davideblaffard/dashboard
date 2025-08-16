package org.pizzeria.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    @GetMapping("/report")
    public String report() {
        return "report/index"; // path: templates/report/index.html
    }
}

