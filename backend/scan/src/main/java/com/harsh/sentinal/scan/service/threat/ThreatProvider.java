package com.harsh.sentinal.scan.service.threat;

import com.harsh.sentinal.scan.dto.ThreatProviderResult;

public interface ThreatProvider {

    String getName();

    ThreatProviderResult analyze(String url);
}
