package com.pruebaTecnica.bike_rental_api.config;

import com.pruebaTecnica.bike_rental_api.entity.*;
import com.pruebaTecnica.bike_rental_api.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final BikeRepository bikeRepository;

    @Override
    public void run(String... args) {
        bikeRepository.save(Bike.builder()
                .code("BIC-001")
                .type(BikeType.URBANA)
                .status(BikeStatus.Disponible)
                .build());

        bikeRepository.save(Bike.builder()
                .code("BIC-002")
                .type(BikeType.MONTAÑA)
                .status(BikeStatus.Disponible)
                .build());

        bikeRepository.save(Bike.builder()
                .code("BIC-003")
                .type(BikeType.ELECTRICA)
                .status(BikeStatus.Disponible)
                .build());

        bikeRepository.save(Bike.builder()
                .code("BIC-004")
                .type(BikeType.MONTAÑA)
                .status(BikeStatus.En_mantenimiento)
                .build());

        bikeRepository.save(Bike.builder()
                .code("BIC-005")
                .type(BikeType.URBANA)
                .status(BikeStatus.Disponible)
                .build());
    }
}