package com.harsh.sentinal.scan.integration.abuseipdb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "abuseipdb")
@Component
public class AbuseIPDBProperties {
    private String apiKey;
    private String baseUrl;
}
