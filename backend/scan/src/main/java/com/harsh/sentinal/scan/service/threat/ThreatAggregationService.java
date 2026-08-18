package com.harsh.sentinal.scan.service.threat;

import com.harsh.sentinal.scan.common.enums.ProviderStatus;
import com.harsh.sentinal.scan.dto.ThreatAggregationResult;
import com.harsh.sentinal.scan.dto.ThreatProviderResult;
import com.harsh.sentinal.scan.util.RiskScoreCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreatAggregationService {

    private final List<ThreatProvider> providers;

    public ThreatAggregationResult analyze(String url) {
        List<ThreatProviderResult> results = new ArrayList<>();

        for (ThreatProvider provider : providers) {
            try {
                results.add(provider.analyze(url));
            } catch (Exception e) {
                log.warn("Threat provider {} failed for url {}", provider.getName(), url, e);

                ProviderStatus status = isTimeout(e) ? ProviderStatus.TIMEOUT : ProviderStatus.ERROR;
                results.add(ThreatProviderResult.failed(provider.getName(), status, "Provider failed: " + e.getMessage()));
            }
        }

        return new ThreatAggregationResult(results, RiskScoreCalculator.calculate(results));
    }

    private boolean isTimeout(Throwable e) {
        while (e != null) {
            if (e instanceof SocketTimeoutException) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }
}
