CREATE TABLE ai_analysis (
    id UUID PRIMARY KEY,
    scan_id UUID NOT NULL,
    executive_summary TEXT NOT NULL,
    technical_analysis TEXT NOT NULL,
    risk_factors JSONB NOT NULL,
    recommendations JSONB NOT NULL,
    confidence INTEGER NOT NULL,
    model VARCHAR(50) NOT NULL,
    generated_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_ai_analysis_scan_id ON ai_analysis (scan_id);
