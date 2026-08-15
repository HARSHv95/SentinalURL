package com.harsh.sentinal.scan.repository;

import com.harsh.sentinal.scan.entity.EmailScanBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailScanBatchRepo extends JpaRepository<EmailScanBatch, UUID> {

    boolean existsByUserIdAndSourceMessageId(UUID userId, String sourceMessageId);

    Page<EmailScanBatch> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
}
