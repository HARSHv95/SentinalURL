package com.harsh.sentinal.scan.integration.openai;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class OpenAIClient {

    private static final double TEMPERATURE = 0.2;

    private final RestClient restClient;
    private final OpenAIProperties properties;

    public OpenAIClient(RestClient.Builder builder, OpenAIProperties properties) {
        this.properties = properties;
        this.restClient = builder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + properties.getApiKey())
                .build();
    }

    public String chatCompletion(String systemPrompt, String userPrompt) {
        OpenAIChatRequest request = new OpenAIChatRequest(
                properties.getModel(),
                List.of(
                        new OpenAIChatRequest.Message("system", systemPrompt),
                        new OpenAIChatRequest.Message("user", userPrompt)
                ),
                new OpenAIChatRequest.ResponseFormat("json_object"),
                TEMPERATURE
        );

        OpenAIChatResponse response = restClient.post()
                .uri("/v1/chat/completions")
                .body(request)
                .retrieve()
                .body(OpenAIChatResponse.class);

        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            throw new IllegalStateException("OpenAI returned no choices");
        }

        return response.choices().get(0).message().content();
    }
}
