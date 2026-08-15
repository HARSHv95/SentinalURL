package com.harsh.sentinal.scan.service.gmail;

import java.util.UUID;

public interface GmailSyncService {

    void sweep();

    void syncAccount(UUID connectionId);

    void syncAccountNow(UUID connectionId);
}
