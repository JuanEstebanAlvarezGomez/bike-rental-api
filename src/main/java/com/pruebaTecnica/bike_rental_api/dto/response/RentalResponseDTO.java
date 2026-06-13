package com.pruebaTecnica.bike_rental_api.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentalResponseDTO {

    private Long id;

    private String bikeCode;

    private String clientName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer actualHours;

    private Double baseCost;

    private Double fineAmount;

    private Double totalCost;
}
