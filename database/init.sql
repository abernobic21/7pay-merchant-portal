-- Enums
CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');
CREATE TYPE user_access_level AS ENUM ('MERCHANT', 'BRANCH');
CREATE TYPE user_status AS ENUM ('ACTIVATED', 'DEACTIVATED');

-- Users table
CREATE TABLE users (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name          VARCHAR(100) NOT NULL,
    last_name           VARCHAR(100) NOT NULL,
    email               VARCHAR(255) NOT NULL UNIQUE,
    phone               VARCHAR(20),
    password_hash       VARCHAR(255) NOT NULL,
    role                user_role NOT NULL,
    access_level        user_access_level NOT NULL,
    merchant_id         VARCHAR(100),
    status              user_status NOT NULL DEFAULT 'DEACTIVATED',
    first_login_flag    BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Password resets table
CREATE TABLE password_resets (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    reset_token     VARCHAR(255) NOT NULL UNIQUE,
    expiry          TIMESTAMP NOT NULL,
    used_at         TIMESTAMP
);

-- Indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_merchant_id ON users(merchant_id);
CREATE INDEX idx_password_resets_user_id ON password_resets(user_id);
CREATE INDEX idx_password_resets_token ON password_resets(reset_token);