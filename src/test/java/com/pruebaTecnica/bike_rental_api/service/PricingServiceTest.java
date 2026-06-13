package com.pruebaTecnica.bike_rental_api.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.pruebaTecnica.bike_rental_api.entity.Bike;
import com.pruebaTecnica.bike_rental_api.entity.BikeType;
import com.pruebaTecnica.bike_rental_api.entity.Rental;

public class PricingServiceTest {

    private final PricingService pricingService = new PricingService();

    @Test
    void shouldRoundHoursUp() {
        Bike bike = new Bike();
        bike.setType(BikeType.URBANA);

        Rental rental = new Rental();
        rental.setBike(bike);
        rental.setEstimatedHours(5);
        rental.setStartTime(LocalDateTime.of(2026, 1, 1, 10, 0));
        rental.setEndTime(LocalDateTime.of(2026, 1, 1, 11, 10));

        pricingService.calculateCost(rental);

        assertEquals(2, rental.getActualHours());
    }

    @Test
    void shouldCalculateCostWithoutFine() {
        Bike bike = new Bike();
        bike.setType(BikeType.URBANA);

        Rental rental = new Rental();
        rental.setBike(bike);
        rental.setEstimatedHours(2);
        rental.setStartTime(LocalDateTime.of(2026, 1, 1, 10, 0));
        rental.setEndTime(LocalDateTime.of(2026, 1, 1, 11, 10));

        double total = pricingService.calculateCost(rental);

        assertEquals(7000.0, total);
        assertEquals(0.0, rental.getFineAmount());
        assertEquals(7000.0, rental.getBaseCost());
    }

    @Test
    void shouldApplyFine() {
        Bike bike = new Bike();
        bike.setType(BikeType.MONTAÑA);

        Rental rental = new Rental();
        rental.setBike(bike);
        rental.setEstimatedHours(2);
        rental.setStartTime(LocalDateTime.of(2026, 1, 1, 10, 0));
        rental.setEndTime(LocalDateTime.of(2026, 1, 1, 13, 20));

        double total = pricingService.calculateCost(rental);

        assertEquals(25000.0, total);
        assertEquals(5000.0, rental.getFineAmount());
        assertEquals(20000.0, rental.getBaseCost());
        assertEquals(4, rental.getActualHours());
    }
}