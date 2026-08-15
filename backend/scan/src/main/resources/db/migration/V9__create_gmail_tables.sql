CREATE TABLE gmail_connections (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    access_token_encrypted TEXT NOT NULL,
    refresh_token_encrypted TEXT NOT NULL,
    token_expires_at TIMESTAMP NOT NULL,
    scope VARCHAR(500) NOT NULL,
    last_synced_at TIMESTAMP,
    last_sync_error VARCHAR(1000),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX idx_gmail_connections_user_id ON gmail_connections (user_id);

CREATE TABLE email_scan_batches (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    subject VARCHAR(500),
    sender_preview VARCHAR(500),
    source_message_id VARCHAR(255) NOT NULL,
    url_count INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_email_scan_batches_user_id_created_at ON email_scan_batches (user_id, created_at DESC);
CREATE UNIQUE INDEX idx_email_scan_batches_user_id_source_message_id ON email_scan_batches (user_id, source_message_id);

ALTER TABLE scans ADD COLUMN email_scan_batch_id UUID;
CREATE INDEX idx_scans_email_scan_batch_id ON scans (email_scan_batch_id);
