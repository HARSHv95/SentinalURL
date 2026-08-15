package com.harsh.sentinal.scan.repository;

import com.harsh.sentinal.scan.dto.ScanReport;
import com.harsh.sentinal.scan.entity.Scan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ScanRepo extends JpaRepository<Scan, UUID>, JpaSpecificationExecutor<Scan> {

    final String GET_SCANBYID = """
    SELECT new com.harsh.sentinal.scan.dto.ScanReport(
        s.id,
        s.url,
        s.status,
        s.riskScore,
        s.verdict,
        s.createdAt
    )
    FROM Scan s
    WHERE s.id = :scanId
    """;

    @Query(value = GET_SCANBYID)
    public ScanReport getScanById(@Param(value = "scanId") UUID scanId);

}
