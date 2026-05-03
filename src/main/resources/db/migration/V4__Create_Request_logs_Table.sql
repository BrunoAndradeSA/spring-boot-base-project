CREATE TABLE request_logs (
    id BIGSERIAL PRIMARY KEY,

    method VARCHAR(10) NOT NULL,
    path VARCHAR(500) NOT NULL,
    status INTEGER NOT NULL,

    user_name VARCHAR(255),
    client_name VARCHAR(255),

    duration_ms BIGINT NOT NULL,

    request_body TEXT,
    response_body TEXT,
    headers TEXT,
    curl TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_request_logs_created_at ON request_logs (created_at);
CREATE INDEX idx_request_logs_status ON request_logs (status);
CREATE INDEX idx_request_logs_path ON request_logs (path);
CREATE INDEX idx_request_logs_user ON request_logs (user_name);