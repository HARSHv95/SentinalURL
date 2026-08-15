package com.harsh.sentinal.scan.dto;

import com.harsh.sentinal.scan.entity.DomainIntelligence;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record DomainIntelligenceResponse(
        String domain,
        String registrar,
        Instant creationDate,
        Instant expirationDate,
        Instant updatedDate,
        Integer domainAgeDays,
        String country,
        String hostingProvider,
        String asn,
        String ipAddresses,
        Map<String, List<String>> dnsRecords,
        String sslIssuer,
        Instant sslValidFrom,
        Instant sslValidTo,
        Boolean sslValid
) {
    public static DomainIntelligenceResponse from(DomainIntelligence entity) {
        return new DomainIntelligenceResponse(
                entity.getDomain(),
                entity.getRegistrar(),
                entity.getCreationDate(),
                entity.getExpirationDate(),
                entity.getUpdatedDate(),
                entity.getDomainAgeDays(),
                entity.getCountry(),
                entity.getHostingProvider(),
                entity.getAsn(),
                entity.getIpAddresses(),
                entity.getDnsRecords(),
                entity.getSslIssuer(),
                entity.getSslValidFrom(),
                entity.getSslValidTo(),
                entity.getSslValid()
        );
    }
}
