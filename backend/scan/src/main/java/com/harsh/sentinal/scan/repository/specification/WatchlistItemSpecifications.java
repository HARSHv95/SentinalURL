package com.harsh.sentinal.scan.repository.specification;

import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.entity.WatchlistItem;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public final class WatchlistItemSpecifications {

    private WatchlistItemSpecifications() {}

    public static Specification<WatchlistItem> belongsToUser(UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }

    public static Specification<WatchlistItem> urlContains(String search) {
        if (search == null || search.isBlank()) {
            return null;
        }
        String pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("url")), pattern);
    }

    public static Specification<WatchlistItem> hasVerdict(Verdict verdict) {
        if (verdict == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("lastVerdict"), verdict);
    }
}
