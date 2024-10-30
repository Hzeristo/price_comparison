drop table if exists `favorites`;
drop table if exists `users`;
drop table if exists `price_history`;
drop table if exists `items`;
drop table if exists `categories`;

create table `users`(
    `id` int not null auto_increment,
    `email` varchar(255) not null,
    `username` varchar(255) not null,
    `password` varchar(255) not null,
    `role` varchar(31) not null default 'USER',
    `phone` varchar(31) not null default '',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key(`id`),
    unique key(`username` ,`email`, `phone`)
) engine=innodb charset=utf8mb4;

create table `categories`(
    `id` int not null auto_increment,
    `name` varchar(255) not null,
    `parent_id` int default null,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key(`id`),
    foreign key(`parent_id`) references `categories`(`id`) on delete cascade
) engine=innodb charset=utf8mb4;

create table `items` (
    `id` int not null auto_increment,
    `sku_id` varchar(63) not null default '', -- 默认值为空字符串
    `name` varchar(255) not null default '', -- 默认值为空字符串
    `category_id` int not null default 0, -- 不设置默认值，保持不变
    `platform` varchar(15) not null default '', -- 默认值为空字符串
    `image` varchar(255) not null default '', -- 默认值为空字符串
    `url` varchar(255) not null default '', -- 默认值为空字符串
    `price` decimal(10, 2) not null default 0.00, -- 默认值为0.00
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    primary key (`id`),
    foreign key (`category_id`) references `categories` (`id`) on delete cascade
) engine=innodb charset=utf8mb4;


create table `price_history`(
    `id` int not null auto_increment,
    `item_id` int not null,
    `platform` varchar(15) not null,
    `price` decimal(10, 2) not null,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key(`id`),
    foreign key(`item_id`) references `items`(`id`) on delete cascade
) engine=innodb charset=utf8mb4;

create table `favorites`(
    `id` int not null auto_increment,
    `user_id` int not null,
    `item_id` int not null,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key(`id`),
    foreign key(`user_id`) references `users`(`id`) on delete cascade,
    foreign key(`item_id`) references `items`(`id`) on delete cascade
) engine=innodb charset=utf8mb4;