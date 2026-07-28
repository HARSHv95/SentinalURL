package com.harsh.sentinal.scan.dto;

import com.harsh.sentinal.scan.common.enums.Verdict;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerdictCount {
    private Verdict verdict;
    private long count;
}
