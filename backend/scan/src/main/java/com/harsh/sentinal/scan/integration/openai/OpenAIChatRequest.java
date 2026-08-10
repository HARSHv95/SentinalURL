package com.harsh.sentinal.scan.integration.openai;

import java.util.List;

public record OpenAIChatRequest(
        String model,
        List<Message> messages,
        ResponseFormat response_format,
        double temperature
) {
    public record Message(String role, String content) {}

    public record ResponseFormat(String type) {}
}
