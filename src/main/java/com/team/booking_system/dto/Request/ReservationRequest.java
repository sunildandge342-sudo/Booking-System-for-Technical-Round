package com.team.booking_system.dto.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ReservationRequest {

    @NotNull(message = "Resource ID is required")
    private Long resourceId;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Start time is required")
    private Instant startTime;

    @NotNull(message = "End time is required")
    private Instant endTime;
}