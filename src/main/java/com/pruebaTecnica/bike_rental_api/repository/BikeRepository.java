package com.pruebaTecnica.bike_rental_api.repository;

import com.pruebaTecnica.bike_rental_api.entity.Bike;
import com.pruebaTecnica.bike_rental_api.entity.BikeStatus;
import com.pruebaTecnica.bike_rental_api.entity.BikeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BikeRepository extends JpaRepository<Bike, Long> {

        Optional<Bike> findByCode(String code);

        List<Bike> findByStatus(BikeStatus status);

        List<Bike> findByStatusAndType(BikeStatus status,BikeType type);
}