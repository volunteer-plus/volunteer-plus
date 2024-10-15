create table if not exists `user`
(
    `id`              bigint primary key auto_increment,
    `email`           varchar(255) not null unique key,
    `password`        varchar(255),
    `status`          varchar(100),
    `user_role`       varchar(100),
    `first_name`      varchar(255),
    `last_name`       varchar(255),
    `phone_number`    varchar(255) default null,
    `create_date`     datetime default null,
    `update_date`     datetime default null
);


create table if not exists `volunteer`
(
    `id`                bigint primary key auto_increment,
    `reputation_score`  float not null,
    `create_date`       datetime default null,
    `update_date`       datetime default null,
    `user_id`           bigint,
    constraint `fk__volunteer__user_id` foreign key (`user_id`) references `user` (`id`)
);


create table if not exists `brigade`
(
    `id`                      bigint primary key auto_increment,
    `regiment_code`           varchar(255) not null unique key,
    `branch`                  varchar(255),
    `role`                    varchar(255),
    `part_of`                 varchar(255),
    `website_link`            varchar(255),
    `current_commander`       varchar(255),
    `description`             LONGTEXT CHARACTER SET utf8mb4,
    `create_date`             datetime default null,
    `update_date`             datetime default null
);


create table if not exists `military_personnel`
(
    `id`                      bigint primary key auto_increment,
    `first_name`              varchar(255) not null,
    `last_name`               varchar(255) not null,
    `date_of_birth`           datetime not null,
    `place_of_birth`          varchar(255) not null,
    `rank`                    varchar(100),
    `status`                  varchar(100),
    `brigade_id`              bigint not null,
    `create_date`             datetime default null,
    `update_date`             datetime default null,
    constraint `fk__military_personnel__brigade_id` foreign key (`brigade_id`) references `brigade` (`id`)
);


create table if not exists `request`
(
    `id`                      bigint primary key auto_increment,
    `military_personnel_id`   bigint,
    `user_id`                 bigint,
    `description`             LONGTEXT CHARACTER SET utf8mb4 not null,
    `deadline`                datetime,
    `amount`                  float,
    `create_date`             datetime default null,
    `update_date`             datetime default null,
    constraint `fk__request__military_personnel_id` foreign key (`military_personnel_id`) references `military_personnel` (`id`),
    constraint `fk__request__user_id` foreign key (`user_id`) references `user` (`id`)
);


create table if not exists `levy`
(
    `id`                      bigint primary key auto_increment,
    `request_id`              bigint,
    `accumulated`             float,
    `trophy_description`      LONGTEXT CHARACTER SET utf8mb4,
    `create_date`             datetime default null,
    `update_date`             datetime default null
);


create table if not exists `volunteer_levy`
(
    `volunteer_id`    bigint not null,
    `levy_id`         bigint not null,

    constraint `fk_volunteer_id` foreign key (`volunteer_id`) references `volunteer` (`id`),
    constraint `fk_levy_id` foreign key (`levy_id`) references `levy` (`id`)
);


create table if not exists `report`
(
    `id`                      bigint primary key auto_increment,
    `data`                    LONGTEXT CHARACTER SET utf8mb4,
    `levy_id`                 bigint,
    `create_date`             datetime default null,
    `update_date`             datetime default null,
    constraint `fk__report_levy_id` foreign key (`levy_id`) references `levy` (`id`)
);


create table if not exists `attachment`
(
    `id`                      bigint primary key auto_increment,
    `create_date`             datetime default null,
    `update_date`             datetime default null,
    `filepath`                LONGTEXT CHARACTER SET utf8mb4,
    `report_id`               bigint,
    constraint `fk__report_id` foreign key (`report_id`) references `report` (`id`)
);
