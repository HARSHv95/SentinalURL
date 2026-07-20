package com.harsh.sentinal.scan.integration.virustotal;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "virustotal")
@Component
public class VirusTotalProperties {
    private String apiKey;
    private String baseUrl;
}
