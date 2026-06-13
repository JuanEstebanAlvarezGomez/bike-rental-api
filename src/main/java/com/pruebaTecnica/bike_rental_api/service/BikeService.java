package com.pruebaTecnica.bike_rental_api.service;

import com.pruebaTecnica.bike_rental_api.dto.request.BikeCreateRequestDTO;
import com.pruebaTecnica.bike_rental_api.entity.Bike;
import com.pruebaTecnica.bike_rental_api.entity.BikeStatus;
import com.pruebaTecnica.bike_rental_api.entity.BikeType;
import com.pruebaTecnica.bike_rental_api.exception.BusinessException;
import com.pruebaTecnica.bike_rental_api.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BikeService {

    private final BikeRepository bikeRepository;

    public List<Bike> getAvailableBikes(BikeType type) {
        if (type == null) {
            return bikeRepository.findByStatus(BikeStatus.Disponible);
        }
        return bikeRepository.findByStatusAndType(BikeStatus.Disponible, type);
    }

    public List<Bike> getAllBikes() {
        return bikeRepository.findAll();
    }

    public Optional<Bike> getBikeByCode(String code) {
        return bikeRepository.findByCode(code);
    }

    @Transactional
    public Bike createBike(BikeCreateRequestDTO request) {
        if (bikeRepository.findByCode(request.getCode()).isPresent()) {
            throw new BusinessException("Ya existe una bicicleta con el código: " + request.getCode());
        }

        Bike bike = new Bike();
        bike.setCode(request.getCode());
        bike.setType(request.getType());
        bike.setStatus(request.getStatus());
        
        return bikeRepository.save(bike);
    }

    @Transactional
    public Bike updateBikeStatus(String code, BikeStatus newStatus) {
        Bike bike = bikeRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException("Bicicleta no encontrada: " + code));
        
        validateStatusTransition(bike.getStatus(), newStatus);
        
        bike.setStatus(newStatus);
        return bikeRepository.save(bike);
    }

    @Transactional
    public void deleteBike(String code) {
        Bike bike = bikeRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException("Bicicleta no encontrada: " + code));
        
        if (bike.getStatus() != BikeStatus.En_mantenimiento) {
            throw new BusinessException(
                "Solo se pueden eliminar bicicletas en estado En_mantenimiento. " +
                "Estado actual: " + bike.getStatus()
            );
        }
        
        bikeRepository.delete(bike);
    }

    private void validateStatusTransition(BikeStatus currentStatus, BikeStatus newStatus) {
        if (currentStatus == newStatus) {
            return;
        }
        
        switch (currentStatus) {
            case Disponible:
                if (newStatus != BikeStatus.Alquilada && newStatus != BikeStatus.En_mantenimiento) {
                    throw new BusinessException(
                        "Una bicicleta Disponible solo puede pasar a Alquilada o En_mantenimiento. " +
                        "No se puede cambiar a: " + newStatus
                    );
                }
                break;
                
            case Alquilada:
                if (newStatus != BikeStatus.Disponible) {
                    throw new BusinessException(
                        "Una bicicleta Alquilada solo puede pasar a Disponible. " +
                        "No se puede cambiar a: " + newStatus
                    );
                }
                break;
                
            case En_mantenimiento:
                if (newStatus != BikeStatus.Disponible) {
                    throw new BusinessException(
                        "Una bicicleta En_mantenimiento solo puede pasar a Disponible. " +
                        "No se puede cambiar a: " + newStatus
                    );
                }
                break;
                
            default:
                throw new BusinessException("Estado de bicicleta inválido: " + currentStatus);
        }
    }
}