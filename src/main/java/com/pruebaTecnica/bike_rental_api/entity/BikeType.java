package com.pruebaTecnica.bike_rental_api.entity;

public enum BikeType {

    URBANA(3500),

    MONTAÑA(5000),

    ELECTRICA(7500);

    private final double ratePerHour;

    BikeType(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }
}
