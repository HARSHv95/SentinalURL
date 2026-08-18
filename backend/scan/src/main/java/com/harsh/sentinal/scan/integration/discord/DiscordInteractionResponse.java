package com.harsh.sentinal.scan.integration.discord;

import java.util.List;

public record DiscordInteractionResponse(int type, ResponseData data) {

    private static final int PONG = 1;
    private static final int CHANNEL_MESSAGE_WITH_SOURCE = 4;
    private static final int DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE = 5;
    private static final int EPHEMERAL_FLAG = 64;

    public record ResponseData(String content, List<DiscordEmbed> embeds, Integer flags) {}

    public static DiscordInteractionResponse pong() {
        return new DiscordInteractionResponse(PONG, null);
    }

    public static DiscordInteractionResponse deferred() {
        return new DiscordInteractionResponse(DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE, null);
    }

    public static DiscordInteractionResponse ephemeralError(String message) {
        return new DiscordInteractionResponse(
                CHANNEL_MESSAGE_WITH_SOURCE,
                new ResponseData(message, null, EPHEMERAL_FLAG)
        );
    }
}
