package com.harsh.sentinal.scan.controller;

import com.harsh.sentinal.scan.dto.EmailScanBatchResponse;
import com.harsh.sentinal.scan.repository.EmailScanBatchRepo;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email-batches")
@RequiredArgsConstructor
public class EmailScanBatchController {

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 50;

    private final EmailScanBatchRepo emailScanBatchRepo;

    @GetMapping("/all")
    public Page<EmailScanBatchResponse> getAllBatches(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, MIN_PAGE_SIZE), MAX_PAGE_SIZE);
        PageRequest pageRequest = PageRequest.of(safePage, safeSize);

        return emailScanBatchRepo.findByUserIdOrderByCreatedAtDesc(userDetails.getUserId(), pageRequest)
                .map(EmailScanBatchResponse::from);
    }
}
