package com.pruebaTecnica.bike_rental_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bike_id")
    private Bike bike;

    private String clientName;

    private LocalDateTime startTime;

    private Integer estimatedHours;

    private LocalDateTime endTime;

    private Integer actualHours;

    private Double baseCost;

    private Double fineAmount;

    private Double totalCost;
}