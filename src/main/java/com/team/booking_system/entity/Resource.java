package com.team.booking_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "resources")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String type;

    @Column(length = 1000)
    private String description;

    private Integer capacity;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}