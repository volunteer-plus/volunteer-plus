create table if not exists `volunteer_feedback`
(
    `id`                bigint primary key auto_increment,
    `reputation_score`  float not null,
    `text`              LONGTEXT CHARACTER SET utf8mb4,
    `create_date`       datetime default null,
    `update_date`       datetime default null,
    `volunteer_id`      bigint,
    `author_id`         bigint,
    constraint `fk__volunteer__volunteer_feedback_id` foreign key (`volunteer_id`) references `volunteer` (`id`),
    constraint `fk__user__volunteer_feedback_id` foreign key (`author_id`) references `user` (`id`)
);

alter table `volunteer` drop column reputation_score;

INSERT INTO email_template(email_message_tag, subject, body) VALUES
    (
        'EMAIL_MESSAGE_TAG_2',
        'Волонтер+: новий фідбек від [(${name})]',
        '
           <!DOCTYPE html>
           <html>
           <head>
               <title>Новий фідбек для волонтера на платформі "Волонтер+" доступний для перегляду</title>
           </head>
           <body>
               <h1>Оцінка: <span th:text="\${score}"></span></h1>
               <p><span th:text="\${text}"></span>,</p>
               <p>Дякуємо за вашу активність на нашій платформі.</p>
               <p>З повагою,<br>Команда Волонтер+</p>
           </body>
           </html>
        '
    );

