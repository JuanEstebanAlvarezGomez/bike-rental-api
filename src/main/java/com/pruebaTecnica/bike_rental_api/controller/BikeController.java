package com.pruebaTecnica.bike_rental_api.controller;

import com.pruebaTecnica.bike_rental_api.dto.request.BikeCreateRequestDTO;
import com.pruebaTecnica.bike_rental_api.dto.request.BikeUpdateStatusRequestDTO;
import com.pruebaTecnica.bike_rental_api.dto.response.BikeResponseDTO;
import com.pruebaTecnica.bike_rental_api.entity.BikeType;
import com.pruebaTecnica.bike_rental_api.mapper.BikeMapper;
import com.pruebaTecnica.bike_rental_api.service.BikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bikes")
@RequiredArgsConstructor
public class BikeController {

    private final BikeService bikeService;
    private final BikeMapper bikeMapper;

    @Operation(summary = "Consultar bicicletas disponibles")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bicicletas disponibles obtenidas exitosamente",
            content = @Content(schema = @Schema(implementation = BikeResponseDTO.class))
        ),
        @ApiResponse(responseCode = "401", description = "No autorizado - Credenciales inválidas", content = @Content)
    })
    @GetMapping("/available")
    public List<BikeResponseDTO> getAvailableBikes(
            @RequestParam(required = false) BikeType type) {
        
        return bikeService
                .getAvailableBikes(type)
                .stream()
                .map(bikeMapper::toDTO)
                .toList();
    }

    @Operation(summary = "Consultar todas las bicicletas")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de bicicletas obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = BikeResponseDTO.class))
        ),
        @ApiResponse(responseCode = "401", description = "No autorizado - Credenciales inválidas", content = @Content)
    })
    @GetMapping
    public List<BikeResponseDTO> getAllBikes() {
        return bikeService
                .getAllBikes()
                .stream()
                .map(bikeMapper::toDTO)
                .toList();
    }

    @Operation(summary = "Consultar bicicleta por código")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bicicleta encontrada exitosamente",
            content = @Content(schema = @Schema(implementation = BikeResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Bicicleta no encontrada", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado - Credenciales inválidas", content = @Content)
    })
    @GetMapping("/{code}")
    public ResponseEntity<BikeResponseDTO> getBikeByCode(@PathVariable String code) {
        return bikeService
                .getBikeByCode(code)
                .map(bikeMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar nueva bicicleta")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Bicicleta creada exitosamente",
            content = @Content(schema = @Schema(implementation = BikeResponseDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o código duplicado", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado - Credenciales inválidas", content = @Content)
    })
    @PostMapping
    public ResponseEntity<BikeResponseDTO> createBike(@Valid @RequestBody BikeCreateRequestDTO request) {
        var bike = bikeService.createBike(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bikeMapper.toDTO(bike));
    }

    @Operation(summary = "Actualizar estado de una bicicleta")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Estado actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = BikeResponseDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Transición de estado inválida", content = @Content),
        @ApiResponse(responseCode = "404", description = "Bicicleta no encontrada", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado - Credenciales inválidas", content = @Content)
    })
    @PutMapping("/{code}/status")
    public ResponseEntity<BikeResponseDTO> updateBikeStatus(
            @PathVariable String code,
            @Valid @RequestBody BikeUpdateStatusRequestDTO request) {
        
        var bike = bikeService.updateBikeStatus(code, request.getStatus());
        return ResponseEntity.ok(bikeMapper.toDTO(bike));
    }

    @Operation(summary = "Eliminar bicicleta (solo si está en mantenimiento)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Bicicleta eliminada exitosamente (sin contenido)", content = @Content),
        @ApiResponse(responseCode = "400", description = "No se puede eliminar - La bicicleta no está en mantenimiento", content = @Content),
        @ApiResponse(responseCode = "404", description = "Bicicleta no encontrada", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado - Credenciales inválidas", content = @Content)
    })
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteBike(@PathVariable String code) {
        bikeService.deleteBike(code);
        return ResponseEntity.noContent().build();
    }
}