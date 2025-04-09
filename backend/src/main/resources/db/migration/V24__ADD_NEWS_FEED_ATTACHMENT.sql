create table if not exists `news_feed_attachment`
(
    `id`                      bigint primary key auto_increment,
    `create_date`             datetime default null,
    `update_date`             datetime default null,
    `is_logo`                 bit not null default 0,
    `news_feed_id`            bigint,
    `filename`                LONGTEXT CHARACTER SET utf8mb4 not null,
    `s3_link`                 LONGTEXT CHARACTER SET utf8mb4 not null,

    constraint `fk__news_feed_attachment_id` foreign key (`news_feed_id`) references `news_feed` (`id`)
);
