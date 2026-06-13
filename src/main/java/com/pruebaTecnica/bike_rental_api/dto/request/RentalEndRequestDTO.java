package com.pruebaTecnica.bike_rental_api.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentalEndRequestDTO {

    private LocalDateTime endTime;
}
