-- Connect to your PostgreSQL and create the database
CREATE DATABASE kafka_enrichment_test;

-- Connect to the kafka_enrichment_test database
\c kafka_enrichment_test;


-- Create the tables
CREATE TABLE account_details (
    account_id VARCHAR(50) PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    created_timestamp BIGINT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ssn_details (
    account_id VARCHAR(50) PRIMARY KEY,
    ssn VARCHAR(11) NOT NULL,
    issue_timestamp BIGINT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_account_created ON account_details(created_timestamp);
CREATE INDEX idx_ssn_issued ON ssn_details(issue_timestamp);

-- Set up logical replication slots for Debezium
SELECT pg_create_logical_replication_slot('account_details_slot', 'pgoutput');
SELECT pg_create_logical_replication_slot('ssn_details_slot', 'pgoutput');

-- Create a replication user for Debezium
CREATE USER debezium_user WITH REPLICATION PASSWORD 'debezium_password';
GRANT CONNECT ON DATABASE kafka_enrichment_test TO debezium_user;
GRANT USAGE ON SCHEMA public TO debezium_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO debezium_user;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA public TO debezium_user;

-- Set up triggers to update timestamps
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER account_details_update_trigger
    BEFORE UPDATE ON account_details
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER ssn_details_update_trigger
    BEFORE UPDATE ON ssn_details
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

-- Insert sample data for testing
INSERT INTO account_details (account_id, customer_name, account_type, created_timestamp) VALUES
('ACC001', 'John Doe', 'PREMIUM', EXTRACT(EPOCH FROM NOW()) * 1000),
('ACC002', 'Jane Smith', 'STANDARD', EXTRACT(EPOCH FROM NOW()) * 1000),
('ACC003', 'Bob Johnson', 'BASIC', EXTRACT(EPOCH FROM NOW()) * 1000),
('ACC004', 'Alice Brown', 'PREMIUM', EXTRACT(EPOCH FROM NOW()) * 1000),
('ACC005', 'Charlie Wilson', 'STANDARD', EXTRACT(EPOCH FROM NOW()) * 1000);

-- SSN details will be inserted with delays to simulate the lagging scenario
-- Insert some immediately (for testing immediate enrichment)
INSERT INTO ssn_details (account_id, ssn, issue_timestamp) VALUES
('ACC001', '123-45-6789', EXTRACT(EPOCH FROM NOW()) * 1000),
('ACC003', '345-67-8901', EXTRACT(EPOCH FROM NOW()) * 1000);

