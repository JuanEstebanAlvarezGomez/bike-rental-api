package com.pruebaTecnica.bike_rental_api.service;

import com.pruebaTecnica.bike_rental_api.entity.Rental;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class PricingService {

    private static final double FINE_PERCENTAGE = 0.5;

    public double calculateCost(Rental rental) {

        long minutesUsed = Duration.between(
                rental.getStartTime(),
                rental.getEndTime())
                .toMinutes();

        int actualHours = (int) Math.ceil(
                minutesUsed / 60.0);

        rental.setActualHours(actualHours);

        double rate = rental.getBike()
                .getType()
                .getRatePerHour();

        double baseCost = actualHours * rate;

        rental.setBaseCost(baseCost);

        long delayedMinutes = Math.max(
                0,
                minutesUsed - rental.getEstimatedHours() * 60L);

        int delayedHours = (int) Math.ceil(
                delayedMinutes / 60.0);

        double fine = delayedHours * (rate * FINE_PERCENTAGE);

        rental.setFineAmount(fine);

        double total = baseCost + fine;

        rental.setTotalCost(total);

        return total;
    }
}