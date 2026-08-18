package com.harsh.sentinal.scan.service.scan;

import com.harsh.sentinal.scan.common.enums.ScanSortOption;
import com.harsh.sentinal.scan.common.enums.ScanStatus;
import com.harsh.sentinal.scan.common.enums.ShareVisibility;
import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.dto.*;
import com.harsh.sentinal.scan.entity.Scan;
import com.harsh.sentinal.scan.exception.ResourceNotFoundException;
import com.harsh.sentinal.scan.repository.AIAnalysisRepo;
import com.harsh.sentinal.scan.repository.AnalysisRepo;
import com.harsh.sentinal.scan.repository.DomainIntelligenceRepo;
import com.harsh.sentinal.scan.repository.ProviderResultRepo;
import com.harsh.sentinal.scan.repository.ScanRepo;
import com.harsh.sentinal.scan.repository.specification.ScanSpecifications;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;
import com.harsh.sentinal.scan.service.background.BackService;
import com.harsh.sentinal.scan.util.RiskScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScanServiceImplementation implements ScanService {

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 50;

    private final ScanRepo scanRepo;
    private final AnalysisRepo analysisRepo;
    private final ProviderResultRepo providerResultRepo;
    private final DomainIntelligenceRepo domainIntelligenceRepo;
    private final AIAnalysisRepo aiAnalysisRepo;
    private final BackService backService;

    @Override
    public ScanResponse createScan(ScanRequest scanRequest, CustomUserDetails customUserDetails) {

        Scan newScan = new Scan();
        newScan.setUserId(customUserDetails.getUserId());
        newScan.setUrl(scanRequest.url());
        newScan.setStatus(ScanStatus.PENDING);

        scanRepo.save(newScan);

        backService.processScan(newScan.getId(), newScan.getUrl());

        ScanResponse response = new ScanResponse();
        response.setScanId(newScan.getId());
        response.setUrl(newScan.getUrl());
        response.setStatus(newScan.getStatus().name());
        response.setCreatedAt(LocalDateTime.ofInstant(newScan.getCreatedAt(), ZoneId.of("Asia/Kolkata")));
        response.setThreatIntelligence(List.of());

        return response;
    }

    @Override
    public void createScanFromBatch(String url, UUID userId, UUID emailScanBatchId) {
        Scan newScan = new Scan();
        newScan.setUserId(userId);
        newScan.setUrl(url);
        newScan.setStatus(ScanStatus.PENDING);
        newScan.setEmailScanBatchId(emailScanBatchId);

        scanRepo.save(newScan);

        backService.processScan(newScan.getId(), newScan.getUrl());
    }

    @Override
    public Page<ScanReport> getAllScans(
            CustomUserDetails userDetails,
            int page,
            int size,
            String search,
            ScanStatus status,
            Verdict verdict,
            ScanSortOption sort,
            UUID emailScanBatchId) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, MIN_PAGE_SIZE), MAX_PAGE_SIZE);

        Specification<Scan> spec = Specification.allOf(
                ScanSpecifications.belongsToUser(userDetails.getUserId()),
                ScanSpecifications.urlContains(search),
                ScanSpecifications.hasStatus(status),
                ScanSpecifications.hasVerdict(verdict),
                ScanSpecifications.belongsToBatch(emailScanBatchId)
        );

        PageRequest pageRequest = PageRequest.of(safePage, safeSize, sort.toSort());

        return scanRepo.findAll(spec, pageRequest).map(ScanReport::from);
    }

    @Override
    public ScanResponse getScanById(UUID scanId, UUID requestingUserId) {
        Scan scan = findOwnedScan(scanId, requestingUserId);

        ScanResponse response = new ScanResponse();
        response.setScanId(scan.getId());
        response.setUrl(scan.getUrl());
        response.setStatus(scan.getStatus().name());
        response.setCreatedAt(LocalDateTime.ofInstant(
                scan.getCreatedAt(),
                ZoneId.of("Asia/Kolkata")
        ));
        response.setShared(scan.getShareVisibility() == ShareVisibility.PUBLIC);
        response.setShareToken(scan.getShareToken());

        if (scan.getStatus() == ScanStatus.COMPLETED) {
            AnalysisReport report = analysisRepo.getAnalysisReportByScanId(scanId);
            response.setAnalysisReport(report);

            // "report" is only present when VirusTotal contributed to this scan.
            // Other providers (e.g. URLHaus) can complete a scan on their own,
            // so fall back to the already-persisted score/verdict on the scan itself.
            response.setRiskReport(report != null
                    ? RiskScoreCalculator.calculate(report)
                    : new RiskReport(scan.getRiskScore(), scan.getVerdict(), 0, List.of()));

            response.setThreatIntelligence(
                    providerResultRepo.findByScanId(scanId).stream()
                            .map(ProviderResultResponse::from)
                            .toList()
            );

            domainIntelligenceRepo.findFirstByScanId(scanId)
                    .map(DomainIntelligenceResponse::from)
                    .ifPresent(response::setDomainIntelligence);

            aiAnalysisRepo.findFirstByScanId(scanId)
                    .map(AIAnalysisResponse::from)
                    .ifPresent(response::setAiAnalysis);
        } else {
            response.setAnalysisReport(null);
            response.setRiskReport(null);
            response.setThreatIntelligence(List.of());
        }

        return response;
    }

    @Override
    public ResponseEntity<String> deleteScan(UUID scanId, UUID requestingUserId) {
        findOwnedScan(scanId, requestingUserId);
        scanRepo.deleteById(scanId);
        return new ResponseEntity<>("Scan Deleted Successfully!!! ", HttpStatus.OK);
    }

    @Override
    public ShareLinkResponse shareScan(UUID scanId, UUID requestingUserId) {
        Scan scan = findOwnedScan(scanId, requestingUserId);

        if (scan.getShareToken() == null) {
            scan.setShareToken(UUID.randomUUID().toString());
        }
        scan.setShareVisibility(ShareVisibility.PUBLIC);
        scanRepo.save(scan);

        return new ShareLinkResponse(scan.getShareToken());
    }

    @Override
    public void unshareScan(UUID scanId, UUID requestingUserId) {
        Scan scan = findOwnedScan(scanId, requestingUserId);

        // Clear the token (not just flip visibility) so a re-share always mints
        // a fresh link — a previously leaked link can never work again.
        scan.setShareVisibility(ShareVisibility.PRIVATE);
        scan.setShareToken(null);
        scanRepo.save(scan);
    }

    @Override
    public ScanResponse getSharedScan(String shareToken) {
        Scan scan = scanRepo.findByShareTokenAndShareVisibility(shareToken, ShareVisibility.PUBLIC)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found."));

        return getScanById(scan.getId(), scan.getUserId());
    }

    @Override
    public String me() {
        return "Scan Successfull";
    }

    private Scan findOwnedScan(UUID scanId, UUID requestingUserId) {
        Scan scan = scanRepo.findById(scanId)
                .orElseThrow(() -> new ResourceNotFoundException("Scan not found."));

        if (!scan.getUserId().equals(requestingUserId)) {
            throw new ResourceNotFoundException("Scan not found.");
        }

        return scan;
    }
}
