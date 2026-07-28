package com.harsh.sentinal.scan.config;

import com.harsh.sentinal.scan.common.enums.ScanSortOption;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ScanSortOptionConverter implements Converter<String, ScanSortOption> {

    @Override
    public ScanSortOption convert(@NonNull String source) {
        return ScanSortOption.valueOf(source.trim().toUpperCase());
    }
}
