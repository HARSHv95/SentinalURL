package com.harsh.sentinal.scan.integration.openai;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "openai")
@Component
public class OpenAIProperties {
    private String apiKey;
    private String baseUrl;
    private String model;
}
