package com.pruebaTecnica.bike_rental_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bike {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String code;

        @Enumerated(EnumType.STRING)
        private BikeType type;

        @Enumerated(EnumType.STRING)
        private BikeStatus status;
}