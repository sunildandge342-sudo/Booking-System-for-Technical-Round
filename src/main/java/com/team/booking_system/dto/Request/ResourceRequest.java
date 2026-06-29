package com.team.booking_system.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ResourceRequest {
    @NotBlank(message = "Name is required")
    private String name;
    private String type;
    private String description;
    @Positive
    private Integer capacity;
    private boolean active = true;
}