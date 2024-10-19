alter table military_personnel add `user_id` bigint not null;
alter table military_personnel add constraint `fk__military_personnel__user_id` foreign key (`user_id`) references `user` (`id`);


create table if not exists `add_request`
(
    `id`              bigint primary key auto_increment,
    `request_id`      LONGTEXT CHARACTER SET utf8mb4 not null,
    `executed`        bit not null default 0,
    `create_date`     datetime default null,
    `update_date`     datetime default null
);
