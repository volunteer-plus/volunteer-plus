
CREATE TABLE IF NOT EXISTS ai_chat_messages (
    `id`                BIGINT AUTO_INCREMENT PRIMARY KEY,
    `conversation_id`   VARCHAR(255) NOT NULL,
    `message_data`      TEXT,
    `create_date`       datetime default null,
    `update_date`       datetime default null,
    INDEX idx_conversation_id (conversation_id)
);
