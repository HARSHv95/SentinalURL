ALTER TABLE scans
    ADD COLUMN verdict VARCHAR(30);

CREATE INDEX idx_scans_user_id_created_at ON scans (user_id, created_at DESC);

CREATE INDEX idx_scans_user_id_status ON scans (user_id, status);

CREATE INDEX idx_scans_user_id_verdict ON scans (user_id, verdict);

CREATE INDEX idx_scans_user_id_risk_score ON scans (user_id, risk_score);
