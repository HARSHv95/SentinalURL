package com.harsh.sentinal.scan.repository;

import com.harsh.sentinal.scan.entity.ProviderResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProviderResultRepo extends JpaRepository<ProviderResult, UUID> {
    List<ProviderResult> findByScanId(UUID scanId);
}
