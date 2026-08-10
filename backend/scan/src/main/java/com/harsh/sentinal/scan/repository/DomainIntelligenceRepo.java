package com.harsh.sentinal.scan.repository;

import com.harsh.sentinal.scan.entity.DomainIntelligence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DomainIntelligenceRepo extends JpaRepository<DomainIntelligence, UUID> {
    Optional<DomainIntelligence> findFirstByDomainOrderByCreatedAtDesc(String domain);

    Optional<DomainIntelligence> findFirstByScanId(UUID scanId);
}
