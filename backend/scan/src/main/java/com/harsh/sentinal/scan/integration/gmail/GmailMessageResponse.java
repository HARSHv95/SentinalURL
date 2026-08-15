package com.harsh.sentinal.scan.integration.gmail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GmailMessageResponse(
        String id,
        String threadId,
        Payload payload
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Payload(String mimeType, List<Header> headers, Body body, List<Payload> parts) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Header(String name, String value) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Body(String data, Integer size) {}
}
