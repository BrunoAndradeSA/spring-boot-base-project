-- ============================
-- CREATE USER
-- ============================
CREATE OR REPLACE FUNCTION create_user(
    p_username VARCHAR,
    p_password VARCHAR,
    p_roles VARCHAR[] DEFAULT ARRAY['ROLE_USER']
)
RETURNS VOID AS
$$
DECLARE
    v_user_id BIGINT;
    v_role RECORD;
BEGIN
    INSERT INTO users (username, password)
    VALUES (
        p_username,
        crypt(p_password, gen_salt('bf'))
    )
    RETURNING id INTO v_user_id;

    FOR v_role IN
        SELECT id FROM roles WHERE name = ANY(p_roles)
    LOOP
        INSERT INTO user_roles (user_id, role_id)
        VALUES (v_user_id, v_role.id);
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- ============================
-- CREATE CLIENT
-- ============================
CREATE OR REPLACE FUNCTION create_client(
    p_client_id VARCHAR,
    p_client_secret VARCHAR,
    p_roles VARCHAR[] DEFAULT ARRAY['ROLE_SYSTEM']
)
RETURNS VOID AS
$$
DECLARE
    v_client_pk BIGINT;
    v_role RECORD;
BEGIN
    INSERT INTO clients (client_id, client_secret)
    VALUES (
        p_client_id,
        crypt(p_client_secret, gen_salt('bf'))
    )
    RETURNING id INTO v_client_pk;

    FOR v_role IN
        SELECT id FROM roles WHERE name = ANY(p_roles)
    LOOP
        INSERT INTO client_roles (client_id, role_id)
        VALUES (v_client_pk, v_role.id);
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- ============================
-- VALIDADE USER PASSWORD
-- ============================
CREATE OR REPLACE FUNCTION validate_user_password(
    p_username VARCHAR,
    p_password VARCHAR
)
RETURNS BOOLEAN AS
$$
DECLARE
    v_hash TEXT;
BEGIN
    SELECT password INTO v_hash
    FROM users
    WHERE username = p_username
      AND deleted_at IS NULL;

    IF v_hash IS NULL THEN
        RETURN FALSE;
    END IF;

    RETURN v_hash = crypt(p_password, v_hash);
END;
$$ LANGUAGE plpgsql;