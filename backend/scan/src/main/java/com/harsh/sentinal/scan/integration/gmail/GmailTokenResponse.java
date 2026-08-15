package com.harsh.sentinal.scan.integration.gmail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GmailTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("expires_in") Integer expiresIn,
        String scope,
        @JsonProperty("token_type") String tokenType
) {}
