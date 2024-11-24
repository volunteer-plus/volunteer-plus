create table if not exists `liqpay_order`
(
    `id`              bigint primary key auto_increment,
    `create_date`     datetime default null,
    `update_date`     datetime default null,
    `user_id`         bigint,
    `currency_name`   nvarchar(50),
    `order_id`        nvarchar(255),
    `amount`          float,
     constraint `fk__user__liqpay_order_id` foreign key (`user_id`) references `user` (`id`)
);