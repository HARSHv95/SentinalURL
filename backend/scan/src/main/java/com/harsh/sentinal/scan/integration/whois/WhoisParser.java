package com.harsh.sentinal.scan.integration.whois;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Best-effort field extraction from raw WHOIS text. WHOIS output isn't
 * standardized across registries — field labels and date formats vary by
 * TLD — so this only recognizes the common label/date variants. Anything it
 * can't confidently parse comes back null; it never guesses.
 */
@Component
public class WhoisParser {

    public record ParsedWhois(String registrar, Instant creationDate, Instant expirationDate, Instant updatedDate) {}

    private static final Pattern REGISTRAR = Pattern.compile("(?im)^\\s*Registrar:\\s*(.+)$");
    private static final Pattern CREATED = Pattern.compile("(?im)^\\s*(?:Creation Date|created|Registered on)\\s*:\\s*(.+)$");
    private static final Pattern EXPIRES = Pattern.compile("(?im)^\\s*(?:Registry Expiry Date|Expiration Date|paid-till|Expiry [Dd]ate)\\s*:\\s*(.+)$");
    private static final Pattern UPDATED = Pattern.compile("(?im)^\\s*(?:Updated Date|last-update|Last Modified)\\s*:\\s*(.+)$");

    public ParsedWhois parse(String rawWhois) {
        if (rawWhois == null) {
            return new ParsedWhois(null, null, null, null);
        }

        return new ParsedWhois(
                extractGroup(REGISTRAR, rawWhois),
                parseDate(extractGroup(CREATED, rawWhois)),
                parseDate(extractGroup(EXPIRES, rawWhois)),
                parseDate(extractGroup(UPDATED, rawWhois))
        );
    }

    private String extractGroup(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : null;
    }

    private Instant parseDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }

        String trimmed = raw.trim();

        try {
            return OffsetDateTime.parse(trimmed, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant();
        } catch (Exception ignored) {
            // fall through to the next attempt
        }

        try {
            return Instant.parse(trimmed);
        } catch (Exception ignored) {
            return null;
        }
    }
}
