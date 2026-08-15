package com.harsh.sentinal.scan.repository;

import com.harsh.sentinal.scan.dto.AnalysisReport;
import com.harsh.sentinal.scan.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AnalysisRepo extends JpaRepository<Analysis, String> {

    final String getAnalysisByScanId = """
            SELECT
                status,
                malicious,
                harmless,
                suspicious,
                undetected
            FROM analysis
            WHERE scan_id = :scan_id;
            """;

    @Query(value = getAnalysisByScanId, nativeQuery = true)
    public AnalysisReport getAnalysisReportByScanId(@Param("scan_id") UUID scan_id);
}
