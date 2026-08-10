CREATE TABLE domain_intelligence (
    id UUID PRIMARY KEY,
    scan_id UUID NOT NULL,
    domain VARCHAR(255) NOT NULL,
    registrar VARCHAR(255),
    creation_date TIMESTAMP,
    expiration_date TIMESTAMP,
    updated_date TIMESTAMP,
    domain_age_days INTEGER,
    country VARCHAR(100),
    hosting_provider VARCHAR(255),
    asn VARCHAR(100),
    ip_addresses VARCHAR(500),
    dns_records JSONB,
    ssl_issuer VARCHAR(500),
    ssl_valid_from TIMESTAMP,
    ssl_valid_to TIMESTAMP,
    ssl_valid BOOLEAN,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_domain_intelligence_scan_id ON domain_intelligence (scan_id);
CREATE INDEX idx_domain_intelligence_domain_created_at ON domain_intelligence (domain, created_at DESC);
