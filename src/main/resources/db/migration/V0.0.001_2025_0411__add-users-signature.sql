-- 사용자 서명
create table users_signatures
(
    id                 bigserial               not null primary key,
    user_unique_id     varchar(41)             not null,
    signature_image    bytea,
    created_date       timestamp default now() not null,
    created_by         varchar(50)             not null,
    last_modified_date timestamp,
    last_modified_by   varchar(50)
);
