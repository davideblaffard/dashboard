package org.pizzeria.dashboard.controller;


import org.pizzeria.dashboard.model.Supplier;
import org.pizzeria.dashboard.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    private final String UPLOAD_DIR = "uploads/";

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
            return "suppliers/show";
        } else {
            return "redirect:/suppliers";
        }
    }

    // CREATE - form per nuovo fornitore
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "suppliers/form";
    }

    // STORE - salva nuovo fornitore
    @PostMapping("/store")
    public String store(@ModelAttribute Supplier supplier,
                        @RequestParam("file") MultipartFile file) throws IOException{

        if (!file.isEmpty()) {
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + filename);
            Files.createDirectories(path.getParent());
            file.transferTo(path.toFile());
            supplier.setOrderFormFilename(filename);
        }

        supplierRepository.save(supplier);
        return "redirect:/suppliers";
    }

    // EDIT - form per modificare fornitore (soprattutto il modulo)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Supplier> result = supplierRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("supplier", result.get());
            return "suppliers/edit";
        } else {
            return "redirect:/suppliers";
        }
    }

    // UPDATE - salva modifiche
    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute Supplier supplier,
                        @RequestParam("file") MultipartFile file) throws IOException {

        Optional<Supplier> existingOpt = supplierRepository.findById(id);
        if (existingOpt.isPresent()) {
            Supplier existing = existingOpt.get();

            // Mantieni vecchio file se non caricato nuovo
            if (!file.isEmpty()) {
                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + filename);
                Files.createDirectories(path.getParent());
                file.transferTo(path.toFile());
                supplier.setOrderFormFilename(filename);
            } else {
                supplier.setOrderFormFilename(existing.getOrderFormFilename());
            }

        supplier.setId(id);
        supplierRepository.save(supplier);
        return "redirect:/suppliers";
        }
        return "redirect:/suppliers";
    }

    // DOWNLOAD FILE
    @GetMapping("/download/{filename}")
    @ResponseBody
    public byte[] downloadFile(@PathVariable String filename) throws IOException {
        Path path = Paths.get(UPLOAD_DIR + filename);
        return Files.readAllBytes(path);
    }

    // DELETE - elimina un fornitore
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        supplierRepository.deleteById(id);
        return "redirect:/suppliers";
    }
}
