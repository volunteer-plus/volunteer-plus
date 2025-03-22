CREATE TABLE IF NOT EXISTS ws_message (
    `id`                BIGINT AUTO_INCREMENT PRIMARY KEY,
    `conversation_id`   VARCHAR(255) NOT NULL,
    `from_user`         BIGINT,
    `to_user`           BIGINT,
    `content`           TEXT,
    `create_date`       datetime default null,
    `update_date`       datetime default null
);
