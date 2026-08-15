package com.harsh.sentinal.scan.service.threat.provider;

import com.harsh.sentinal.scan.common.enums.ProviderStatus;
import com.harsh.sentinal.scan.integration.openphish.OpenPhishFeedCache;
import com.harsh.sentinal.scan.service.threat.ThreatProvider;
import com.harsh.sentinal.scan.service.threat.ThreatProviderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OpenPhishProvider implements ThreatProvider {

    private static final String PROVIDER_NAME = "OPENPHISH";

    private final OpenPhishFeedCache feedCache;

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public ThreatProviderResult analyze(String url) {
        Set<String> feed = feedCache.getUrls();
        boolean malicious = feed.contains(url) || feed.contains(toggleTrailingSlash(url));

        String details = malicious
                ? "Listed in the OpenPhish community feed"
                : "Not found in the OpenPhish community feed";

        return new ThreatProviderResult(
                PROVIDER_NAME,
                ProviderStatus.AVAILABLE,
                malicious,
                false,
                null, null, null, null,
                List.of(),
                null,
                Instant.now(),
                details
        );
    }

    private String toggleTrailingSlash(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url + "/";
    }
}
