package com.pruebaTecnica.bike_rental_api.controller;

import com.pruebaTecnica.bike_rental_api.dto.request.*;
import com.pruebaTecnica.bike_rental_api.dto.response.RentalResponseDTO;
import com.pruebaTecnica.bike_rental_api.mapper.RentalMapper;
import com.pruebaTecnica.bike_rental_api.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    @Operation(summary = "Iniciar alquiler")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Alquiler iniciado exitosamente",
            content = @Content(schema = @Schema(implementation = RentalResponseDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida - Bicicleta no disponible", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado - Credenciales inválidas", content = @Content),
        @ApiResponse(responseCode = "404", description = "Bicicleta no encontrada", content = @Content)
    })
    @PostMapping("/start")
    public RentalResponseDTO startRental(@Valid @RequestBody RentalStartRequestDTO request) {
        return rentalMapper.toDTO(rentalService.startRental(request));
    }

    @Operation(summary = "Finalizar alquiler")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Alquiler finalizado exitosamente - Costo calculado",
            content = @Content(schema = @Schema(implementation = RentalResponseDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida - Alquiler ya finalizado", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado - Credenciales inválidas", content = @Content),
        @ApiResponse(responseCode = "404", description = "Alquiler no encontrado", content = @Content)
    })
    @PutMapping("/{id}/end")
    public RentalResponseDTO endRental(@PathVariable Long id, @RequestBody(required = false) RentalEndRequestDTO request) {
        LocalDateTime endTime = request != null && request.getEndTime() != null
                ? request.getEndTime()
                : LocalDateTime.now();

        return rentalMapper.toDTO(rentalService.endRental(id, endTime));
    }

    @Operation(summary = "Historial de alquileres")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Historial obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = RentalResponseDTO.class))
        ),
        @ApiResponse(responseCode = "401", description = "No autorizado - Credenciales inválidas", content = @Content),
        @ApiResponse(responseCode = "404", description = "Bicicleta no encontrada", content = @Content)
    })
    @GetMapping("/history/{bikeCode}")
    public List<RentalResponseDTO> history(@PathVariable String bikeCode) {
        return rentalService
                .getHistory(bikeCode)
                .stream()
                .map(rentalMapper::toDTO)
                .toList();
    }
}