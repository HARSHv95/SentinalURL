package com.harsh.sentinal.scan.service.threat.provider;

import com.harsh.sentinal.scan.common.enums.ProviderStatus;
import com.harsh.sentinal.scan.integration.abuseipdb.AbuseIPDBClient;
import com.harsh.sentinal.scan.integration.abuseipdb.AbuseIPDBProperties;
import com.harsh.sentinal.scan.integration.abuseipdb.AbuseIPDBResponse;
import com.harsh.sentinal.scan.service.threat.ThreatProvider;
import com.harsh.sentinal.scan.service.threat.ThreatProviderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.URI;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AbuseIPDBProvider implements ThreatProvider {

    private static final String PROVIDER_NAME = "ABUSEIPDB";
    private static final int MALICIOUS_THRESHOLD = 50;

    private final AbuseIPDBClient client;
    private final AbuseIPDBProperties properties;

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public ThreatProviderResult analyze(String url) {
        if (properties.getApiKey() == null || properties.getApiKey().isBlank()) {
            return ThreatProviderResult.unavailable(PROVIDER_NAME, "AbuseIPDB check skipped: no API key configured");
        }

        String ipAddress = resolveIp(url);
        if (ipAddress == null) {
            return ThreatProviderResult.unavailable(PROVIDER_NAME, "AbuseIPDB check skipped: could not resolve host to an IP address");
        }

        AbuseIPDBResponse response = client.check(ipAddress);
        AbuseIPDBResponse.Data data = response.data();

        boolean malicious = data.abuseConfidenceScore() >= MALICIOUS_THRESHOLD;

        String details = String.format(
                "AbuseIPDB confidence score %d/100 for %s (%d reports, ISP: %s)",
                data.abuseConfidenceScore(), ipAddress, data.totalReports(), data.isp()
        );

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

    private String resolveIp(String url) {
        try {
            String host = URI.create(url).getHost();
            if (host == null) {
                return null;
            }
            return InetAddress.getByName(host).getHostAddress();
        } catch (Exception e) {
            return null;
        }
    }
}
