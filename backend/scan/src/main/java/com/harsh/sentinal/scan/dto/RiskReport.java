package com.harsh.sentinal.scan.dto;

import com.harsh.sentinal.scan.common.enums.Verdict;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiskReport {
    private int riskScore;

    private Verdict verdict;

    private int confidence;
}
