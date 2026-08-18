package com.harsh.sentinal.scan.integration.discord;

import java.util.List;

public record DiscordEmbed(
        String title,
        String description,
        int color,
        List<Field> fields
) {
    public record Field(String name, String value, boolean inline) {}
}
