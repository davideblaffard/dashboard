package org.pizzeria.dashboard.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class EmployeeDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String filename;

    @ManyToOne
    @JoinColumn(name = "employee_contract_id")
    private EmployeeContract employeeContract;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public EmployeeContract getEmployeeContract() {
        return employeeContract;
    }

    public void setEmployeeContract(EmployeeContract employeeContract) {
        this.employeeContract = employeeContract;
    }

    
    
}
