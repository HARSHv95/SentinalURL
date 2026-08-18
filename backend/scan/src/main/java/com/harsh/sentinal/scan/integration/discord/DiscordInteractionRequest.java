package com.harsh.sentinal.scan.integration.discord;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DiscordInteractionRequest(
        int type,
        String id,
        String token,
        Data data
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(String name, List<Option> options) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Option(String name, String value) {}

    public String optionValue(String optionName) {
        if (data == null || data.options() == null) {
            return null;
        }
        return data.options().stream()
                .filter(o -> optionName.equals(o.name()))
                .map(Option::value)
                .findFirst()
                .orElse(null);
    }
}
