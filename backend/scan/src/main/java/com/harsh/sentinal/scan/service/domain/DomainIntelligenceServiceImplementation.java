package com.harsh.sentinal.scan.service.domain;

import com.harsh.sentinal.scan.entity.DomainIntelligence;
import com.harsh.sentinal.scan.integration.dns.DnsLookupClient;
import com.harsh.sentinal.scan.integration.geoip.IpGeoClient;
import com.harsh.sentinal.scan.integration.ssl.SslCertificateInspector;
import com.harsh.sentinal.scan.integration.whois.WhoisClient;
import com.harsh.sentinal.scan.integration.whois.WhoisParser;
import com.harsh.sentinal.scan.repository.DomainIntelligenceRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DomainIntelligenceServiceImplementation implements DomainIntelligenceService {

    private static final Duration CACHE_TTL = Duration.ofHours(24);

    private final DomainIntelligenceRepo domainIntelligenceRepo;
    private final DnsLookupClient dnsLookupClient;
    private final WhoisClient whoisClient;
    private final WhoisParser whoisParser;
    private final IpGeoClient ipGeoClient;
    private final SslCertificateInspector sslCertificateInspector;

    @Override
    public DomainIntelligence analyze(UUID scanId, String hostname) {
        Optional<DomainIntelligence> cached = domainIntelligenceRepo.findFirstByDomainOrderByCreatedAtDesc(hostname);

        if (cached.isPresent() && isFresh(cached.get())) {
            return cloneForScan(cached.get(), scanId);
        }

        return fetchFresh(scanId, hostname);
    }

    private boolean isFresh(DomainIntelligence existing) {
        return Duration.between(existing.getCreatedAt(), Instant.now()).compareTo(CACHE_TTL) <= 0;
    }

    private DomainIntelligence cloneForScan(DomainIntelligence source, UUID scanId) {
        DomainIntelligence clone = new DomainIntelligence();
        clone.setScanId(scanId);
        clone.setDomain(source.getDomain());
        clone.setRegistrar(source.getRegistrar());
        clone.setCreationDate(source.getCreationDate());
        clone.setExpirationDate(source.getExpirationDate());
        clone.setUpdatedDate(source.getUpdatedDate());
        clone.setDomainAgeDays(source.getDomainAgeDays());
        clone.setCountry(source.getCountry());
        clone.setHostingProvider(source.getHostingProvider());
        clone.setAsn(source.getAsn());
        clone.setIpAddresses(source.getIpAddresses());
        clone.setDnsRecords(source.getDnsRecords());
        clone.setSslIssuer(source.getSslIssuer());
        clone.setSslValidFrom(source.getSslValidFrom());
        clone.setSslValidTo(source.getSslValidTo());
        clone.setSslValid(source.getSslValid());
        return domainIntelligenceRepo.save(clone);
    }

    private DomainIntelligence fetchFresh(UUID scanId, String hostname) {
        DomainIntelligence result = new DomainIntelligence();
        result.setScanId(scanId);
        result.setDomain(hostname);

        try {
            Map<String, List<String>> dnsRecords = dnsLookupClient.lookupRecords(hostname);
            result.setDnsRecords(dnsRecords);

            List<String> ips = new ArrayList<>();
            ips.addAll(dnsRecords.getOrDefault("A", List.of()));
            ips.addAll(dnsRecords.getOrDefault("AAAA", List.of()));
            result.setIpAddresses(String.join(",", ips));

            if (!ips.isEmpty()) {
                IpGeoClient.IpGeoResponse geo = ipGeoClient.lookup(ips.get(0));
                if (geo != null && "success".equals(geo.status())) {
                    result.setCountry(geo.country());
                    result.setHostingProvider(geo.isp() != null ? geo.isp() : geo.org());
                    result.setAsn(geo.as());
                }
            }
        } catch (Exception e) {
            log.warn("DNS/geo lookup failed for {}", hostname, e);
        }

        try {
            String rawWhois = whoisClient.lookup(hostname);
            WhoisParser.ParsedWhois parsed = whoisParser.parse(rawWhois);

            result.setRegistrar(parsed.registrar());
            result.setCreationDate(parsed.creationDate());
            result.setExpirationDate(parsed.expirationDate());
            result.setUpdatedDate(parsed.updatedDate());

            if (parsed.creationDate() != null) {
                result.setDomainAgeDays((int) Duration.between(parsed.creationDate(), Instant.now()).toDays());
            }
        } catch (Exception e) {
            log.warn("WHOIS lookup failed for {}", hostname, e);
        }

        try {
            SslCertificateInspector.SslInfo sslInfo = sslCertificateInspector.inspect(hostname);
            if (sslInfo != null) {
                result.setSslIssuer(sslInfo.issuer());
                result.setSslValidFrom(sslInfo.validFrom());
                result.setSslValidTo(sslInfo.validTo());
                result.setSslValid(sslInfo.valid());
            }
        } catch (Exception e) {
            log.warn("SSL inspection failed for {}", hostname, e);
        }

        return domainIntelligenceRepo.save(result);
    }
}
