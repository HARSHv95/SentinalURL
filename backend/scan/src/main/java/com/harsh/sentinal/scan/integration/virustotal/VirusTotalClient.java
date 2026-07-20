package com.harsh.sentinal.scan.integration.virustotal;

import com.harsh.sentinal.scan.dto.AnalysisResponse;
import com.harsh.sentinal.scan.dto.SubmitUrlResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class VirusTotalClient {

    private final RestClient restClient;

    public VirusTotalClient(RestClient.Builder builder,
                            VirusTotalProperties properties) {

        this.restClient = builder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("x-apikey", properties.getApiKey())
                .build();
    }

    public String submitUrl(String url) {

        SubmitUrlResponse response = restClient.post()
                .uri("/urls")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("url=" + url)
                .retrieve()
                .body(SubmitUrlResponse.class);

        if (response == null || response.data() == null) {
            throw new RuntimeException("Failed to submit URL to VirusTotal.");
        }

        return response.data().id();
    }

    public AnalysisResponse getAnalysis(String analysisId) {

        AnalysisResponse response = restClient.get()
                .uri("/analyses/{id}", analysisId)
                .retrieve()
                .body(AnalysisResponse.class);

        if (response == null) {
            throw new RuntimeException("Failed to retrieve VirusTotal analysis.");
        }

        return response;
    }

}
