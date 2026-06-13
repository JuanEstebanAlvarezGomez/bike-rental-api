package com.pruebaTecnica.bike_rental_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalStartRequestDTO {

    @NotBlank
    private String bikeCode;

    @NotBlank
    private String clientName;

    @Min(1)
    private Integer estimatedHours;
}