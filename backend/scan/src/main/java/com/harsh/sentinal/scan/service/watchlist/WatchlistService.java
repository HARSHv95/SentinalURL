package com.harsh.sentinal.scan.service.watchlist;

import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.common.enums.WatchlistSortOption;
import com.harsh.sentinal.scan.dto.WatchlistAddRequest;
import com.harsh.sentinal.scan.dto.WatchlistAlertResponse;
import com.harsh.sentinal.scan.dto.WatchlistItemResponse;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface WatchlistService {

    WatchlistItemResponse addItem(WatchlistAddRequest request, CustomUserDetails userDetails);

    Page<WatchlistItemResponse> getAllItems(
            CustomUserDetails userDetails,
            int page,
            int size,
            String search,
            Verdict verdict,
            WatchlistSortOption sort);

    void removeItem(UUID itemId, CustomUserDetails userDetails);

    Page<WatchlistAlertResponse> getAlerts(
            CustomUserDetails userDetails,
            int page,
            int size,
            boolean unreadOnly);

    long getUnreadAlertCount(CustomUserDetails userDetails);

    void markAlertRead(UUID alertId, CustomUserDetails userDetails);
}
