package org.pizzeria.dashboard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String ccEmails; // eventualmente separati da virgola
    private String deliveryDays; // es: "Lunedì e Giovedì"

    @Lob
    private String notes; // testo 

    private String orderFormFilename;

    // Costruttori
    public Supplier() {}

    public Supplier(String name, String email, String ccEmails, String deliveryDays, String orderFormTemplate) {
        this.name = name;
        this.email = email;
        this.ccEmails = ccEmails;
        this.deliveryDays = deliveryDays;
        this.orderFormFilename = orderFormFilename;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCcEmails() {
        return ccEmails;
    }

    public void setCcEmails(String ccEmails) {
        this.ccEmails = ccEmails;
    }

    public String getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(String deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOrderFormFilename() {
        return orderFormFilename;
    }

    public void setOrderFormFilename(String orderFormFilename) {
        this.orderFormFilename = orderFormFilename;
    }

    
    
}
