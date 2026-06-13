package com.pruebaTecnica.bike_rental_api.repository;

import com.pruebaTecnica.bike_rental_api.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

        List<Rental> findByBike_Code(String code);
}
