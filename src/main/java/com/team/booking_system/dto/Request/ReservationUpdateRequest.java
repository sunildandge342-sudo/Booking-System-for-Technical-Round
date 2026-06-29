package com.team.booking_system.dto.Request;

import com.team.booking_system.entity.ReservationStatus;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ReservationUpdateRequest {
    private ReservationStatus status;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    private Instant startTime;
    private Instant endTime;
}