package com.harsh.sentinal.scan.repository.specification;

import com.harsh.sentinal.scan.common.enums.ScanStatus;
import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.entity.Scan;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public final class ScanSpecifications {

    private ScanSpecifications() {}

    public static Specification<Scan> belongsToUser(UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }

    public static Specification<Scan> urlContains(String search) {
        if (search == null || search.isBlank()) {
            return null;
        }
        String pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("url")), pattern);
    }

    public static Specification<Scan> hasStatus(ScanStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Scan> hasVerdict(Verdict verdict) {
        if (verdict == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("verdict"), verdict);
    }
}
