drop table if exists authorities;
drop table if exists users;

create table users
(
    email    varchar(50) primary key,
    username varchar(50)  not null,
    password varchar(500) not null
);

create table authorities
(
    email     varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key (email) references users (email) on delete cascade
);
create unique index ix_auth_username on authorities (email, authority);