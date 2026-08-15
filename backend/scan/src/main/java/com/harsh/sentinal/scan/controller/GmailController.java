package com.harsh.sentinal.scan.controller;

import com.harsh.sentinal.scan.dto.GmailAuthorizeUrlResponse;
import com.harsh.sentinal.scan.dto.GmailStatusResponse;
import com.harsh.sentinal.scan.entity.GmailConnection;
import com.harsh.sentinal.scan.exception.ResourceNotFoundException;
import com.harsh.sentinal.scan.integration.gmail.GmailClient;
import com.harsh.sentinal.scan.integration.gmail.GmailProperties;
import com.harsh.sentinal.scan.integration.gmail.GmailTokenResponse;
import com.harsh.sentinal.scan.repository.GmailConnectionRepo;
import com.harsh.sentinal.scan.security.jwt.GmailOAuthStateService;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;
import com.harsh.sentinal.scan.service.gmail.GmailSyncService;
import com.harsh.sentinal.scan.util.TokenEncryptionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/gmail")
@RequiredArgsConstructor
public class GmailController {

    private final GmailClient gmailClient;
    private final GmailOAuthStateService gmailOAuthStateService;
    private final GmailConnectionRepo gmailConnectionRepo;
    private final TokenEncryptionService tokenEncryptionService;
    private final GmailSyncService gmailSyncService;
    private final GmailProperties gmailProperties;

    @GetMapping("/authorize-url")
    public GmailAuthorizeUrlResponse authorizeUrl(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String state = gmailOAuthStateService.generateState(userDetails.getUserId());
        return new GmailAuthorizeUrlResponse(gmailClient.buildAuthorizeUrl(state));
    }

    @GetMapping("/callback")
    public void callback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            HttpServletResponse response) throws IOException {

        String target = gmailProperties.getFrontendSuccessUrl();

        try {
            if (error != null) {
                response.sendRedirect(target + "?error=" + enc(error));
                return;
            }

            if (code == null || state == null) {
                response.sendRedirect(target + "?error=missing_params");
                return;
            }

            UUID userId = gmailOAuthStateService.resolveUserId(state);
            GmailTokenResponse tokens = gmailClient.exchangeCode(code);

            GmailConnection connection = gmailConnectionRepo.findByUserId(userId).orElseGet(GmailConnection::new);

            if (tokens.refreshToken() == null && connection.getId() == null) {
                response.sendRedirect(target + "?error=no_refresh_token");
                return;
            }

            connection.setUserId(userId);
            connection.setAccessTokenEncrypted(tokenEncryptionService.encrypt(tokens.accessToken()));
            if (tokens.refreshToken() != null) {
                connection.setRefreshTokenEncrypted(tokenEncryptionService.encrypt(tokens.refreshToken()));
            }
            connection.setTokenExpiresAt(Instant.now().plusSeconds(tokens.expiresIn()));
            connection.setScope(tokens.scope());
            gmailConnectionRepo.save(connection);

            response.sendRedirect(target + "?connected=gmail");
        } catch (Exception e) {
            log.warn("Gmail OAuth callback failed", e);
            response.sendRedirect(target + "?error=oauth_failed");
        }
    }

    @GetMapping("/status")
    public GmailStatusResponse status(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return gmailConnectionRepo.findByUserId(userDetails.getUserId())
                .map(c -> new GmailStatusResponse(true, c.getLastSyncedAt(), c.getLastSyncError()))
                .orElse(new GmailStatusResponse(false, null, null));
    }

    @PostMapping("/disconnect")
    public ResponseEntity<String> disconnect(@AuthenticationPrincipal CustomUserDetails userDetails) {
        gmailConnectionRepo.deleteByUserId(userDetails.getUserId());
        return ResponseEntity.ok("Gmail disconnected.");
    }

    @PostMapping("/sync-now")
    public ResponseEntity<String> syncNow(@AuthenticationPrincipal CustomUserDetails userDetails) {
        GmailConnection connection = gmailConnectionRepo.findByUserId(userDetails.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Gmail is not connected."));

        gmailSyncService.syncAccountNow(connection.getId());
        return ResponseEntity.ok("Sync completed.");
    }

    private String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
