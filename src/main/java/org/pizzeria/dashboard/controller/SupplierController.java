package org.pizzeria.dashboard.controller;


import org.pizzeria.dashboard.model.Supplier;
import org.pizzeria.dashboard.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    // INDEX - mostra tutti i fornitori
    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "suppliers/index";
    }

    // SHOW - dettagli del singolo fornitore
    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {
        Optional<Supplier> result = supplierRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("supplier", result.get());
            return "supplier/show";
        } else {
            return "redirect:/suppliers";
        }
    }

    // CREATE - form per nuovo fornitore
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "supplier/create";
    }

    // STORE - salva nuovo fornitore
    @PostMapping("/store")
    public String store(@ModelAttribute Supplier supplier) {
        supplierRepository.save(supplier);
        return "redirect:/suppliers";
    }

    // EDIT - form per modificare fornitore (soprattutto il modulo)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Supplier> result = supplierRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("supplier", result.get());
            return "supplier/edit";
        } else {
            return "redirect:/suppliers";
        }
    }

    // UPDATE - salva modifiche
    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute Supplier supplier) {
        supplier.setId(id);
        supplierRepository.save(supplier);
        return "redirect:/suppliers";
    }

    // DELETE - elimina un fornitore
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        supplierRepository.deleteById(id);
        return "redirect:/suppliers";
    }
}
