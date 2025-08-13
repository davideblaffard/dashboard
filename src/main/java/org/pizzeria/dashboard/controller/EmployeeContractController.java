package org.pizzeria.dashboard.controller;

import org.pizzeria.dashboard.model.EmployeeContract;
import org.pizzeria.dashboard.model.EmployeeDocument;
import org.pizzeria.dashboard.repository.EmployeeContractRepository;
import org.pizzeria.dashboard.repository.EmployeeDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

@Controller
@RequestMapping("/contracts")
public class EmployeeContractController {

    private static final String UPLOAD_DIR = "uploads/contracts/";

    @Autowired
    private EmployeeContractRepository contractRepository;

    @Autowired
    private EmployeeDocumentRepository documentRepository;

    // INDEX - lista contratti
    @GetMapping
    public String index(Model model) {
        model.addAttribute("contracts", contractRepository.findAll());
        return "contracts/index";
    }

    // CREATE - form nuovo dipendente
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("contract", new EmployeeContract());
        return "contracts/form";
    }

    // STORE - salva nuovo dipendente
    @PostMapping("/store")
    public String store(@ModelAttribute EmployeeContract contract) {
        contractRepository.save(contract);
        return "redirect:/contracts";
    }

    // SHOW - dettagli contratto e documenti
    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {
        Optional<EmployeeContract> opt = contractRepository.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("contract", opt.get());
            return "contracts/show";
        } else {
            return "redirect:/contracts";
        }
    }

    // DELETE - rimuove dipendente
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        contractRepository.deleteById(id);
        return "redirect:/contracts";
    }

    // UPLOAD DOCUMENT
    @PostMapping("/{id}/upload")
    public String uploadDocument(@PathVariable Integer id, @RequestParam("file") MultipartFile file) throws IOException {
        Optional<EmployeeContract> opt = contractRepository.findById(id);
        if (opt.isPresent() && !file.isEmpty()) {
            EmployeeContract contract = opt.get();

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + filename);
            Files.createDirectories(path.getParent());
            file.transferTo(path.toFile());

            EmployeeDocument doc = new EmployeeDocument();
            doc.setFilename(filename);
            doc.setEmployeeContract(contract);
            documentRepository.save(doc);
        }
        return "redirect:/contracts/" + id;
    }

    // DELETE DOCUMENT
    @PostMapping("/{contractId}/delete-doc/{docId}")
    public String deleteDocument(@PathVariable Integer contractId, @PathVariable Integer docId) {
        documentRepository.deleteById(docId);
        return "redirect:/contracts/" + contractId;
    }
}

