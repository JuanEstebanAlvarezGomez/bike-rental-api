package com.pruebaTecnica.bike_rental_api.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
                LocalDateTime timestamp,
                Integer status,
                String error) {
}
