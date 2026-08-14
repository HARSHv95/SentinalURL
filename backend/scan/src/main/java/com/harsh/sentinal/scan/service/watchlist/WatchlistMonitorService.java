package com.harsh.sentinal.scan.service.watchlist;

import java.util.UUID;

public interface WatchlistMonitorService {

    void sweep();

    void checkItem(UUID watchlistItemId);

    void checkItemNow(UUID watchlistItemId);
}
