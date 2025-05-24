ALTER TABLE user_2fa
    ADD COLUMN enabled             TINYINT(1)   NOT NULL DEFAULT 0,
    -- персональні ліміти
    ADD COLUMN total_allowed_time_sec bigint NOT NULL DEFAULT 3600,
    ADD COLUMN retry_period_sec       bigint NOT NULL DEFAULT 30,
    ADD COLUMN max_failures           INT NOT NULL DEFAULT 5,
    -- лічильники і блокування
    ADD COLUMN attempts_count         INT NOT NULL DEFAULT 0,
    ADD COLUMN last_code_sent_at      DATETIME     NULL,
    -- унікальний ідентифікатор 2FA-запиту
    ADD COLUMN verification_id       VARCHAR(36) NOT NULL UNIQUE COMMENT 'UUID для send/verify';
