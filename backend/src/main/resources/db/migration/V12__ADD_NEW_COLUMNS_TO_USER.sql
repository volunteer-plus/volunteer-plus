ALTER TABLE `user`
    ADD COLUMN `enabled` BOOLEAN DEFAULT TRUE AFTER `update_date`,
    ADD COLUMN `account_non_expired` BOOLEAN DEFAULT TRUE AFTER `enabled`,
    ADD COLUMN `account_non_locked` BOOLEAN DEFAULT TRUE AFTER `account_non_expired`,
    ADD COLUMN `credentials_non_expired` BOOLEAN DEFAULT TRUE AFTER `account_non_locked`;