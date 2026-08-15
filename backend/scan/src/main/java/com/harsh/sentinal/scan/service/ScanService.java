package com.harsh.sentinal.scan.service;

import com.harsh.sentinal.scan.common.enums.ScanSortOption;
import com.harsh.sentinal.scan.common.enums.ScanStatus;
import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.dto.ScanReport;
import com.harsh.sentinal.scan.dto.ScanRequest;
import com.harsh.sentinal.scan.dto.ScanResponse;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ScanService {
    public ScanResponse createScan(ScanRequest scanRequest, CustomUserDetails customUserDetails);

    public void createScanFromBatch(String url, UUID userId, UUID emailScanBatchId);

    public Page<ScanReport> getAllScans(
            CustomUserDetails userDetails,
            int page,
            int size,
            String search,
            ScanStatus status,
            Verdict verdict,
            ScanSortOption sort,
            UUID emailScanBatchId);

    public ScanResponse getScanById(UUID scanId);

    public ResponseEntity<String> deleteScan(UUID scanId);

    public String me();
}
