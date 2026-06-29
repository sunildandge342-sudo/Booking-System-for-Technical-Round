package com.team.booking_system.dto.Response;

import com.team.booking_system.entity.Reservation;
import com.team.booking_system.entity.ReservationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ReservationResponse {
    private Long id;
    private Long resourceId;
    private String resourceName;
    private Long userId;
    private String username;
    private ReservationStatus status;
    private BigDecimal price;
    private Instant startTime;
    private Instant endTime;
    private Instant createdAt;
    private Instant updatedAt;

    public static ReservationResponse from(Reservation r) {
        ReservationResponse dto = new ReservationResponse();
        dto.setId(r.getId());
        dto.setResourceId(r.getResource().getId());
        dto.setResourceName(r.getResource().getName());
        dto.setUserId(r.getUser().getId());
        dto.setUsername(r.getUser().getUsername());
        dto.setStatus(r.getStatus());
        dto.setPrice(r.getPrice());
        dto.setStartTime(r.getStartTime());
        dto.setEndTime(r.getEndTime());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setUpdatedAt(r.getUpdatedAt());
        return dto;
    }
}