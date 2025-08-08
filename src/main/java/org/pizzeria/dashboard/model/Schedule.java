package org.pizzeria.dashboard.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private String role; // "Sala" o "Cucina"

    @Column(nullable = false)
    private LocalDate day;

    private LocalTime startTime;
    private LocalTime endTime;

    private Double hourlyRate;
}
