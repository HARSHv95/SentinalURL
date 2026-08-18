package com.harsh.sentinal.scan.service.scan;

import com.harsh.sentinal.scan.common.enums.ScanSortOption;
import com.harsh.sentinal.scan.common.enums.ScanStatus;
import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.dto.ScanReport;
import com.harsh.sentinal.scan.dto.ScanRequest;
import com.harsh.sentinal.scan.dto.ScanResponse;
import com.harsh.sentinal.scan.dto.ShareLinkResponse;
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

    public ScanResponse getScanById(UUID scanId, UUID requestingUserId);

    public ResponseEntity<String> deleteScan(UUID scanId, UUID requestingUserId);

    public ShareLinkResponse shareScan(UUID scanId, UUID requestingUserId);

    public void unshareScan(UUID scanId, UUID requestingUserId);

    public ScanResponse getSharedScan(String shareToken);

    public String me();
}
