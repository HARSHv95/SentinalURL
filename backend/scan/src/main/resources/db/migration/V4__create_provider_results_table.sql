CREATE TABLE provider_results (
    id UUID PRIMARY KEY,
    scan_id UUID NOT NULL,
    provider_name VARCHAR(30) NOT NULL,
    malicious BOOLEAN NOT NULL,
    malicious_engine_count INTEGER,
    harmless_engine_count INTEGER,
    suspicious_engine_count INTEGER,
    undetected_engine_count INTEGER,
    details VARCHAR(1000),
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_provider_results_scan_id ON provider_results (scan_id);
