package com.team.booking_system.repository;

import com.team.booking_system.entity.Reservation;
import com.team.booking_system.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

    // overlap check: any CONFIRMED reservation for this resource overlapping [start, end]
    @Query("""
            SELECT COUNT(r) > 0 FROM Reservation r
            WHERE r.resource.id = :resourceId
              AND r.status = 'CONFIRMED'
              AND r.startTime < :endTime
              AND r.endTime   > :startTime
              AND (:excludeId IS NULL OR r.id <> :excludeId)
            """)
    boolean existsOverlap(@Param("resourceId") Long resourceId,
                          @Param("startTime")  Instant startTime,
                          @Param("endTime")    Instant endTime,
                          @Param("excludeId")  Long excludeId);
}