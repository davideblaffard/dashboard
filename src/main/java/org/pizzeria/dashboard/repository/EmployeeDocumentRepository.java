package org.pizzeria.dashboard.repository;

import org.pizzeria.dashboard.model.EmployeeDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeDocumentRepository extends JpaRepository<EmployeeDocument, Integer>{
    
}
