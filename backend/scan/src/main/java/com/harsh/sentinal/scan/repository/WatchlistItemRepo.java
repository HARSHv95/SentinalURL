package com.harsh.sentinal.scan.repository;

import com.harsh.sentinal.scan.entity.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WatchlistItemRepo extends JpaRepository<WatchlistItem, UUID>, JpaSpecificationExecutor<WatchlistItem> {

    List<WatchlistItem> findByActiveTrue();

    Optional<WatchlistItem> findByIdAndUserId(UUID id, UUID userId);

    boolean existsByUserIdAndUrl(UUID userId, String url);
}
