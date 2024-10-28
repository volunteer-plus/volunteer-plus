create table if not exists `email_template`
(
    `id`                      bigint primary key auto_increment,
    `email_message_tag`       varchar(255),
    `subject`                 LONGTEXT CHARACTER SET utf8mb4 not null,
    `body`                    LONGTEXT CHARACTER SET utf8mb4 not null,
    `create_date`             datetime default null,
    `update_date`             datetime default null
);

create table if not exists `email_notification`
(
    `id`                      bigint primary key auto_increment,
    `email_template_id`       bigint,
    `subject_data`            LONGTEXT CHARACTER SET utf8mb4 not null,
    `template_data`           LONGTEXT CHARACTER SET utf8mb4 not null,
    `is_deleted`              bit not null,
    `draft`                   bit not null,
    `status`                  varchar(255) not null,
    `create_date`             datetime default null,
    `update_date`             datetime default null,
    constraint `fk__email_notification_id` foreign key (`email_template_id`) references `email_template` (`id`)
);


create table if not exists `email_recipient`
(
    `id`                      bigint primary key auto_increment,
    `create_date`             datetime default null,
    `update_date`             datetime default null,
    `email_notification_id`   bigint,
    `sent`                    bit not null,
    `to_recipient`            bit not null,
    `cc`                      bit not null,
    `bcc`                     bit not null,
    `email_address`           varchar(255) not null,
    `full_name`               varchar(255),
    `sent_date`               datetime,
    constraint `fk__email_recipient_id` foreign key (`email_notification_id`) references `email_notification` (`id`)
);

create table if not exists `email_attachment`
(
    `id`                      bigint primary key auto_increment,
    `create_date`             datetime default null,
    `update_date`             datetime default null,
    `email_notification_id`   bigint,
    `filename`                LONGTEXT CHARACTER SET utf8mb4 not null,
    `s3_link`                 LONGTEXT CHARACTER SET utf8mb4 not null,
    constraint `fk__email_attachment_id` foreign key (`email_notification_id`) references `email_notification` (`id`)
);
