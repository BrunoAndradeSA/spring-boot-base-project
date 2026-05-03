-- ============================
-- ROLES
-- ============================
INSERT INTO roles (name) VALUES
('ROLE_USER'),
('ROLE_ADMIN'),
('ROLE_SYSTEM');

-- ============================
-- SCOPE
-- ============================
INSERT INTO scopes (name) VALUES
('read'),
('write'),
('admin');

-- ============================
-- CREATE ADMIN USER
-- ============================
SELECT create_user('admin', '123456', ARRAY['ROLE_ADMIN']);

-- ============================
-- CREATE SYSTEM CLIENT
-- ============================
SELECT create_client('system', 'secret123', ARRAY['ROLE_SYSTEM']);