package com.harsh.sentinal.scan.integration.openphish;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "openphish")
@Component
public class OpenPhishProperties {
    private String feedUrl;
}
