package com.harsh.sentinal.scan.service;

import com.harsh.sentinal.scan.dto.ScanReport;
import com.harsh.sentinal.scan.dto.ScanRequest;
import com.harsh.sentinal.scan.dto.ScanResponse;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ScanService {
    public ScanResponse createScan(ScanRequest scanRequest, CustomUserDetails customUserDetails);

    public Page<ScanReport> getAllScans(CustomUserDetails userDetails, Pageable pageable);

    public ScanResponse getScanById(UUID scanId);

    public ResponseEntity<String> deleteScan(UUID scanId);

    public String me();
}
