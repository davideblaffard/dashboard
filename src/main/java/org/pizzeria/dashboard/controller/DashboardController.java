package org.pizzeria.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {


    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String section, Model model) {
    String fragment = "fragments/default :: defaultContent";

    if ("contracts".equals(section)) {
        fragment = "contracts/index :: contractsList";
    } else if ("suppliers".equals(section)) {
        fragment = "suppliers/index :: suppliersList";
    } else if ("reports".equals(section)) {
        fragment = "reports/index :: reportsContent";
    }

    model.addAttribute("contentFragment", fragment);
    return "dashboard";
    }
    
}
