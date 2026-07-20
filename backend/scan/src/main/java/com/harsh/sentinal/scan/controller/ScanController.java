package com.harsh.sentinal.scan.controller;

import com.harsh.sentinal.scan.dto.AnalysisResponse;
import com.harsh.sentinal.scan.dto.ScanReport;
import com.harsh.sentinal.scan.dto.ScanRequest;
import com.harsh.sentinal.scan.dto.ScanResponse;
import com.harsh.sentinal.scan.integration.virustotal.VirusTotalClient;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;
import com.harsh.sentinal.scan.service.ScanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/scan")
@RequiredArgsConstructor
public class ScanController {

    @Autowired
    private ScanService scanService;

    private final VirusTotalClient virusTotalClient;

    @GetMapping("/me")
    public String me(){
        return scanService.me();
    }

    @PostMapping("/create")
    public ResponseEntity<ScanResponse> createScan(
            @Valid @RequestBody ScanRequest scanInfo,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(scanService.createScan(scanInfo, userDetails));
    }

    @GetMapping("/all")
    public Page<ScanReport> getAllScans(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable){
        return scanService.getAllScans(userDetails, pageable);
    }

    @GetMapping("/id")
    public ScanResponse getScanByID(
            @Valid @RequestParam UUID scanId,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        return scanService.getScanById(scanId);
    }

    @GetMapping("/delete")
    public ResponseEntity<String> deleteScan(
            @Valid @RequestParam UUID scanId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return scanService.deleteScan(scanId);
    }

    @GetMapping("/test")
    public AnalysisResponse test() {

        String id = virusTotalClient.submitUrl("https://google.com");

        return virusTotalClient.getAnalysis(id);
    }

}
