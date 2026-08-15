package com.harsh.sentinal.scan.integration.phishtank;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "phishtank")
@Component
public class PhishTankProperties {
    /** Optional — works without one, just with tighter rate limits. */
    private String appKey;
    private String baseUrl;
}
