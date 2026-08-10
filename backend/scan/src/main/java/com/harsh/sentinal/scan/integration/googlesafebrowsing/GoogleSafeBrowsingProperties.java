package com.harsh.sentinal.scan.integration.googlesafebrowsing;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "google-safe-browsing")
@Component
public class GoogleSafeBrowsingProperties {
    private String apiKey;
    private String baseUrl;
}
