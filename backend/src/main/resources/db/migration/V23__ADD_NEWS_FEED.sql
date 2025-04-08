CREATE TABLE IF NOT EXISTS news_feed (
    `id`                BIGINT AUTO_INCREMENT PRIMARY KEY,
    `subject`           VARCHAR(1000) NOT NULL,
    `body`              LONGTEXT CHARACTER SET utf8mb4,
    `user_id`           BIGINT,
    `create_date`       datetime default null,
    `update_date`       datetime default null,

    constraint `fk__news_feed__user_id` foreign key (`user_id`) references `user` (`id`)
);


CREATE TABLE IF NOT EXISTS news_feed_comment (
    `id`                BIGINT AUTO_INCREMENT PRIMARY KEY,
    `body`              LONGTEXT CHARACTER SET utf8mb4,
    `user_id`           BIGINT,
    `news_feed_id`      BIGINT,
    `create_date`       datetime default null,
    `update_date`       datetime default null,

    constraint `fk__news_feed_comment__user_id` foreign key (`user_id`) references `user` (`id`),
    constraint `fk__news_feed_comment__news_feed_id` foreign key (`news_feed_id`) references `news_feed` (`id`)
);
