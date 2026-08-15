package com.harsh.sentinal.scan.integration.urlhaus;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "urlhaus")
@Component
public class URLHausProperties {
    private String authKey;
    private String baseUrl;
}
