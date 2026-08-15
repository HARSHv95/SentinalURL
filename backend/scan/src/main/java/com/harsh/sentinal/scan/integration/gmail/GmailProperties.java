package com.harsh.sentinal.scan.integration.gmail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "gmail")
@Component
public class GmailProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authUri;
    private String tokenUri;
    private String apiBaseUrl;
    private String scope;
    private String frontendSuccessUrl;
    private int syncIntervalMinutes;
    private int syncLookbackHours;
}
