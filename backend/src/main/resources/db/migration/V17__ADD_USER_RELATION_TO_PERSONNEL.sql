alter table military_personnel add `user_id` BIGINT;
alter table military_personnel add constraint `fk__military_personnel_user_id` foreign key (`user_id`) references `user` (`id`)
