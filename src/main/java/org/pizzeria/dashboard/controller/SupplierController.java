package org.pizzeria.dashboard.controller;

import org.pizzeria.dashboard.model.Supplier;
import org.pizzeria.dashboard.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Optional;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    private static final Path UPLOAD_DIR = Paths.get("uploads");

    // INDEX (fragment)
    @GetMapping
    public String index(Model model) {
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "fragments/suppliers/index :: index";
    }

    // SHOW (fragment)
    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {
        Optional<Supplier> opt = supplierRepository.findById(id);
        if (opt.isEmpty()) {
            model.addAttribute("suppliers", supplierRepository.findAll());
            return "fragments/suppliers/index :: index";
        }
        model.addAttribute("supplier", opt.get());
        return "fragments/suppliers/show :: show";
    }

    // CREATE form (fragment)
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "fragments/suppliers/form :: form";
    }

    // EDIT form (fragment)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Supplier> opt = supplierRepository.findById(id);
        if (opt.isEmpty()) {
            model.addAttribute("suppliers", supplierRepository.findAll());
            return "fragments/suppliers/index :: index";
        }
        model.addAttribute("supplier", opt.get());
        return "fragments/suppliers/form :: form";
    }

    // STORE (ritorna la lista aggiornata come fragment)
    @PostMapping("/store")
    public String store(@ModelAttribute Supplier supplier,
                        @RequestParam(name = "file", required = false) MultipartFile file,
                        Model model) throws IOException {

        if (file != null && !file.isEmpty()) {
            String filename = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path target = UPLOAD_DIR.resolve(filename).normalize();
            Files.createDirectories(target.getParent());
            file.transferTo(target.toFile());
            supplier.setOrderFormFilename(filename);
        }

        supplierRepository.save(supplier);
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "fragments/suppliers/index :: index";
    }

    // UPDATE (ritorna la lista aggiornata come fragment)
    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id,
                         @ModelAttribute Supplier form,
                         @RequestParam(name = "file", required = false) MultipartFile file,
                         Model model) throws IOException {

        Optional<Supplier> opt = supplierRepository.findById(id);
        if (opt.isPresent()) {
            Supplier existing = opt.get();

            existing.setName(form.getName());
            existing.setEmail(form.getEmail());
            existing.setCcEmails(form.getCcEmails());
            existing.setDeliveryDays(form.getDeliveryDays());
            existing.setNotes(form.getNotes());

            if (file != null && !file.isEmpty()) {
                String filename = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
                Path target = UPLOAD_DIR.resolve(filename).normalize();
                Files.createDirectories(target.getParent());
                file.transferTo(target.toFile());
                existing.setOrderFormFilename(filename);
            }

            supplierRepository.save(existing);
        }

        model.addAttribute("suppliers", supplierRepository.findAll());
        return "fragments/suppliers/index :: index";
    }

    // DELETE (ritorna la lista aggiornata come fragment)
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        supplierRepository.deleteById(id);
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "fragments/suppliers/index :: index";
    }

    // DOWNLOAD file (binary)
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> download(@PathVariable String filename) throws MalformedURLException {
        Path safe = UPLOAD_DIR.resolve(filename).normalize();
        Resource resource = new UrlResource(safe.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }
        String contentDisposition = ContentDisposition.attachment().filename(filename).build().toString();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
