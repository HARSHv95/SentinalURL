package com.harsh.sentinal.scan.service.domain;

import com.harsh.sentinal.scan.entity.DomainIntelligence;

import java.util.UUID;

public interface DomainIntelligenceService {
    DomainIntelligence analyze(UUID scanId, String hostname);
}
