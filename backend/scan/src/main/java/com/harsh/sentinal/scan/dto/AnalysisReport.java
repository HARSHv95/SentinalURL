package com.harsh.sentinal.scan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisReport {

    private String status;
    private int malicious;
    private int harmless;
    private int suspicious;
    private int undetected;
}
