package com.harsh.sentinal.scan.dto;

import java.time.Instant;

public record GmailStatusResponse(boolean connected, Instant lastSyncedAt, String lastSyncError) {}
