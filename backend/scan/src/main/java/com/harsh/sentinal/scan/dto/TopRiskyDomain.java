package com.harsh.sentinal.scan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopRiskyDomain {
    private String domain;
    private long scanCount;
    private double avgRiskScore;
    private int maxRiskScore;
}
