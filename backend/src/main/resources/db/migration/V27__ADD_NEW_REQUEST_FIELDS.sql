alter table `request` add category varchar(500);
alter table `request` add status varchar(255);

alter table `levy` add goal_amount float not null default 0;
alter table `levy` add category varchar(500);
alter table `levy` add status varchar(255);
alter table `levy` add description LONGTEXT CHARACTER SET utf8mb4;
