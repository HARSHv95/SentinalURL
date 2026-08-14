CREATE TABLE watchlist_items (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    url VARCHAR(2048) NOT NULL,
    hostname VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    last_verdict VARCHAR(30),
    last_risk_score INTEGER,
    last_ssl_issuer VARCHAR(500),
    last_ssl_valid_from TIMESTAMP,
    last_ssl_valid_to TIMESTAMP,
    last_ssl_valid BOOLEAN,
    last_ssl_expiry_alerted_at TIMESTAMP,
    last_dns_records JSONB,
    last_registrar VARCHAR(255),
    last_checked_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_watchlist_items_user_id ON watchlist_items (user_id);
CREATE UNIQUE INDEX idx_watchlist_items_user_id_url ON watchlist_items (user_id, url);
CREATE INDEX idx_watchlist_items_active ON watchlist_items (active);

CREATE TABLE watchlist_alerts (
    id UUID PRIMARY KEY,
    watchlist_item_id UUID NOT NULL,
    user_id UUID NOT NULL,
    url VARCHAR(2048) NOT NULL,
    type VARCHAR(30) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    previous_value TEXT,
    new_value TEXT,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_watchlist_alerts_watchlist_item_id ON watchlist_alerts (watchlist_item_id);
CREATE INDEX idx_watchlist_alerts_user_id_created_at ON watchlist_alerts (user_id, created_at DESC);
CREATE INDEX idx_watchlist_alerts_user_id_is_read ON watchlist_alerts (user_id, is_read);
