package com.harsh.sentinal.scan.config;

import com.harsh.sentinal.scan.integration.discord.DiscordClient;
import com.harsh.sentinal.scan.integration.discord.DiscordProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordCommandRegistrar implements CommandLineRunner {

    private final DiscordClient discordClient;
    private final DiscordProperties properties;

    @Override
    public void run(String... args) {
        if (properties.getPublicKey() == null || properties.getPublicKey().isBlank()) {
            log.info("Discord not configured (discord.public-key is blank) — skipping command registration.");
            return;
        }

        try {
            discordClient.registerCommands();
            log.info("Discord /scan command registered successfully.");
        } catch (Exception e) {
            log.warn("Failed to register Discord commands — /scan may not appear until this succeeds.", e);
        }
    }
}
