package org.pizzeria.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.pizzeria.dashboard.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer>{
    
}
