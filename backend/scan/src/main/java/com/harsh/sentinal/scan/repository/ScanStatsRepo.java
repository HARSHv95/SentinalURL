package com.harsh.sentinal.scan.repository;

import com.harsh.sentinal.scan.dto.VerdictCount;
import com.harsh.sentinal.scan.entity.Scan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ScanStatsRepo extends JpaRepository<Scan, UUID> {

    @Query("""
        SELECT new com.harsh.sentinal.scan.dto.VerdictCount(s.verdict, COUNT(s))
        FROM Scan s
        WHERE s.userId = :userId AND s.verdict IS NOT NULL
        GROUP BY s.verdict
        """)
    List<VerdictCount> countByVerdict(@Param("userId") UUID userId);

    @Query(value = """
        SELECT CAST(date_trunc('day', created_at) AS date) AS scanDate, COUNT(*) AS scanCount
        FROM scans
        WHERE user_id = :userId AND created_at >= :since
        GROUP BY scanDate
        ORDER BY scanDate
        """, nativeQuery = true)
    List<DailyCountProjection> countByDay(@Param("userId") UUID userId, @Param("since") Instant since);

    @Query(value = """
        SELECT LEAST(risk_score / 10, 9) AS bucketIndex, COUNT(*) AS bucketCount
        FROM scans
        WHERE user_id = :userId AND verdict IS NOT NULL
        GROUP BY bucketIndex
        ORDER BY bucketIndex
        """, nativeQuery = true)
    List<RiskBucketProjection> countByRiskBucket(@Param("userId") UUID userId);

    @Query("""
        SELECT s.url AS url, s.riskScore AS riskScore
        FROM Scan s
        WHERE s.userId = :userId AND s.verdict IS NOT NULL
        """)
    List<UrlRiskProjection> findUrlRiskPairs(@Param("userId") UUID userId);

    interface DailyCountProjection {
        LocalDate getScanDate();
        Long getScanCount();
    }

    interface RiskBucketProjection {
        Integer getBucketIndex();
        Long getBucketCount();
    }

    interface UrlRiskProjection {
        String getUrl();
        int getRiskScore();
    }
}
