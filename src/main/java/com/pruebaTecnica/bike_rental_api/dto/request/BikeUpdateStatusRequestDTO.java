package com.pruebaTecnica.bike_rental_api.dto.request;

import com.pruebaTecnica.bike_rental_api.entity.BikeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BikeUpdateStatusRequestDTO {
    
    @NotNull(message = "El estado es obligatorio")
    private BikeStatus status;
}