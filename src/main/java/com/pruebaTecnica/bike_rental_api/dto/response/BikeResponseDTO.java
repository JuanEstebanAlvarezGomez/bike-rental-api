package com.pruebaTecnica.bike_rental_api.dto.response;

import com.pruebaTecnica.bike_rental_api.entity.BikeStatus;
import com.pruebaTecnica.bike_rental_api.entity.BikeType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BikeResponseDTO {

    private String code;

    private BikeType type;

    private BikeStatus status;
}
