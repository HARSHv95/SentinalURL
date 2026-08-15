package com.harsh.sentinal.scan.integration.gmail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GmailMessageListResponse(
        List<MessageStub> messages,
        String nextPageToken,
        Integer resultSizeEstimate
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MessageStub(String id, String threadId) {}
}
