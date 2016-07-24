create database if not exists notepadsql DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'notepadsql'@'%' IDENTIFIED BY 'notepadsql';
CREATE USER IF NOT EXISTS 'notepadsql'@'localhost' IDENTIFIED BY 'notepadsql';
grant all on notepadsql.* to 'notepadsql'@'%';
grant all on notepadsql.* to 'notepadsql'@'localhost';
flush privileges;
use notepadsql;

create table if not exists users(
	usersid int not null auto_increment,
    login varchar(32) not null unique,
    password varchar(32) not null,
    primary key(usersid)
);

create table if not exists categories(
	categoriesid int not null auto_increment,
    name varchar(32) not null,
    description text,
    categories_usersid int not null,
    foreign key (categories_usersid) references users(usersid),
    primary key(categoriesid)
);

create table if not exists notes(
	notesid int not null auto_increment,
    title varchar(32) not null,
    content text,
    notes_categoriesid int not null,
    notes_usersid int not null,
    foreign key (notes_categoriesid) references categories(categoriesid),
    foreign key (notes_usersid) references users(usersid),
    primary key(notesid)
);