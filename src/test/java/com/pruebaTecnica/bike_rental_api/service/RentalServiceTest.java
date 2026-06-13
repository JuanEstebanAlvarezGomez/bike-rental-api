package com.pruebaTecnica.bike_rental_api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pruebaTecnica.bike_rental_api.dto.request.RentalStartRequestDTO;
import com.pruebaTecnica.bike_rental_api.entity.Bike;
import com.pruebaTecnica.bike_rental_api.entity.BikeStatus;
import com.pruebaTecnica.bike_rental_api.entity.Rental;
import com.pruebaTecnica.bike_rental_api.exception.BusinessException;
import com.pruebaTecnica.bike_rental_api.repository.BikeRepository;
import com.pruebaTecnica.bike_rental_api.repository.RentalRepository;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private BikeRepository bikeRepository;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void shouldStartRentalSuccessfully() {
        Bike bike = new Bike();
        bike.setCode("BIC-001");
        bike.setStatus(BikeStatus.Disponible);

        when(bikeRepository.findByCode("BIC-001")).thenReturn(Optional.of(bike));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RentalStartRequestDTO request = new RentalStartRequestDTO();
        request.setBikeCode("BIC-001");
        request.setClientName("Juan");
        request.setEstimatedHours(2);

        Rental rental = rentalService.startRental(request);

        assertNotNull(rental);
        assertEquals(BikeStatus.Alquilada, bike.getStatus());
        verify(bikeRepository).save(bike);
        verify(rentalRepository).save(any(Rental.class));
    }

    @Test
    void shouldThrowExceptionWhenBikeNotFound() {
        when(bikeRepository.findByCode("BIC-999")).thenReturn(Optional.empty());

        RentalStartRequestDTO request = new RentalStartRequestDTO();
        request.setBikeCode("BIC-999");

        assertThrows(BusinessException.class, () -> rentalService.startRental(request));
    }

    @Test
    void shouldThrowExceptionWhenBikeNotAvailable() {
        Bike bike = new Bike();
        bike.setStatus(BikeStatus.Alquilada);

        when(bikeRepository.findByCode("BIC-001")).thenReturn(Optional.of(bike));

        RentalStartRequestDTO request = new RentalStartRequestDTO();
        request.setBikeCode("BIC-001");

        assertThrows(BusinessException.class, () -> rentalService.startRental(request));
    }

    @Test
    void shouldEndRentalSuccessfully() {
        Bike bike = new Bike();
        bike.setStatus(BikeStatus.Alquilada);

        Rental rental = new Rental();
        rental.setBike(bike);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Rental result = rentalService.endRental(1L, LocalDateTime.now());

        assertNotNull(result.getEndTime());
        assertEquals(BikeStatus.Disponible, bike.getStatus());
        verify(pricingService).calculateCost(rental);
    }

    @Test
    void shouldThrowExceptionWhenRentalNotFound() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> rentalService.endRental(1L, LocalDateTime.now()));
    }

    @Test
    void shouldThrowExceptionWhenRentalAlreadyFinished() {
        Rental rental = new Rental();
        rental.setEndTime(LocalDateTime.now());

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        assertThrows(BusinessException.class, () -> rentalService.endRental(1L, LocalDateTime.now()));
    }
}