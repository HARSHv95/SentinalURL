package com.harsh.sentinal.scan.controller;

import com.harsh.sentinal.scan.common.enums.ScanSortOption;
import com.harsh.sentinal.scan.common.enums.ScanStatus;
import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.dto.AnalysisResponse;
import com.harsh.sentinal.scan.dto.ScanReport;
import com.harsh.sentinal.scan.dto.ScanRequest;
import com.harsh.sentinal.scan.dto.ScanResponse;
import com.harsh.sentinal.scan.dto.ShareLinkResponse;
import com.harsh.sentinal.scan.integration.virustotal.VirusTotalClient;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;
import com.harsh.sentinal.scan.service.scan.ScanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ScanStatus status,
            @RequestParam(required = false) Verdict verdict,
            @RequestParam(defaultValue = "newest") ScanSortOption sort,
            @RequestParam(required = false) UUID emailScanBatchId){
        return scanService.getAllScans(userDetails, page, size, search, status, verdict, sort, emailScanBatchId);
    }

    @GetMapping("/id")
    public ScanResponse getScanByID(
            @Valid @RequestParam UUID scanId,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        return scanService.getScanById(scanId, userDetails.getUserId());
    }

    @GetMapping("/delete")
    public ResponseEntity<String> deleteScan(
            @Valid @RequestParam UUID scanId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return scanService.deleteScan(scanId, userDetails.getUserId());
    }

    @PostMapping("/share")
    public ShareLinkResponse shareScan(
            @Valid @RequestParam UUID scanId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return scanService.shareScan(scanId, userDetails.getUserId());
    }

    @PostMapping("/unshare")
    public ResponseEntity<String> unshareScan(
            @Valid @RequestParam UUID scanId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        scanService.unshareScan(scanId, userDetails.getUserId());
        return ResponseEntity.ok("Sharing disabled.");
    }

    @GetMapping("/shared/{shareToken}")
    public ScanResponse getSharedScan(@PathVariable String shareToken){
        return scanService.getSharedScan(shareToken);
    }

    @GetMapping("/test")
    public AnalysisResponse test() {

        String id = virusTotalClient.submitUrl("https://google.com");

        return virusTotalClient.getAnalysis(id);
    }

}
