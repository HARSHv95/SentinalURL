package com.harsh.sentinal.scan.integration.discord;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Plain RestClient calls to Discord's REST API — no SDK, mirrors
 * GmailClient/OpenAIClient/VirusTotalClient. Command registration uses bot-token
 * auth; the follow-up webhook call uses only the interaction token embedded in
 * the URL path (confirmed against Discord's docs — no bot token needed there).
 */
@Service
public class DiscordClient {

    private final RestClient restClient;
    private final DiscordProperties properties;

    public DiscordClient(RestClient.Builder builder, DiscordProperties properties) {
        this.properties = properties;
        this.restClient = builder.baseUrl(properties.getApiBaseUrl()).build();
    }

    public void registerCommands() {
        boolean hasTestGuild = properties.getTestGuildId() != null && !properties.getTestGuildId().isBlank();

        String uri = hasTestGuild
                ? "/applications/" + properties.getApplicationId() + "/guilds/" + properties.getTestGuildId() + "/commands"
                : "/applications/" + properties.getApplicationId() + "/commands";

        restClient.put()
                .uri(uri)
                .header("Authorization", "Bot " + properties.getBotToken())
                .body(List.of(DiscordCommandDefinition.scanCommand()))
                .retrieve()
                .toBodilessEntity();
    }

    public void sendFollowup(String interactionToken, DiscordEmbed embed) {
        restClient.patch()
                .uri("/webhooks/{applicationId}/{interactionToken}/messages/@original",
                        properties.getApplicationId(), interactionToken)
                .body(new DiscordFollowupRequest(List.of(embed)))
                .retrieve()
                .toBodilessEntity();
    }
}
