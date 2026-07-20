package com.harsh.sentinal.scan.service.Implementation;

import com.harsh.sentinal.scan.common.enums.ScanStatus;
import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.dto.*;
import com.harsh.sentinal.scan.entity.Scan;
import com.harsh.sentinal.scan.integration.virustotal.VirusTotalClient;
import com.harsh.sentinal.scan.repository.AnalysisRepo;
import com.harsh.sentinal.scan.repository.ScanRepo;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;
import com.harsh.sentinal.scan.service.ScanService;
import com.harsh.sentinal.scan.service.background.BackService;
import com.harsh.sentinal.scan.util.UrlValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        newScan.setUser_id(customUserDetails.getUserId());
        newScan.setUrl(scanRequest.url());
        newScan.setStatus(ScanStatus.PENDING);

        scanRepo.save(newScan);

        backService.processScan(newScan.getId(), newScan.getUrl());

        return new ScanResponse(newScan.getId(), newScan.getUrl(), newScan.getStatus().name(), LocalDateTime.ofInstant(newScan.getCreated_at(), ZoneId.of("Asia/Kolkata")), null, null);
    }

    @Override
    public Page<ScanReport> getAllScans(CustomUserDetails userDetails, Pageable pageable) {
        return scanRepo.getAllScans(userDetails.getUserId(), pageable);
    }

    @Override
    public ScanResponse getScanById(UUID scanId) {
        ScanReport scan = scanRepo.getScanById(scanId);

        ScanResponse response = new ScanResponse();
        response.setScanId(scan.getScanId());
        response.setUrl(scan.getUrl());
        response.setStatus(scan.getScanStatus().name());
        response.setCreatedAt(LocalDateTime.ofInstant(
                scan.getCreated_at(),
                ZoneId.of("Asia/Kolkata")
        ));

        AnalysisReport report = analysisRepo.getAnalysisReportByScanId(scanId);
        response.setAnalysisReport(report);

        int score = (report.getMalicious() * 20) + (report.getSuspicious() * 10);
        score = Math.min(score,100);

        float total_engines = report.getHarmless() + report.getSuspicious() + report.getMalicious() + report.getUndetected();

        int confidence = Math.round((report.getMalicious() + report.getSuspicious()) * 100 / total_engines);

        RiskReport riskReport = new RiskReport();
        riskReport.setRiskScore(score);
        riskReport.setConfidence(confidence);

        if(score >= 0 && score <= 10){ riskReport.setVerdict(Verdict.SAFE);}
        else if(score >= 11 && score <= 30) { riskReport.setVerdict(Verdict.LOW_RISK);}
        else if(score >= 31 && score <= 60){ riskReport.setVerdict(Verdict.MEDIUM_RISK);}
        else if(score >= 71 && score <= 90){ riskReport.setVerdict(Verdict.HIGH_RISK);}
        else{ riskReport.setVerdict(Verdict.CRITICAL);}

        response.setRiskReport(riskReport);
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
