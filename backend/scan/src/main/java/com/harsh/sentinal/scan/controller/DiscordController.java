package com.harsh.sentinal.scan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harsh.sentinal.scan.integration.discord.DiscordInteractionRequest;
import com.harsh.sentinal.scan.integration.discord.DiscordInteractionResponse;
import com.harsh.sentinal.scan.security.discord.DiscordSignatureVerifier;
import com.harsh.sentinal.scan.service.discord.DiscordCommandService;
import com.harsh.sentinal.scan.util.UrlValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/discord")
@RequiredArgsConstructor
public class DiscordController {

    private static final int PING = 1;
    private static final int APPLICATION_COMMAND = 2;

    private final DiscordSignatureVerifier signatureVerifier;
    private final DiscordCommandService discordCommandService;
    private final ObjectMapper objectMapper;

    @PostMapping("/interactions")
    public ResponseEntity<DiscordInteractionResponse> handleInteraction(
            @RequestHeader("X-Signature-Ed25519") String signature,
            @RequestHeader("X-Signature-Timestamp") String timestamp,
            @RequestBody String rawBody) {

        if (!signatureVerifier.verify(signature, timestamp, rawBody)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        DiscordInteractionRequest interaction;
        try {
            interaction = objectMapper.readValue(rawBody, DiscordInteractionRequest.class);
        } catch (Exception e) {
            log.warn("Failed to parse Discord interaction payload", e);
            return ResponseEntity.badRequest().build();
        }

        if (interaction.type() == PING) {
            return ResponseEntity.ok(DiscordInteractionResponse.pong());
        }

        if (interaction.type() == APPLICATION_COMMAND && interaction.data() != null
                && "scan".equals(interaction.data().name())) {
            return handleScanCommand(interaction);
        }

        return ResponseEntity.ok(DiscordInteractionResponse.pong());
    }

    private ResponseEntity<DiscordInteractionResponse> handleScanCommand(DiscordInteractionRequest interaction) {
        String url = interaction.optionValue("url");

        if (url == null || !new UrlValidator().isValid(url, null)) {
            return ResponseEntity.ok(
                    DiscordInteractionResponse.ephemeralError("That doesn't look like a valid URL.")
            );
        }

        discordCommandService.handleScanCommand(url, interaction.token());

        return ResponseEntity.ok(DiscordInteractionResponse.deferred());
    }
}
