package com.harsh.sentinal.scan.repository;

import com.harsh.sentinal.scan.entity.GmailConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GmailConnectionRepo extends JpaRepository<GmailConnection, UUID> {

    Optional<GmailConnection> findByUserId(UUID userId);

    void deleteByUserId(UUID userId);
}
