package com.pruebaTecnica.bike_rental_api.mapper;

import com.pruebaTecnica.bike_rental_api.dto.response.RentalResponseDTO;
import com.pruebaTecnica.bike_rental_api.entity.Rental;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper {

    public RentalResponseDTO toDTO(Rental rental) {
        RentalResponseDTO dto = new RentalResponseDTO();

        dto.setId(rental.getId());
        dto.setBikeCode(rental.getBike().getCode());
        dto.setClientName(rental.getClientName());
        dto.setStartTime(rental.getStartTime());
        dto.setEndTime(rental.getEndTime());
        dto.setActualHours(rental.getActualHours());
        dto.setBaseCost(rental.getBaseCost());
        dto.setFineAmount(rental.getFineAmount());
        dto.setTotalCost(rental.getTotalCost());

        return dto;
    }
}