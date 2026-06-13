package com.pruebaTecnica.bike_rental_api.service;

import com.pruebaTecnica.bike_rental_api.dto.request.RentalStartRequestDTO;
import com.pruebaTecnica.bike_rental_api.entity.*;
import com.pruebaTecnica.bike_rental_api.exception.BusinessException;
import com.pruebaTecnica.bike_rental_api.repository.BikeRepository;
import com.pruebaTecnica.bike_rental_api.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final BikeRepository bikeRepository;
    private final PricingService pricingService;

    @Transactional
    public Rental startRental(RentalStartRequestDTO request) {
        Bike bike = bikeRepository
                .findByCode(request.getBikeCode())
                .orElseThrow(() -> new BusinessException("Bicicleta no encontrada"));

        if (bike.getStatus() != BikeStatus.Disponible) {
            throw new BusinessException("La bicicleta no está disponible");
        }

        Rental rental = Rental.builder()
                .bike(bike)
                .clientName(request.getClientName())
                .estimatedHours(request.getEstimatedHours())
                .startTime(LocalDateTime.now())
                .build();

        bike.setStatus(BikeStatus.Alquilada);
        bikeRepository.save(bike);

        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental endRental(Long rentalId, LocalDateTime endTime) {
        Rental rental = rentalRepository
                .findById(rentalId)
                .orElseThrow(() -> new BusinessException("Alquiler no encontrado"));

        if (rental.getEndTime() != null) {
            throw new BusinessException("Este alquiler ya fue finalizado");
        }

        rental.setEndTime(endTime);
        pricingService.calculateCost(rental);

        Bike bike = rental.getBike();
        bike.setStatus(BikeStatus.Disponible);
        bikeRepository.save(bike);

        return rentalRepository.save(rental);
    }

    public List<Rental> getHistory(String bikeCode) {
        return rentalRepository.findByBike_Code(bikeCode);
    }
}