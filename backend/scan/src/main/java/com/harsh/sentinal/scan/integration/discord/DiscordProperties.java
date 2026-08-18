package com.harsh.sentinal.scan.integration.discord;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "discord")
@Component
public class DiscordProperties {
    private String applicationId;
    private String publicKey;
    private String botToken;
    private String apiBaseUrl;
    private String testGuildId;
}
