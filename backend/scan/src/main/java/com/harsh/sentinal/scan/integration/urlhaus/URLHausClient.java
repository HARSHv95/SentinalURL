package com.harsh.sentinal.scan.integration.urlhaus;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class URLHausClient {

    private final RestClient restClient;
    private final URLHausProperties properties;

    public URLHausClient(RestClient.Builder builder, URLHausProperties properties) {
        this.properties = properties;
        this.restClient = builder
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    public URLHausResponse lookup(String url) {

        URLHausResponse response = restClient.post()
                .uri("/url/")
                .header("Auth-Key", properties.getAuthKey())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("url=" + url)
                .retrieve()
                .body(URLHausResponse.class);

        if (response == null) {
            throw new IllegalStateException("Failed to retrieve URLHaus lookup response.");
        }

        return response;
    }
}
