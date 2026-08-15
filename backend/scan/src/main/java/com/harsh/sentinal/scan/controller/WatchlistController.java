package com.harsh.sentinal.scan.controller;

import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.common.enums.WatchlistSortOption;
import com.harsh.sentinal.scan.dto.WatchlistAddRequest;
import com.harsh.sentinal.scan.dto.WatchlistAlertResponse;
import com.harsh.sentinal.scan.dto.WatchlistItemResponse;
import com.harsh.sentinal.scan.exception.ResourceNotFoundException;
import com.harsh.sentinal.scan.repository.WatchlistItemRepo;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;
import com.harsh.sentinal.scan.service.watchlist.WatchlistMonitorService;
import com.harsh.sentinal.scan.service.watchlist.WatchlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final WatchlistMonitorService watchlistMonitorService;
    private final WatchlistItemRepo watchlistItemRepo;

    @PostMapping("/add")
    public ResponseEntity<WatchlistItemResponse> addItem(
            @Valid @RequestBody WatchlistAddRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(watchlistService.addItem(request, userDetails));
    }

    @GetMapping("/all")
    public Page<WatchlistItemResponse> getAllItems(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Verdict verdict,
            @RequestParam(defaultValue = "newest") WatchlistSortOption sort) {
        return watchlistService.getAllItems(userDetails, page, size, search, verdict, sort);
    }

    @GetMapping("/remove")
    public ResponseEntity<String> removeItem(
            @Valid @RequestParam UUID itemId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        watchlistService.removeItem(itemId, userDetails);
        return ResponseEntity.ok("Watchlist item removed successfully.");
    }

    @GetMapping("/alerts")
    public Page<WatchlistAlertResponse> getAlerts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean unreadOnly) {
        return watchlistService.getAlerts(userDetails, page, size, unreadOnly);
    }

    @GetMapping("/alerts/unread-count")
    public long getUnreadAlertCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return watchlistService.getUnreadAlertCount(userDetails);
    }

    @PostMapping("/alerts/read")
    public ResponseEntity<String> markAlertRead(
            @Valid @RequestParam UUID alertId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        watchlistService.markAlertRead(alertId, userDetails);
        return ResponseEntity.ok("Alert marked as read.");
    }

    @PostMapping("/check-now")
    public ResponseEntity<String> checkNow(
            @Valid @RequestParam UUID itemId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        watchlistItemRepo.findByIdAndUserId(itemId, userDetails.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist item not found."));

        watchlistMonitorService.checkItemNow(itemId);
        return ResponseEntity.ok("Check completed.");
    }
}
