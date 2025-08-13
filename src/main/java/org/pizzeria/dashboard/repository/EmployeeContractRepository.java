package org.pizzeria.dashboard.repository;

import org.pizzeria.dashboard.model.EmployeeContract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeContractRepository extends JpaRepository<EmployeeContract, Integer>{
    
}
