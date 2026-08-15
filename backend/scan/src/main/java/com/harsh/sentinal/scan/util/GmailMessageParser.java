package com.harsh.sentinal.scan.util;

import com.harsh.sentinal.scan.integration.gmail.GmailMessageResponse;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Walks a Gmail API message's recursive MIME parts tree, base64url-decodes
 * text/* bodies, and regex-extracts candidate URLs. Best-effort — the Gmail
 * link-tracking redirect wrapper (google.com/url?q=...) is not unwrapped.
 */
@Component
public class GmailMessageParser {

    private static final Pattern URL_PATTERN =
            Pattern.compile("https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+", Pattern.CASE_INSENSITIVE);

    public String extractHeader(GmailMessageResponse message, String name) {
        if (message.payload() == null || message.payload().headers() == null) {
            return null;
        }

        return message.payload().headers().stream()
                .filter(h -> name.equalsIgnoreCase(h.name()))
                .map(GmailMessageResponse.Header::value)
                .findFirst()
                .orElse(null);
    }

    public Set<String> extractUrls(GmailMessageResponse message) {
        Set<String> urls = new LinkedHashSet<>();
        collect(message.payload(), urls);
        return urls;
    }

    private void collect(GmailMessageResponse.Payload part, Set<String> urls) {
        if (part == null) {
            return;
        }

        if (part.body() != null && part.body().data() != null
                && (part.mimeType() == null || part.mimeType().startsWith("text/"))) {

            String decoded = new String(
                    Base64.getUrlDecoder().decode(part.body().data()),
                    StandardCharsets.UTF_8
            );

            Matcher matcher = URL_PATTERN.matcher(decoded);
            while (matcher.find()) {
                urls.add(matcher.group().replaceAll("[.,;:!?)\\]]+$", ""));
            }
        }

        if (part.parts() != null) {
            part.parts().forEach(p -> collect(p, urls));
        }
    }
}
