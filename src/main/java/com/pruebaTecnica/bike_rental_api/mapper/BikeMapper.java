package com.pruebaTecnica.bike_rental_api.mapper;

import com.pruebaTecnica.bike_rental_api.dto.response.BikeResponseDTO;
import com.pruebaTecnica.bike_rental_api.entity.Bike;
import org.springframework.stereotype.Component;

@Component
public class BikeMapper {

    public BikeResponseDTO toDTO(
            Bike bike) {

        return new BikeResponseDTO(
                bike.getCode(),
                bike.getType(),
                bike.getStatus());
    }
}
