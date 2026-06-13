package com.pruebaTecnica.bike_rental_api.dto.request;

import com.pruebaTecnica.bike_rental_api.entity.BikeStatus;
import com.pruebaTecnica.bike_rental_api.entity.BikeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BikeCreateRequestDTO {
    
    @NotBlank(message = "El código es obligatorio")
    private String code;
    
    @NotNull(message = "El tipo es obligatorio")
    private BikeType type;
    
    @NotNull(message = "El estado es obligatorio")
    private BikeStatus status;
}