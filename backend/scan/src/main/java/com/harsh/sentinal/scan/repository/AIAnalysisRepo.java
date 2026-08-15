package com.harsh.sentinal.scan.repository;

import com.harsh.sentinal.scan.entity.AIAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AIAnalysisRepo extends JpaRepository<AIAnalysis, UUID> {
    Optional<AIAnalysis> findFirstByScanId(UUID scanId);
}
