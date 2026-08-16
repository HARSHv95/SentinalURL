package com.harsh.sentinal.scan.repository;

import com.harsh.sentinal.scan.common.enums.ShareVisibility;
import com.harsh.sentinal.scan.entity.Scan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ScanRepo extends JpaRepository<Scan, UUID>, JpaSpecificationExecutor<Scan> {

    Optional<Scan> findByShareTokenAndShareVisibility(String shareToken, ShareVisibility shareVisibility);

}
