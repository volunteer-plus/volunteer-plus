create table if not exists `add_request`
(
    `id`              bigint primary key auto_increment,
    `request_id`      LONGTEXT CHARACTER SET utf8mb4 not null,
    `executed`        bit not null default 0,
    `create_date`     datetime default null,
    `update_date`     datetime default null
);
