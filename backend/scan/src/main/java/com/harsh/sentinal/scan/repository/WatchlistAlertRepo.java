package com.harsh.sentinal.scan.repository;

import com.harsh.sentinal.scan.entity.WatchlistAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WatchlistAlertRepo extends JpaRepository<WatchlistAlert, UUID> {

    Page<WatchlistAlert> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    Page<WatchlistAlert> findByUserIdAndReadFalseOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    long countByUserIdAndReadFalse(UUID userId);

    Optional<WatchlistAlert> findByIdAndUserId(UUID id, UUID userId);
}
