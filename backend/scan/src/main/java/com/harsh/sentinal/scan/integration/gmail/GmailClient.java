package com.harsh.sentinal.scan.integration.gmail;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

/**
 * Plain RestClient calls to Google's OAuth token endpoint and the Gmail API —
 * no SDK, mirrors OpenAIClient/VirusTotalClient. Unlike those two (one static
 * API key baked in at construction), the Gmail access token is per-user and
 * short-lived, so it's passed per-call rather than as a default header.
 */
@Service
public class GmailClient {

    private final RestClient restClient;
    private final GmailProperties properties;

    public GmailClient(RestClient.Builder builder, GmailProperties properties) {
        this.properties = properties;
        this.restClient = builder.build();
    }

    public String buildAuthorizeUrl(String state) {
        return UriComponentsBuilder.fromUriString(properties.getAuthUri())
                .queryParam("client_id", properties.getClientId())
                .queryParam("redirect_uri", properties.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", properties.getScope())
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    public GmailTokenResponse exchangeCode(String code) {
        String body = "grant_type=authorization_code"
                + "&code=" + enc(code)
                + "&client_id=" + enc(properties.getClientId())
                + "&client_secret=" + enc(properties.getClientSecret())
                + "&redirect_uri=" + enc(properties.getRedirectUri());
        return post(body);
    }

    public GmailTokenResponse refreshAccessToken(String refreshToken) {
        String body = "grant_type=refresh_token"
                + "&refresh_token=" + enc(refreshToken)
                + "&client_id=" + enc(properties.getClientId())
                + "&client_secret=" + enc(properties.getClientSecret());
        return post(body);
    }

    private GmailTokenResponse post(String formBody) {
        GmailTokenResponse response = restClient.post()
                .uri(properties.getTokenUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formBody)
                .retrieve()
                .body(GmailTokenResponse.class);

        if (response == null || response.accessToken() == null) {
            throw new IllegalStateException("Gmail token endpoint returned no access token.");
        }

        return response;
    }

    public GmailMessageListResponse listMessages(String accessToken, String query, String pageToken) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(properties.getApiBaseUrl() + "/gmail/v1/users/me/messages")
                .queryParam("q", query)
                .queryParam("maxResults", 50);

        if (pageToken != null) {
            uriBuilder.queryParam("pageToken", pageToken);
        }

        return restClient.get()
                .uri(uriBuilder.build().toUri())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(GmailMessageListResponse.class);
    }

    public GmailMessageResponse getMessage(String accessToken, String messageId) {
        String uri = properties.getApiBaseUrl() + "/gmail/v1/users/me/messages/" + messageId + "?format=full";

        return restClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(GmailMessageResponse.class);
    }

    private String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
