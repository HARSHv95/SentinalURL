package com.harsh.sentinal.scan.service.Implementation;

import com.harsh.sentinal.scan.common.enums.ScanSortOption;
import com.harsh.sentinal.scan.common.enums.ScanStatus;
import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.dto.*;
import com.harsh.sentinal.scan.entity.Scan;
import com.harsh.sentinal.scan.integration.virustotal.VirusTotalClient;
import com.harsh.sentinal.scan.repository.AnalysisRepo;
import com.harsh.sentinal.scan.repository.ScanRepo;
import com.harsh.sentinal.scan.repository.specification.ScanSpecifications;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;
import com.harsh.sentinal.scan.service.ScanService;
import com.harsh.sentinal.scan.service.background.BackService;
import com.harsh.sentinal.scan.util.RiskScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScanServiceImplementation implements ScanService {

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 50;

    @Autowired
    private ScanRepo scanRepo;

    @Autowired
    private AnalysisRepo analysisRepo;

    private final VirusTotalClient virusTotalClient;

    @Autowired
    private BackService backService;


    @Override
    public ScanResponse createScan(ScanRequest scanRequest, CustomUserDetails customUserDetails) {


        Scan newScan = new Scan();
        newScan.setUserId(customUserDetails.getUserId());
        newScan.setUrl(scanRequest.url());
        newScan.setStatus(ScanStatus.PENDING);

        scanRepo.save(newScan);

        backService.processScan(newScan.getId(), newScan.getUrl());

        return new ScanResponse(newScan.getId(), newScan.getUrl(), newScan.getStatus().name(), LocalDateTime.ofInstant(newScan.getCreatedAt(), ZoneId.of("Asia/Kolkata")), null, null);
    }

    @Override
    public Page<ScanReport> getAllScans(
            CustomUserDetails userDetails,
            int page,
            int size,
            String search,
            ScanStatus status,
            Verdict verdict,
            ScanSortOption sort) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, MIN_PAGE_SIZE), MAX_PAGE_SIZE);

        Specification<Scan> spec = Specification.allOf(
                ScanSpecifications.belongsToUser(userDetails.getUserId()),
                ScanSpecifications.urlContains(search),
                ScanSpecifications.hasStatus(status),
                ScanSpecifications.hasVerdict(verdict)
        );

        PageRequest pageRequest = PageRequest.of(safePage, safeSize, sort.toSort());

        return scanRepo.findAll(spec, pageRequest).map(ScanReport::from);
    }

    @Override
    public ScanResponse getScanById(UUID scanId) {
        ScanReport scan = scanRepo.getScanById(scanId);

        ScanResponse response = new ScanResponse();
        response.setScanId(scan.getScanId());
        response.setUrl(scan.getUrl());
        response.setStatus(scan.getScanStatus().name());
        response.setCreatedAt(LocalDateTime.ofInstant(
                scan.getCreatedAt(),
                ZoneId.of("Asia/Kolkata")
        ));

        if(scan.getScanStatus() == ScanStatus.COMPLETED){
            AnalysisReport report = analysisRepo.getAnalysisReportByScanId(scanId);
            response.setAnalysisReport(report);
            response.setRiskReport(RiskScoreCalculator.calculate(report));
        }
        else{
            response.setAnalysisReport(null);
            response.setRiskReport(null);
        }
        return response;
    }

    @Override
    public ResponseEntity<String> deleteScan(UUID scanId) {
        scanRepo.deleteById(scanId);
        return new ResponseEntity<>("Scan Deleted Successfully!!! ", HttpStatus.OK);
    }

    @Override
    public String me() {
        return "Scan Successfull";
    }
}
