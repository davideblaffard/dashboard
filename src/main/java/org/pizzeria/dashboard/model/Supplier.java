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
    private String orderFormTemplate; // testo precompilato modificabile

    // Costruttori
    public Supplier() {}

    public Supplier(String name, String email, String ccEmails, String deliveryDays, String orderFormTemplate) {
        this.name = name;
        this.email = email;
        this.ccEmails = ccEmails;
        this.deliveryDays = deliveryDays;
        this.orderFormTemplate = orderFormTemplate;
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

    public String getOrderFormTemplate() {
        return orderFormTemplate;
    }

    public void setOrderFormTemplate(String orderFormTemplate) {
        this.orderFormTemplate = orderFormTemplate;
    }
    
}
