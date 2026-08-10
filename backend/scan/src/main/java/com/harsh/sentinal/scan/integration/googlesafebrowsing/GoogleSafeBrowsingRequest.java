package com.harsh.sentinal.scan.integration.googlesafebrowsing;

import java.util.List;

public record GoogleSafeBrowsingRequest(
        Client client,
        ThreatInfo threatInfo
) {
    public record Client(String clientId, String clientVersion) {}

    public record ThreatInfo(
            List<String> threatTypes,
            List<String> platformTypes,
            List<String> threatEntryTypes,
            List<ThreatEntry> threatEntries
    ) {}

    public record ThreatEntry(String url) {}

    public static GoogleSafeBrowsingRequest forUrl(String url) {
        return new GoogleSafeBrowsingRequest(
                new Client("sentinalurl", "1.0.0"),
                new ThreatInfo(
                        List.of("MALWARE", "SOCIAL_ENGINEERING", "UNWANTED_SOFTWARE", "POTENTIALLY_HARMFUL_APPLICATION"),
                        List.of("ANY_PLATFORM"),
                        List.of("URL"),
                        List.of(new ThreatEntry(url))
                )
        );
    }
}
