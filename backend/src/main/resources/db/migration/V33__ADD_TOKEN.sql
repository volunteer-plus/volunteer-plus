-- 1. VerificationToken
CREATE TABLE IF NOT EXISTS verification_tokens
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    token       VARCHAR(64) NOT NULL UNIQUE,
    user_id     BIGINT      NOT NULL UNIQUE,
    expiry_date TIMESTAMP   NOT NULL,
    `create_date`     datetime default null,
    `update_date`     datetime default null,
    INDEX idx_verif_user (user_id),
    INDEX idx_verif_expiry (expiry_date),
    CONSTRAINT fk_vt_user
        FOREIGN KEY (user_id) REFERENCES `user` (id) ON DELETE CASCADE
);

-- 2. PasswordResetToken
CREATE TABLE IF NOT EXISTS password_reset_tokens
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    token       VARCHAR(64) NOT NULL UNIQUE,
    user_id     BIGINT      NOT NULL UNIQUE,
    expiry_date TIMESTAMP   NOT NULL,
    `create_date`     datetime default null,
    `update_date`     datetime default null,
    INDEX idx_prt_user (user_id),
    INDEX idx_prt_expiry (expiry_date),
    CONSTRAINT fk_prt_user
        FOREIGN KEY (user_id) REFERENCES `user` (id) ON DELETE CASCADE
);

-- 3. User2fa
CREATE TABLE IF NOT EXISTS user_2fa
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT                      NOT NULL UNIQUE,
    method_type ENUM ('EMAIL','SMS','TOTP') NOT NULL,
    secret      VARCHAR(100),
    token       VARCHAR(64),
    expiry_date TIMESTAMP,
    used        TINYINT(1)                  NOT NULL DEFAULT 0,
    created_at  TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `create_date`     datetime default null,
    `update_date`     datetime default null,
    INDEX idx_2fa_user (user_id),
    INDEX idx_2fa_method (method_type),
    INDEX idx_2fa_expiry (expiry_date),
    CONSTRAINT fk_2fa_user
        FOREIGN KEY (user_id) REFERENCES `user` (id) ON DELETE CASCADE
);
