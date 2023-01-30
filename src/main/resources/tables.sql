drop table if exists authorities;
drop table if exists users;

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

CREATE TABLE work_days
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT REFERENCES users (id),
    day           DATE NOT NULL,
    delivery_gain INT  NOT NULL CHECK ( delivery_gain >= 0 ),
    comment       VARCHAR(255)
);

CREATE TABLE shifts
(
    id            BIGSERIAL PRIMARY KEY,
    day_id        BIGINT REFERENCES work_days (id),
    interval      INTERVAL      NOT NULL,
    average_ratio NUMERIC(4, 2) NOT NULL CHECK ( average_ratio >= 0 ),
    orders_count  SMALLINT CHECK (orders_count >= 0),
    gain          INT CHECK (gain >= 0),
    distance      NUMERIC(4, 2) NOT NULL CHECK (distance >= 0)
);

CREATE TABLE expenses
(
    id     BIGSERIAL PRIMARY KEY,
    day_id BIGINT REFERENCES work_days (id),
    name   VARCHAR(50) NOT NULL,
    value  SMALLINT    NOT NULL CHECK ( value > 0 )
);