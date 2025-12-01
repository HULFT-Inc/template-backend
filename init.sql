-- Initialize database with sample data

-- Organizations table
CREATE TABLE IF NOT EXISTS organizations (
    id BIGSERIAL PRIMARY KEY,
    shortcode VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL
);

-- Change records table
CREATE TABLE IF NOT EXISTS change_records (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT NOT NULL REFERENCES organizations(id),
    document_type VARCHAR(50) NOT NULL,
    document_id VARCHAR(255) NOT NULL,
    field_name VARCHAR(255) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    changed_by VARCHAR(255) NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_change_records_org ON change_records(organization_id);
CREATE INDEX IF NOT EXISTS idx_change_records_doc_type ON change_records(document_type);
CREATE INDEX IF NOT EXISTS idx_change_records_doc_id ON change_records(document_id);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_active ON users(active);

-- Sample data
INSERT INTO organizations (shortcode, name) VALUES
('GPS', 'Global Procurement Services'),
('ACME', 'ACME Corporation')
ON CONFLICT (shortcode) DO NOTHING;

INSERT INTO users (first_name, last_name, email) VALUES
('John', 'Doe', 'john.doe@example.com'),
('Jane', 'Smith', 'jane.smith@example.com'),
('Bob', 'Johnson', 'bob.johnson@example.com')
ON CONFLICT (email) DO NOTHING;
