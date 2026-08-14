package com.harsh.sentinal.scan.config;

import com.harsh.sentinal.scan.common.enums.WatchlistSortOption;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class WatchlistSortOptionConverter implements Converter<String, WatchlistSortOption> {

    @Override
    public WatchlistSortOption convert(@NonNull String source) {
        return WatchlistSortOption.valueOf(source.trim().toUpperCase());
    }
}
