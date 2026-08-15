package com.harsh.sentinal.scan.service.watchlist.Implementation;

import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.common.enums.WatchlistSortOption;
import com.harsh.sentinal.scan.dto.WatchlistAddRequest;
import com.harsh.sentinal.scan.dto.WatchlistAlertResponse;
import com.harsh.sentinal.scan.dto.WatchlistItemResponse;
import com.harsh.sentinal.scan.entity.WatchlistAlert;
import com.harsh.sentinal.scan.entity.WatchlistItem;
import com.harsh.sentinal.scan.exception.DuplicateWatchlistItemException;
import com.harsh.sentinal.scan.exception.ResourceNotFoundException;
import com.harsh.sentinal.scan.repository.WatchlistAlertRepo;
import com.harsh.sentinal.scan.repository.WatchlistItemRepo;
import com.harsh.sentinal.scan.repository.specification.WatchlistItemSpecifications;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;
import com.harsh.sentinal.scan.service.watchlist.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WatchlistServiceImplementation implements WatchlistService {

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 50;

    private final WatchlistItemRepo watchlistItemRepo;
    private final WatchlistAlertRepo watchlistAlertRepo;

    @Override
    public WatchlistItemResponse addItem(WatchlistAddRequest request, CustomUserDetails userDetails) {
        if (watchlistItemRepo.existsByUserIdAndUrl(userDetails.getUserId(), request.url())) {
            throw new DuplicateWatchlistItemException("This URL is already on your watchlist.");
        }

        WatchlistItem item = new WatchlistItem();
        item.setUserId(userDetails.getUserId());
        item.setUrl(request.url());
        item.setHostname(extractHostname(request.url()));
        item.setActive(true);

        watchlistItemRepo.save(item);

        return WatchlistItemResponse.from(item);
    }

    @Override
    public Page<WatchlistItemResponse> getAllItems(
            CustomUserDetails userDetails,
            int page,
            int size,
            String search,
            Verdict verdict,
            WatchlistSortOption sort) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, MIN_PAGE_SIZE), MAX_PAGE_SIZE);

        Specification<WatchlistItem> spec = Specification.allOf(
                WatchlistItemSpecifications.belongsToUser(userDetails.getUserId()),
                WatchlistItemSpecifications.urlContains(search),
                WatchlistItemSpecifications.hasVerdict(verdict)
        );

        PageRequest pageRequest = PageRequest.of(safePage, safeSize, sort.toSort());

        return watchlistItemRepo.findAll(spec, pageRequest).map(WatchlistItemResponse::from);
    }

    @Override
    public void removeItem(UUID itemId, CustomUserDetails userDetails) {
        WatchlistItem item = watchlistItemRepo.findByIdAndUserId(itemId, userDetails.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist item not found."));

        watchlistItemRepo.delete(item);
    }

    @Override
    public Page<WatchlistAlertResponse> getAlerts(
            CustomUserDetails userDetails,
            int page,
            int size,
            boolean unreadOnly) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, MIN_PAGE_SIZE), MAX_PAGE_SIZE);
        PageRequest pageRequest = PageRequest.of(safePage, safeSize);

        Page<WatchlistAlert> alerts = unreadOnly
                ? watchlistAlertRepo.findByUserIdAndReadFalseOrderByCreatedAtDesc(userDetails.getUserId(), pageRequest)
                : watchlistAlertRepo.findByUserIdOrderByCreatedAtDesc(userDetails.getUserId(), pageRequest);

        return alerts.map(WatchlistAlertResponse::from);
    }

    @Override
    public long getUnreadAlertCount(CustomUserDetails userDetails) {
        return watchlistAlertRepo.countByUserIdAndReadFalse(userDetails.getUserId());
    }

    @Override
    public void markAlertRead(UUID alertId, CustomUserDetails userDetails) {
        WatchlistAlert alert = watchlistAlertRepo.findByIdAndUserId(alertId, userDetails.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found."));

        alert.setRead(true);
        watchlistAlertRepo.save(alert);
    }

    private String extractHostname(String url) {
        try {
            return URI.create(url).getHost();
        } catch (Exception e) {
            return null;
        }
    }
}
