package com.team.booking_system.controller;

import com.team.booking_system.dto.Request.ReservationRequest;
import com.team.booking_system.dto.Request.ReservationUpdateRequest;
import com.team.booking_system.dto.Response.ReservationResponse;
import com.team.booking_system.entity.ReservationStatus;
import com.team.booking_system.entity.User;
import com.team.booking_system.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reservations", description = "Manage reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    @Operation(summary = "List reservations — ADMIN sees all, USER sees own")
    public ResponseEntity<Page<ReservationResponse>> list(
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(
                reservationService.listReservations(currentUser, status, minPrice, maxPrice, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a reservation by ID")
    public ResponseEntity<ReservationResponse> get(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(reservationService.findById(id, currentUser));
    }

    @PostMapping
    @Operation(summary = "Create a reservation")
    public ResponseEntity<ReservationResponse> create(
            @Valid @RequestBody ReservationRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.create(request, currentUser));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a reservation")
    public ResponseEntity<ReservationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ReservationUpdateRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(reservationService.update(id, request, currentUser));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a reservation")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        reservationService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}