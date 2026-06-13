package com.pruebaTecnica.bike_rental_api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pruebaTecnica.bike_rental_api.dto.request.BikeCreateRequestDTO;
import com.pruebaTecnica.bike_rental_api.entity.Bike;
import com.pruebaTecnica.bike_rental_api.entity.BikeStatus;
import com.pruebaTecnica.bike_rental_api.entity.BikeType;
import com.pruebaTecnica.bike_rental_api.exception.BusinessException;
import com.pruebaTecnica.bike_rental_api.repository.BikeRepository;

@ExtendWith(MockitoExtension.class)
public class BikeServiceTest {

    @Mock
    private BikeRepository bikeRepository;

    @InjectMocks
    private BikeService bikeService;

    @Test
    void shouldGetAvailableBikes() {
        Bike bike1 = new Bike();
        bike1.setStatus(BikeStatus.Disponible);
        Bike bike2 = new Bike();
        bike2.setStatus(BikeStatus.Disponible);

        when(bikeRepository.findByStatus(BikeStatus.Disponible)).thenReturn(List.of(bike1, bike2));

        List<Bike> result = bikeService.getAvailableBikes(null);

        assertEquals(2, result.size());
        verify(bikeRepository).findByStatus(BikeStatus.Disponible);
    }

    @Test
    void shouldGetAvailableBikesByType() {
        Bike bike = new Bike();
        bike.setType(BikeType.URBANA);
        bike.setStatus(BikeStatus.Disponible);

        when(bikeRepository.findByStatusAndType(BikeStatus.Disponible, BikeType.URBANA))
                .thenReturn(List.of(bike));

        List<Bike> result = bikeService.getAvailableBikes(BikeType.URBANA);

        assertEquals(1, result.size());
        verify(bikeRepository).findByStatusAndType(BikeStatus.Disponible, BikeType.URBANA);
    }

    @Test
    void shouldGetAllBikes() {
        when(bikeRepository.findAll()).thenReturn(List.of(new Bike(), new Bike()));

        List<Bike> result = bikeService.getAllBikes();

        assertEquals(2, result.size());
        verify(bikeRepository).findAll();
    }

    @Test
    void shouldGetBikeByCode() {
        Bike bike = new Bike();
        bike.setCode("BIC-001");

        when(bikeRepository.findByCode("BIC-001")).thenReturn(Optional.of(bike));

        Optional<Bike> result = bikeService.getBikeByCode("BIC-001");

        assertTrue(result.isPresent());
        assertEquals("BIC-001", result.get().getCode());
    }

    @Test
    void shouldCreateBike() {
        BikeCreateRequestDTO request = new BikeCreateRequestDTO();
        request.setCode("BIC-010");
        request.setType(BikeType.URBANA);
        request.setStatus(BikeStatus.Disponible);

        Bike bike = new Bike();
        bike.setCode("BIC-010");
        bike.setType(BikeType.URBANA);
        bike.setStatus(BikeStatus.Disponible);

        when(bikeRepository.findByCode("BIC-010")).thenReturn(Optional.empty());
        when(bikeRepository.save(any(Bike.class))).thenReturn(bike);

        Bike result = bikeService.createBike(request);

        assertNotNull(result);
        assertEquals("BIC-010", result.getCode());
        verify(bikeRepository).save(any(Bike.class));
    }

    @Test
    void shouldThrowExceptionWhenCreateBikeWithDuplicateCode() {
        BikeCreateRequestDTO request = new BikeCreateRequestDTO();
        request.setCode("BIC-001");

        Bike existingBike = new Bike();
        existingBike.setCode("BIC-001");

        when(bikeRepository.findByCode("BIC-001")).thenReturn(Optional.of(existingBike));

        assertThrows(BusinessException.class, () -> bikeService.createBike(request));
        verify(bikeRepository, never()).save(any(Bike.class));
    }

    @Test
    void shouldUpdateBikeStatus() {
        Bike bike = new Bike();
        bike.setCode("BIC-001");
        bike.setStatus(BikeStatus.Disponible);

        when(bikeRepository.findByCode("BIC-001")).thenReturn(Optional.of(bike));
        when(bikeRepository.save(any(Bike.class))).thenReturn(bike);

        Bike result = bikeService.updateBikeStatus("BIC-001", BikeStatus.En_mantenimiento);

        assertEquals(BikeStatus.En_mantenimiento, result.getStatus());
        verify(bikeRepository).save(bike);
    }

    @Test
    void shouldThrowExceptionWhenUpdateBikeStatusWithInvalidTransition() {
        Bike bike = new Bike();
        bike.setCode("BIC-001");
        bike.setStatus(BikeStatus.Alquilada);

        when(bikeRepository.findByCode("BIC-001")).thenReturn(Optional.of(bike));

        assertThrows(BusinessException.class, () -> 
            bikeService.updateBikeStatus("BIC-001", BikeStatus.En_mantenimiento));
    }

    @Test
    void shouldDeleteBikeWhenInMaintenance() {
        Bike bike = new Bike();
        bike.setCode("BIC-004");
        bike.setStatus(BikeStatus.En_mantenimiento);

        when(bikeRepository.findByCode("BIC-004")).thenReturn(Optional.of(bike));

        assertDoesNotThrow(() -> bikeService.deleteBike("BIC-004"));
        verify(bikeRepository).delete(bike);
    }

    @Test
    void shouldThrowExceptionWhenDeleteBikeNotInMaintenance() {
        Bike bike = new Bike();
        bike.setCode("BIC-001");
        bike.setStatus(BikeStatus.Disponible);

        when(bikeRepository.findByCode("BIC-001")).thenReturn(Optional.of(bike));

        assertThrows(BusinessException.class, () -> bikeService.deleteBike("BIC-001"));
        verify(bikeRepository, never()).delete(any(Bike.class));
    }

    @Test
    void shouldThrowExceptionWhenDeleteBikeNotFound() {
        when(bikeRepository.findByCode("BIC-999")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> bikeService.deleteBike("BIC-999"));
        verify(bikeRepository, never()).delete(any(Bike.class));
    }
}