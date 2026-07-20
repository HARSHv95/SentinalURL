CREATE TABLE scans (

      id UUID PRIMARY KEY,

      user_id UUID NOT NULL,

      url VARCHAR(2048) NOT NULL,

      status VARCHAR(30) NOT NULL,

      risk_score INTEGER,

      created_at TIMESTAMP NOT NULL,

      updated_at TIMESTAMP NOT NULL

);