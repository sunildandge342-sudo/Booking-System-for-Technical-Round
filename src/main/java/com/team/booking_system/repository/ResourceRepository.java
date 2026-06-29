package com.team.booking_system.repository;

import com.team.booking_system.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Page<Resource> findAllByActiveTrue(Pageable pageable);
}