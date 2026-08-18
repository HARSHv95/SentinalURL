package com.harsh.sentinal.scan.service.watchlist.Implementation;

import com.harsh.sentinal.scan.common.enums.AlertSeverity;
import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.common.enums.WatchlistAlertType;
import com.harsh.sentinal.scan.entity.WatchlistAlert;
import com.harsh.sentinal.scan.entity.WatchlistItem;
import com.harsh.sentinal.scan.integration.dns.DnsLookupClient;
import com.harsh.sentinal.scan.integration.ssl.SslCertificateInspector;
import com.harsh.sentinal.scan.integration.whois.WhoisClient;
import com.harsh.sentinal.scan.integration.whois.WhoisParser;
import com.harsh.sentinal.scan.repository.WatchlistAlertRepo;
import com.harsh.sentinal.scan.repository.WatchlistItemRepo;
import com.harsh.sentinal.scan.dto.ThreatAggregationResult;
import com.harsh.sentinal.scan.service.threat.ThreatAggregationService;
import com.harsh.sentinal.scan.service.watchlist.WatchlistMonitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class WatchlistMonitorServiceImplementation implements WatchlistMonitorService {

    private static final Duration SSL_EXPIRY_ALERT_COOLDOWN = Duration.ofHours(24);

    private final WatchlistItemRepo watchlistItemRepo;
    private final WatchlistAlertRepo watchlistAlertRepo;
    private final ThreatAggregationService threatAggregationService;
    private final SslCertificateInspector sslCertificateInspector;
    private final DnsLookupClient dnsLookupClient;
    private final WhoisClient whoisClient;
    private final WhoisParser whoisParser;

    @Value("${watchlist.ssl-expiry-warning-days}")
    private int sslExpiryWarningDays;

    @Lazy
    @Autowired
    private WatchlistMonitorService self;

    @Override
    @Scheduled(fixedRateString = "${watchlist.check-interval-hours}", timeUnit = TimeUnit.HOURS)
    public void sweep() {
        List<WatchlistItem> activeItems = watchlistItemRepo.findByActiveTrue();
        log.info("Watchlist sweep starting for {} active item(s)", activeItems.size());
        activeItems.forEach(item -> self.checkItem(item.getId()));
    }

    @Override
    @Async
    public void checkItem(UUID watchlistItemId) {
        try {
            performCheck(watchlistItemId);
        } catch (Exception e) {
            log.warn("Watchlist check failed for item {}", watchlistItemId, e);
        }
    }

    @Override
    public void checkItemNow(UUID watchlistItemId) {
        performCheck(watchlistItemId);
    }

    private void performCheck(UUID watchlistItemId) {
        WatchlistItem item = watchlistItemRepo.findById(watchlistItemId).orElseThrow();
        List<WatchlistAlert> newAlerts = new ArrayList<>();

        checkVerdict(item, newAlerts);
        checkSsl(item, newAlerts);
        checkDns(item, newAlerts);
        checkWhois(item, newAlerts);

        item.setLastCheckedAt(Instant.now());
        watchlistItemRepo.save(item);
        watchlistAlertRepo.saveAll(newAlerts);
    }

    private void checkVerdict(WatchlistItem item, List<WatchlistAlert> newAlerts) {
        try {
            ThreatAggregationResult aggregation = threatAggregationService.analyze(item.getUrl());
            Verdict newVerdict = aggregation.riskReport().getVerdict();

            if (item.getLastVerdict() != null && newVerdict != item.getLastVerdict()) {
                newAlerts.add(buildAlert(item, WatchlistAlertType.VERDICT_CHANGED,
                        severityForVerdictChange(item.getLastVerdict(), newVerdict),
                        "Verdict changed from " + item.getLastVerdict() + " to " + newVerdict,
                        item.getLastVerdict().name(), newVerdict.name()));
            }

            item.setLastVerdict(newVerdict);
            item.setLastRiskScore(aggregation.riskReport().getRiskScore());
        } catch (Exception e) {
            log.warn("Watchlist verdict check failed for item {}", item.getId(), e);
        }
    }

    private void checkSsl(WatchlistItem item, List<WatchlistAlert> newAlerts) {
        try {
            SslCertificateInspector.SslInfo ssl = sslCertificateInspector.inspect(item.getHostname());
            if (ssl == null) {
                return;
            }

            long daysLeft = Duration.between(Instant.now(), ssl.validTo()).toDays();
            boolean recentlyAlerted = item.getLastSslExpiryAlertedAt() != null
                    && Duration.between(item.getLastSslExpiryAlertedAt(), Instant.now()).compareTo(SSL_EXPIRY_ALERT_COOLDOWN) < 0;

            if (daysLeft <= sslExpiryWarningDays && !recentlyAlerted) {
                newAlerts.add(buildAlert(item, WatchlistAlertType.SSL_EXPIRING,
                        daysLeft < 0 ? AlertSeverity.CRITICAL : AlertSeverity.WARNING,
                        "SSL certificate expires in " + daysLeft + " days (" + ssl.validTo() + ")",
                        item.getLastSslValidTo() != null ? item.getLastSslValidTo().toString() : null,
                        ssl.validTo().toString()));
                item.setLastSslExpiryAlertedAt(Instant.now());
            }

            boolean issuerChanged = item.getLastSslIssuer() != null && !item.getLastSslIssuer().equals(ssl.issuer());
            boolean becameInvalid = Boolean.TRUE.equals(item.getLastSslValid()) && !ssl.valid();

            if (issuerChanged || becameInvalid) {
                newAlerts.add(buildAlert(item, WatchlistAlertType.SSL_CHANGED, AlertSeverity.CRITICAL,
                        issuerChanged ? "SSL certificate issuer changed" : "SSL certificate became invalid",
                        item.getLastSslIssuer(), ssl.issuer()));
            }

            item.setLastSslIssuer(ssl.issuer());
            item.setLastSslValidFrom(ssl.validFrom());
            item.setLastSslValidTo(ssl.validTo());
            item.setLastSslValid(ssl.valid());
        } catch (Exception e) {
            log.warn("Watchlist SSL check failed for item {}", item.getId(), e);
        }
    }

    private void checkDns(WatchlistItem item, List<WatchlistAlert> newAlerts) {
        try {
            Map<String, List<String>> dns = dnsLookupClient.lookupRecords(item.getHostname());

            if (item.getLastDnsRecords() != null && !item.getLastDnsRecords().equals(dns)) {
                newAlerts.add(buildAlert(item, WatchlistAlertType.DNS_CHANGED, AlertSeverity.WARNING,
                        "DNS records changed for " + item.getHostname(),
                        item.getLastDnsRecords().toString(), dns.toString()));
            }

            item.setLastDnsRecords(dns);
        } catch (Exception e) {
            log.warn("Watchlist DNS check failed for item {}", item.getId(), e);
        }
    }

    private void checkWhois(WatchlistItem item, List<WatchlistAlert> newAlerts) {
        try {
            WhoisParser.ParsedWhois parsed = whoisParser.parse(whoisClient.lookup(item.getHostname()));

            if (parsed.registrar() == null) {
                return;
            }

            if (item.getLastRegistrar() != null && !item.getLastRegistrar().equals(parsed.registrar())) {
                newAlerts.add(buildAlert(item, WatchlistAlertType.WHOIS_CHANGED, AlertSeverity.WARNING,
                        "Registrar changed from " + item.getLastRegistrar() + " to " + parsed.registrar(),
                        item.getLastRegistrar(), parsed.registrar()));
            }

            item.setLastRegistrar(parsed.registrar());
        } catch (Exception e) {
            log.warn("Watchlist WHOIS check failed for item {}", item.getId(), e);
        }
    }

    private AlertSeverity severityForVerdictChange(Verdict oldVerdict, Verdict newVerdict) {
        if (newVerdict.ordinal() <= oldVerdict.ordinal()) {
            return AlertSeverity.INFO;
        }
        return (newVerdict == Verdict.HIGH_RISK || newVerdict == Verdict.CRITICAL)
                ? AlertSeverity.CRITICAL
                : AlertSeverity.WARNING;
    }

    private WatchlistAlert buildAlert(
            WatchlistItem item,
            WatchlistAlertType type,
            AlertSeverity severity,
            String message,
            String previousValue,
            String newValue) {

        WatchlistAlert alert = new WatchlistAlert();
        alert.setWatchlistItemId(item.getId());
        alert.setUserId(item.getUserId());
        alert.setUrl(item.getUrl());
        alert.setType(type);
        alert.setSeverity(severity);
        alert.setMessage(message);
        alert.setPreviousValue(previousValue);
        alert.setNewValue(newValue);
        alert.setRead(false);
        return alert;
    }
}
