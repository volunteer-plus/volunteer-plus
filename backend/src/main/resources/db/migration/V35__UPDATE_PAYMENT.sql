-- 1. Перейменувати таблицю
RENAME TABLE `liqpay_order` TO `payment_order`;

-- 2. Додати колонки для провайдера та статусу з дефолтними значеннями
ALTER TABLE `payment_order`
    ADD COLUMN `provider` VARCHAR(20) NOT NULL DEFAULT 'LIQPAY',
    ADD COLUMN `status`   VARCHAR(20) NOT NULL DEFAULT 'PENDING';
