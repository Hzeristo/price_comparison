drop table if exists `user_roles`;
drop table if exists `role_permissions`;
drop table if exists `users`;
drop table if exists `roles`;
drop table if exists `price_history`;
drop table if exists `permissions`;
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

create table `roles`(
    `id` int not null auto_increment,
    `name` varchar(31),
    primary key(`id`),
    unique key(`name`)
) engine=innodb charset=utf8mb4;

create table `permissions`(
    `id` int not null auto_increment,
    `name` varchar(31),
    primary key(`id`),
    unique key(`name`)
) engine=innodb charset=utf8mb4;

create table `user_roles`(
    `user_id` int not null,
    `role_id` int not null,
    primary key(`user_id`, `role_id`),
    foreign key(`user_id`) references `users`(`id`) on delete cascade,
    foreign key(`role_id`) references `roles`(`id`) on delete cascade
) engine=innodb charset=utf8mb4;

create table `role_permissions`(
    `role_id` int not null,
    `permission_id` int not null,
    primary key(`role_id`, `permission_id`),
    foreign key(`role_id`) references `roles`(`id`) on delete cascade,
    foreign key(`permission_id`) references `permissions`(`id`) on delete cascade
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

create table `items`(
    `id` int not null auto_increment,
    `name` varchar(255) not null,
    `category_id` int not null,
    `platform` varchar(15) not null,
    `image` varchar(255) not null,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key(`id`),
    foreign key(`category_id`) references `categories`(`id`) on delete cascade
) engine=innodb charset=utf8mb4;

create table `price_history`(
    `id` int not null auto_increment,
    `product_id` int not null,
    `price` decimal(10, 2) not null,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key(`id`),
    foreign key(`product_id`) references `items`(`id`) on delete cascade
) engine=innodb charset=utf8mb4;