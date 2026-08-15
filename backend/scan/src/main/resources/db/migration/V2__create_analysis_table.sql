CREATE TABLE analysis(

    id VARCHAR(1000) PRIMARY KEY,

    scan_id UUID NOT NULL,

    url VARCHAR(2048) NOT NULL,

    status VARCHAR(30) NOT NULL,

    malicious INTEGER,

    harmless INTEGER,

    suspicious INTEGER,

    undetected INTEGER,

    created_at TIMESTAMP
)