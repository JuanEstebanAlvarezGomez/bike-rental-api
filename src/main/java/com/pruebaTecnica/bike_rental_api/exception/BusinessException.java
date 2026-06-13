package com.pruebaTecnica.bike_rental_api.exception;

public class BusinessException
        extends RuntimeException {

    public BusinessException(
            String message) {

        super(message);
    }
}
