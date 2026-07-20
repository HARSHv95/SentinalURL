package com.harsh.sentinal.scan.service.background;

import java.util.UUID;

public interface BackService {

    public void processScan(UUID scanId, String url);
}
