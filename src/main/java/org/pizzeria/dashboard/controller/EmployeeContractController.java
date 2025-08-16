package org.pizzeria.dashboard.controller;

import org.pizzeria.dashboard.model.EmployeeContract;
import org.pizzeria.dashboard.model.EmployeeDocument;
import org.pizzeria.dashboard.repository.EmployeeContractRepository;
import org.pizzeria.dashboard.repository.EmployeeDocumentRepository;
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
@RequestMapping("/contracts")
public class EmployeeContractController {

    private static final Path UPLOAD_DIR = Paths.get("uploads/contracts");

    @Autowired
    private EmployeeContractRepository contractRepository;

    @Autowired
    private EmployeeDocumentRepository documentRepository;

    // INDEX (fragment)
    @GetMapping
    public String index(Model model) {
        model.addAttribute("contracts", contractRepository.findAll());
        return "fragments/contracts/index :: index";
    }

    // CREATE form (fragment)
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("contract", new EmployeeContract());
        return "fragments/contracts/form :: form";
    }

    // STORE (ritorna lista aggiornata come fragment)
    @PostMapping("/store")
    public String store(@ModelAttribute EmployeeContract contract, Model model) {
        contractRepository.save(contract);
        model.addAttribute("contracts", contractRepository.findAll());
        return "fragments/contracts/index :: index";
    }

    // SHOW (fragment)
    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {
        Optional<EmployeeContract> opt = contractRepository.findById(id);
        if (opt.isEmpty()) {
            model.addAttribute("contracts", contractRepository.findAll());
            return "fragments/contracts/index :: index";
        }
        model.addAttribute("contract", opt.get());
        return "fragments/contracts/show :: show";
    }

    // DELETE (ritorna lista aggiornata come fragment)
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        contractRepository.deleteById(id);
        model.addAttribute("contracts", contractRepository.findAll());
        return "fragments/contracts/index :: index";
    }

    // UPLOAD DOCUMENT (ritorna show aggiornato come fragment)
    @PostMapping("/{id}/upload")
    public String uploadDocument(@PathVariable Integer id,
                                 @RequestParam("file") MultipartFile file,
                                 Model model) throws IOException {
        Optional<EmployeeContract> opt = contractRepository.findById(id);
        if (opt.isPresent() && file != null && !file.isEmpty()) {
            EmployeeContract contract = opt.get();
            String filename = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path target = UPLOAD_DIR.resolve(filename).normalize();
            Files.createDirectories(target.getParent());
            file.transferTo(target.toFile());

            EmployeeDocument doc = new EmployeeDocument();
            doc.setFilename(filename);
            doc.setEmployeeContract(contract);
            documentRepository.save(doc);

            model.addAttribute("contract", contractRepository.findById(id).orElse(contract));
            return "fragments/contracts/show :: show";
        }
        model.addAttribute("contracts", contractRepository.findAll());
        return "fragments/contracts/index :: index";
    }

    // DELETE DOCUMENT (ritorna show aggiornato come fragment)
    @PostMapping("/{contractId}/delete-doc/{docId}")
    public String deleteDocument(@PathVariable Integer contractId,
                                 @PathVariable Integer docId,
                                 Model model) {
        documentRepository.deleteById(docId);
        contractRepository.findById(contractId).ifPresent(c -> model.addAttribute("contract", c));
        return "fragments/contracts/show :: show";
    }

    // (Opzionale) DOWNLOAD document
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> download(@PathVariable String filename) throws MalformedURLException {
        Path safe = UPLOAD_DIR.resolve(filename).normalize();
        Resource resource = new UrlResource(safe.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }
        String cd = ContentDisposition.attachment().filename(filename).build().toString();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, cd)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
