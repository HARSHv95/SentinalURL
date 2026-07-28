package com.harsh.sentinal.scan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskBucketCount {
    private String bucket;
    private long count;
}
