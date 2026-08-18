package com.harsh.sentinal.scan.service.report;

import com.harsh.sentinal.scan.common.enums.ScanStatus;
import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.dto.*;
import com.harsh.sentinal.scan.entity.Scan;
import com.harsh.sentinal.scan.repository.ScanRepo;
import com.harsh.sentinal.scan.repository.ScanStatsRepo;
import com.harsh.sentinal.scan.repository.specification.ScanSpecifications;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImplementation implements ReportService {

    private static final int MIN_DAYS = 7;
    private static final int MAX_DAYS = 90;
    private static final int TOP_DOMAINS_LIMIT = 5;
    private static final int RECENT_THREATS_LIMIT = 5;
    private static final ZoneId ZONE = ZoneId.of("Asia/Kolkata");

    private static final String[] RISK_BUCKET_LABELS = {
            "0-9", "10-19", "20-29", "30-39", "40-49",
            "50-59", "60-69", "70-79", "80-89", "90-100"
    };

    private final ScanRepo scanRepo;
    private final ScanStatsRepo scanStatsRepo;

    @Override
    public ScanStatsResponse getStats(CustomUserDetails userDetails, int days) {
        UUID userId = userDetails.getUserId();
        int safeDays = Math.min(Math.max(days, MIN_DAYS), MAX_DAYS);

        ScanStatsResponse response = new ScanStatsResponse();
        response.setSummary(buildSummary(userId));
        response.setVerdictDistribution(buildVerdictDistribution(userId));
        response.setDailyScanCounts(buildDailyScanCounts(userId, safeDays));
        response.setRiskDistribution(buildRiskDistribution(userId));
        response.setTopRiskyDomains(buildTopRiskyDomains(userId));
        response.setRecentThreats(buildRecentThreats(userId));

        return response;
    }

    private ScanSummaryStats buildSummary(UUID userId) {
        long total = scanRepo.count(ScanSpecifications.belongsToUser(userId));

        long safe = scanRepo.count(Specification.allOf(
                ScanSpecifications.belongsToUser(userId),
                ScanSpecifications.hasVerdict(Verdict.SAFE)
        ));

        long suspicious = scanRepo.count(Specification.allOf(
                ScanSpecifications.belongsToUser(userId),
                ScanSpecifications.hasVerdictIn(List.of(Verdict.LOW_RISK, Verdict.MEDIUM_RISK))
        ));

        long malicious = scanRepo.count(Specification.allOf(
                ScanSpecifications.belongsToUser(userId),
                ScanSpecifications.hasVerdictIn(List.of(Verdict.HIGH_RISK, Verdict.CRITICAL))
        ));

        long pending = scanRepo.count(Specification.allOf(
                ScanSpecifications.belongsToUser(userId),
                ScanSpecifications.hasStatusIn(List.of(ScanStatus.PENDING, ScanStatus.IN_PROGRESS))
        ));

        return new ScanSummaryStats(total, safe, suspicious, malicious, pending);
    }

    private List<VerdictCount> buildVerdictDistribution(UUID userId) {
        Map<Verdict, Long> counts = scanStatsRepo.countByVerdict(userId).stream()
                .collect(Collectors.toMap(VerdictCount::getVerdict, VerdictCount::getCount));

        List<VerdictCount> result = new ArrayList<>();
        for (Verdict verdict : Verdict.values()) {
            result.add(new VerdictCount(verdict, counts.getOrDefault(verdict, 0L)));
        }
        return result;
    }

    private List<DailyScanCount> buildDailyScanCounts(UUID userId, int days) {
        LocalDate today = LocalDate.now(ZONE);
        LocalDate start = today.minusDays(days - 1L);
        Instant since = start.atStartOfDay(ZONE).toInstant();

        Map<LocalDate, Long> counts = scanStatsRepo.countByDay(userId, since).stream()
                .collect(Collectors.toMap(
                        ScanStatsRepo.DailyCountProjection::getScanDate,
                        ScanStatsRepo.DailyCountProjection::getScanCount
                ));

        List<DailyScanCount> result = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(today); date = date.plusDays(1)) {
            result.add(new DailyScanCount(date, counts.getOrDefault(date, 0L)));
        }
        return result;
    }

    private List<RiskBucketCount> buildRiskDistribution(UUID userId) {
        Map<Integer, Long> counts = scanStatsRepo.countByRiskBucket(userId).stream()
                .collect(Collectors.toMap(
                        ScanStatsRepo.RiskBucketProjection::getBucketIndex,
                        ScanStatsRepo.RiskBucketProjection::getBucketCount
                ));

        List<RiskBucketCount> result = new ArrayList<>();
        for (int i = 0; i < RISK_BUCKET_LABELS.length; i++) {
            result.add(new RiskBucketCount(RISK_BUCKET_LABELS[i], counts.getOrDefault(i, 0L)));
        }
        return result;
    }

    private List<TopRiskyDomain> buildTopRiskyDomains(UUID userId) {
        Map<String, List<Integer>> scoresByDomain = new HashMap<>();

        for (ScanStatsRepo.UrlRiskProjection row : scanStatsRepo.findUrlRiskPairs(userId)) {
            String domain = extractDomain(row.getUrl());
            if (domain == null) continue;
            scoresByDomain.computeIfAbsent(domain, key -> new ArrayList<>()).add(row.getRiskScore());
        }

        return scoresByDomain.entrySet().stream()
                .map(entry -> {
                    List<Integer> scores = entry.getValue();
                    double avg = scores.stream().mapToInt(Integer::intValue).average().orElse(0);
                    int max = scores.stream().mapToInt(Integer::intValue).max().orElse(0);
                    return new TopRiskyDomain(entry.getKey(), scores.size(), avg, max);
                })
                .sorted(Comparator.comparingInt(TopRiskyDomain::getMaxRiskScore).reversed()
                        .thenComparing(Comparator.comparingLong(TopRiskyDomain::getScanCount).reversed()))
                .limit(TOP_DOMAINS_LIMIT)
                .toList();
    }

    private String extractDomain(String url) {
        try {
            String host = URI.create(url).getHost();
            return host != null ? host : url;
        } catch (Exception e) {
            return null;
        }
    }

    private List<ScanReport> buildRecentThreats(UUID userId) {
        Specification<Scan> spec = Specification.allOf(
                ScanSpecifications.belongsToUser(userId),
                ScanSpecifications.hasVerdictIn(List.of(Verdict.HIGH_RISK, Verdict.CRITICAL))
        );

        PageRequest pageRequest = PageRequest.of(
                0, RECENT_THREATS_LIMIT, Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return scanRepo.findAll(spec, pageRequest).map(ScanReport::from).getContent();
    }
}
