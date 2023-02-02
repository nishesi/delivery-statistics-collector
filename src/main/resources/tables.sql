drop table if exists expenses;
drop table if exists shifts;
drop table if exists work_dates;
drop table if exists authorities;
drop table if exists users;

delete from expenses;
delete from shifts;
delete from work_dates;
delete from authorities;
delete from users;

-- Common tables

create table users
(
    id       BIGSERIAL PRIMARY KEY,
    email    varchar(50) UNIQUE,
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


-- Work tables

CREATE TABLE work_dates
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT REFERENCES users (id),
    date          DATE NOT NULL,
    delivery_gain INT  NOT NULL CHECK ( delivery_gain >= 0 ),
    comment       VARCHAR(255)
);

CREATE TABLE shifts
(
    id            BIGSERIAL PRIMARY KEY,
    date_id       BIGINT REFERENCES work_dates (id),
    start_time    TIME          NOT NULL,
    end_time      TIME          NOT NULL,
    average_ratio NUMERIC(4, 2) NOT NULL CHECK ( average_ratio >= 0 ),
    orders_count  SMALLINT CHECK (orders_count >= 0),
    gain          INT CHECK (gain >= 0),
    distance      NUMERIC(4, 2) NOT NULL CHECK (distance >= 0)
);

CREATE TABLE expenses
(
    id      BIGSERIAL PRIMARY KEY,
    date_id BIGINT REFERENCES work_dates (id),
    name    VARCHAR(50) NOT NULL,
    value   SMALLINT    NOT NULL CHECK ( value > 0 )
);



select count(*) from work_dates group by user_id;