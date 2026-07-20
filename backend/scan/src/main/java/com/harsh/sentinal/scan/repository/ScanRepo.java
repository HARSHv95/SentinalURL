package com.harsh.sentinal.scan.repository;

import com.harsh.sentinal.scan.dto.ScanReport;
import com.harsh.sentinal.scan.dto.ScanResponse;
import com.harsh.sentinal.scan.entity.Scan;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public interface ScanRepo extends JpaRepository<Scan, UUID> {

    final String GET_ALL_SCANS = """
    SELECT new com.harsh.sentinal.scan.dto.ScanReport(
        s.id,
        s.url,
        s.status,
        s.risk_score,
        s.created_at
    )
    FROM Scan s
    WHERE s.user_id = :userId
    """;

    final String GET_SCANBYID = """
    SELECT new com.harsh.sentinal.scan.dto.ScanReport(
        s.id,
        s.url,
        s.status,
        s.risk_score,
        s.created_at
    )
    FROM Scan s
    WHERE s.id = :scanId
    """;

    @Query(value = GET_ALL_SCANS)
    public Page<ScanReport> getAllScans(
            @Param(value = "userId") UUID userId,
            Pageable pageable);


    @Query(value = GET_SCANBYID)
    public ScanReport getScanById(@Param(value = "scanId") UUID scanId);

}
