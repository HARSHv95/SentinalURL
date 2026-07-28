package com.harsh.sentinal.scan.common.enums;

import org.springframework.data.domain.Sort;

public enum ScanSortOption {
    NEWEST,
    OLDEST,
    RISK_HIGH,
    RISK_LOW;

    public Sort toSort() {
        return switch (this) {
            case NEWEST -> Sort.by(Sort.Direction.DESC, "createdAt");
            case OLDEST -> Sort.by(Sort.Direction.ASC, "createdAt");
            case RISK_HIGH -> Sort.by(Sort.Direction.DESC, "riskScore");
            case RISK_LOW -> Sort.by(Sort.Direction.ASC, "riskScore");
        };
    }
}
