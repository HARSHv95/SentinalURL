package com.harsh.sentinal.scan.service.gmail;

import com.harsh.sentinal.scan.entity.EmailScanBatch;
import com.harsh.sentinal.scan.entity.GmailConnection;
import com.harsh.sentinal.scan.integration.gmail.GmailClient;
import com.harsh.sentinal.scan.integration.gmail.GmailMessageListResponse;
import com.harsh.sentinal.scan.integration.gmail.GmailMessageResponse;
import com.harsh.sentinal.scan.integration.gmail.GmailProperties;
import com.harsh.sentinal.scan.integration.gmail.GmailTokenResponse;
import com.harsh.sentinal.scan.repository.EmailScanBatchRepo;
import com.harsh.sentinal.scan.repository.GmailConnectionRepo;
import com.harsh.sentinal.scan.service.scan.ScanService;
import com.harsh.sentinal.scan.util.GmailMessageParser;
import com.harsh.sentinal.scan.util.TokenEncryptionService;
import com.harsh.sentinal.scan.util.UrlValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailSyncServiceImplementation implements GmailSyncService {

    private static final Duration OVERLAP_BUFFER = Duration.ofMinutes(5);
    private static final Duration EXPIRY_SKEW = Duration.ofSeconds(60);

    private final GmailConnectionRepo gmailConnectionRepo;
    private final EmailScanBatchRepo emailScanBatchRepo;
    private final GmailClient gmailClient;
    private final GmailMessageParser gmailMessageParser;
    private final TokenEncryptionService tokenEncryptionService;
    private final ScanService scanService;
    private final GmailProperties gmailProperties;

    @Lazy
    @Autowired
    private GmailSyncService self;

    @Override
    @Scheduled(fixedRateString = "${gmail.sync-interval-minutes}", timeUnit = TimeUnit.MINUTES)
    public void sweep() {
        List<GmailConnection> connections = gmailConnectionRepo.findAll();
        log.info("Gmail sync sweep starting for {} connection(s)", connections.size());
        connections.forEach(c -> self.syncAccount(c.getId()));
    }

    @Override
    @Async
    public void syncAccount(UUID connectionId) {
        try {
            performSync(connectionId);
        } catch (Exception e) {
            log.warn("Gmail sync failed for connection {}", connectionId, e);
            gmailConnectionRepo.findById(connectionId).ifPresent(c -> {
                c.setLastSyncError(truncate(e.getMessage(), 1000));
                gmailConnectionRepo.save(c);
            });
        }
    }

    @Override
    public void syncAccountNow(UUID connectionId) {
        performSync(connectionId);
    }

    private void performSync(UUID connectionId) {
        GmailConnection connection = gmailConnectionRepo.findById(connectionId).orElseThrow();
        String accessToken = ensureValidAccessToken(connection);

        Instant windowStart = connection.getLastSyncedAt() != null
                ? connection.getLastSyncedAt().minus(OVERLAP_BUFFER)
                : Instant.now().minus(Duration.ofHours(gmailProperties.getSyncLookbackHours()));
        String query = "after:" + windowStart.getEpochSecond();

        String pageToken = null;
        do {
            GmailMessageListResponse page = gmailClient.listMessages(accessToken, query, pageToken);
            if (page != null && page.messages() != null) {
                for (GmailMessageListResponse.MessageStub stub : page.messages()) {
                    processMessage(connection, accessToken, stub.id());
                }
            }
            pageToken = page != null ? page.nextPageToken() : null;
        } while (pageToken != null);

        connection.setLastSyncedAt(Instant.now());
        connection.setLastSyncError(null);
        gmailConnectionRepo.save(connection);
    }

    private void processMessage(GmailConnection connection, String accessToken, String messageId) {
        try {
            if (emailScanBatchRepo.existsByUserIdAndSourceMessageId(connection.getUserId(), messageId)) {
                return;
            }

            GmailMessageResponse message = gmailClient.getMessage(accessToken, messageId);
            UrlValidator validator = new UrlValidator();
            List<String> urls = gmailMessageParser.extractUrls(message).stream()
                    .filter(u -> validator.isValid(u, null))
                    .toList();

            if (urls.isEmpty()) {
                return;
            }

            EmailScanBatch batch = new EmailScanBatch();
            batch.setUserId(connection.getUserId());
            batch.setSubject(truncate(gmailMessageParser.extractHeader(message, "Subject"), 500));
            batch.setSenderPreview(truncate(gmailMessageParser.extractHeader(message, "From"), 500));
            batch.setSourceMessageId(messageId);
            batch.setUrlCount(urls.size());
            emailScanBatchRepo.save(batch);

            urls.forEach(url -> scanService.createScanFromBatch(url, connection.getUserId(), batch.getId()));
        } catch (Exception e) {
            log.warn("Failed to process Gmail message {} for connection {}", messageId, connection.getId(), e);
        }
    }

    private String ensureValidAccessToken(GmailConnection connection) {
        if (connection.getTokenExpiresAt().isAfter(Instant.now().plus(EXPIRY_SKEW))) {
            return tokenEncryptionService.decrypt(connection.getAccessTokenEncrypted());
        }

        String refreshToken = tokenEncryptionService.decrypt(connection.getRefreshTokenEncrypted());
        GmailTokenResponse refreshed = gmailClient.refreshAccessToken(refreshToken);

        connection.setAccessTokenEncrypted(tokenEncryptionService.encrypt(refreshed.accessToken()));
        connection.setTokenExpiresAt(Instant.now().plusSeconds(refreshed.expiresIn()));
        gmailConnectionRepo.save(connection);

        return refreshed.accessToken();
    }

    private String truncate(String s, int max) {
        return s == null ? null : (s.length() > max ? s.substring(0, max) : s);
    }
}
