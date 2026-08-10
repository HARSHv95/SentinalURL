package com.harsh.sentinal.scan.integration.openphish;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * OpenPhish's free community feed is a bulk text file (one URL per line),
 * refreshed by them every ~12h — not a per-URL lookup API. Fetching it on
 * every scan would be wasteful, so it's cached in memory and refreshed
 * lazily once the cache is older than {@link #TTL}.
 */
@Slf4j
@Component
public class OpenPhishFeedCache {

    private static final Duration TTL = Duration.ofHours(1);

    private final RestClient restClient;
    private final OpenPhishProperties properties;

    private final AtomicReference<Set<String>> cachedUrls = new AtomicReference<>(Set.of());
    private volatile Instant lastFetchedAt = Instant.EPOCH;
    private final Object refreshLock = new Object();

    public OpenPhishFeedCache(RestClient.Builder builder, OpenPhishProperties properties) {
        this.restClient = builder.build();
        this.properties = properties;
    }

    public Set<String> getUrls() {
        if (isStale()) {
            refresh();
        }
        return cachedUrls.get();
    }

    private boolean isStale() {
        return Duration.between(lastFetchedAt, Instant.now()).compareTo(TTL) > 0;
    }

    private void refresh() {
        synchronized (refreshLock) {
            if (!isStale()) {
                return;
            }

            try {
                String body = restClient.get()
                        .uri(properties.getFeedUrl())
                        .retrieve()
                        .body(String.class);

                if (body != null) {
                    Set<String> urls = Arrays.stream(body.split("\\R"))
                            .map(String::trim)
                            .filter(line -> !line.isBlank())
                            .collect(Collectors.toUnmodifiableSet());

                    cachedUrls.set(urls);
                }
            } catch (Exception e) {
                log.warn("Failed to refresh OpenPhish feed, continuing to serve stale/empty cache", e);
            } finally {
                // Reset the clock even on failure so a broken feed doesn't get
                // re-fetched on every single scan within the TTL window.
                lastFetchedAt = Instant.now();
            }
        }
    }
}
