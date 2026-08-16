ALTER TABLE scans ADD COLUMN share_token VARCHAR(64);
ALTER TABLE scans ADD COLUMN share_visibility VARCHAR(20) NOT NULL DEFAULT 'PRIVATE';

CREATE UNIQUE INDEX idx_scans_share_token ON scans (share_token);
