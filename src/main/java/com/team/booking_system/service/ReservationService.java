package com.team.booking_system.service;

import com.team.booking_system.dto.Request.ReservationRequest;
import com.team.booking_system.dto.Request.ReservationUpdateRequest;
import com.team.booking_system.dto.Response.ReservationResponse;
import com.team.booking_system.entity.*;
import com.team.booking_system.exception.ConflictException;
import com.team.booking_system.exception.ResourceNotFoundException;
import com.team.booking_system.exception.UnauthorizedException;
import com.team.booking_system.repository.ReservationRepository;
import com.team.booking_system.repository.ResourceRepository;
import com.team.booking_system.specification.ReservationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ResourceRepository resourceRepository;

    public Page<ReservationResponse> listReservations(
            User currentUser,
            ReservationStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable) {

        // ADMIN sees all; USER sees only own
        Long userId = currentUser.getRole() == Role.ADMIN ? null : currentUser.getId();

        Specification<Reservation> spec =
                ReservationSpecification.filter(userId, status, minPrice, maxPrice);

        return reservationRepository.findAll(spec, pageable)
                .map(ReservationResponse::from);
    }

    public ReservationResponse findById(Long id, User currentUser) {
        Reservation reservation = getOrThrow(id);
        assertAccessible(reservation, currentUser);
        return ReservationResponse.from(reservation);
    }

    @Transactional
    public ReservationResponse create(ReservationRequest req, User currentUser) {
        Resource resource = resourceRepository.findById(req.getResourceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resource not found with id: " + req.getResourceId()));

        if (!resource.isActive()) {
            throw new ConflictException("Resource is not active");
        }

        if (req.getEndTime().isBefore(req.getStartTime()) ||
                req.getEndTime().equals(req.getStartTime())) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }

        // Overlap check for CONFIRMED reservations
        boolean overlap = reservationRepository.existsOverlap(
                resource.getId(), req.getStartTime(), req.getEndTime(), null);
        if (overlap) {
            throw new ConflictException(
                    "Resource already has a confirmed reservation in the requested time range");
        }

        Reservation reservation = Reservation.builder()
                .resource(resource)
                .user(currentUser)
                .price(req.getPrice())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .status(ReservationStatus.PENDING)
                .build();

        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    @Transactional
    public ReservationResponse update(Long id, ReservationUpdateRequest req, User currentUser) {
        Reservation reservation = getOrThrow(id);
        assertAccessible(reservation, currentUser);

        if (req.getStatus() != null) {
            // Only ADMIN can CONFIRM/CANCEL freely; USER can only cancel own
            if (req.getStatus() == ReservationStatus.CONFIRMED
                    && currentUser.getRole() != Role.ADMIN) {
                throw new UnauthorizedException("Only ADMIN can confirm reservations");
            }
            reservation.setStatus(req.getStatus());
        }

        if (req.getPrice() != null) {
            reservation.setPrice(req.getPrice());
        }

        if (req.getStartTime() != null) reservation.setStartTime(req.getStartTime());
        if (req.getEndTime()   != null) reservation.setEndTime(req.getEndTime());

        // Re-check overlap if times changed and status is/becomes CONFIRMED
        if ((req.getStartTime() != null || req.getEndTime() != null)
                && reservation.getStatus() == ReservationStatus.CONFIRMED) {
            boolean overlap = reservationRepository.existsOverlap(
                    reservation.getResource().getId(),
                    reservation.getStartTime(),
                    reservation.getEndTime(),
                    reservation.getId());
            if (overlap) {
                throw new ConflictException("Updated times conflict with another confirmed reservation");
            }
        }

        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    @Transactional
    public void delete(Long id, User currentUser) {
        Reservation reservation = getOrThrow(id);
        assertAccessible(reservation, currentUser);
        reservationRepository.delete(reservation);
    }

    // ── helpers ─────────────────────────────────────────────────────────────

    private Reservation getOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reservation not found with id: " + id));
    }

    private void assertAccessible(Reservation reservation, User currentUser) {
        if (currentUser.getRole() == Role.ADMIN) return;
        if (!reservation.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You do not have access to this reservation");
        }
    }
}