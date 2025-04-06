ALTER TABLE `user`
    ADD COLUMN `reset_token` VARCHAR(255),
    ADD COLUMN `reset_token_expiration_time` BIGINT;