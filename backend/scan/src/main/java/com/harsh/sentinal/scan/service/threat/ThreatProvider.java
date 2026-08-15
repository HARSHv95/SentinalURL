package com.harsh.sentinal.scan.service.threat;

public interface ThreatProvider {

    String getName();

    ThreatProviderResult analyze(String url);
}
