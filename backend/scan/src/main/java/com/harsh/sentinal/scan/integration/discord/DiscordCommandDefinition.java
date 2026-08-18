package com.harsh.sentinal.scan.integration.discord;

import java.util.List;

public record DiscordCommandDefinition(
        String name,
        String description,
        int type,
        List<Option> options
) {
    private static final int CHAT_INPUT = 1;
    private static final int STRING_OPTION = 3;

    public record Option(String name, String description, int type, boolean required) {}

    public static DiscordCommandDefinition scanCommand() {
        return new DiscordCommandDefinition(
                "scan",
                "Check a URL for security threats",
                CHAT_INPUT,
                List.of(new Option("url", "The URL to check", STRING_OPTION, true))
        );
    }
}
