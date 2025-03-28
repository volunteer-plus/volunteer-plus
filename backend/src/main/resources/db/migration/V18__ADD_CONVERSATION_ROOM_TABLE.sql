alter table `ws_message` drop column if exists `to_user`;
alter table `ws_message` drop column if exists `conversation_id`;

CREATE TABLE IF NOT EXISTS conversation_room (
    `id`                BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name`              VARCHAR(255) NOT NULL,
    `create_date`       datetime default null,
    `update_date`       datetime default null
);

alter table `ws_message` add `conversation_room_id` BIGINT;
alter table `ws_message` add constraint `fk__conversation_ws_message_id` foreign key (`conversation_room_id`) references `conversation_room` (`id`);

create table if not exists `conversation_room_user`
(
    `conversation_room_id`    bigint not null,
    `user_id`         bigint not null,

    constraint `fk_conversation_room_id` foreign key (`conversation_room_id`) references `conversation_room` (`id`),
    constraint `fk_user_id` foreign key (`user_id`) references `user` (`id`)
);
