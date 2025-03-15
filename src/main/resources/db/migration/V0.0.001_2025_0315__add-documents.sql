-- 사용자 보상 휴가 정보
create table users_comp_leave_entries
(
    id                 bigserial                   not null primary key,
    user_unique_id     varchar(41)                 not null,
    contents           varchar(500)                not null,
    description        text,
    effective_date     timestamp                   not null,
    leave_days         numeric(3, 1) default 0     not null,
    used_days          numeric(3, 1) default 0     not null,
    created_date       timestamp     default now() not null,
    created_by         varchar(200)                not null,
    last_modified_date timestamp,
    last_modified_by   varchar(200)
);

-- 문서
create table documents
(
    id                 bigserial               not null primary key,
    type               varchar(50)             not null,
    status             varchar(50)             not null,
    writer_id          varchar(41)             not null,
    created_date       timestamp default now() not null,
    created_by         varchar(200)            not null,
    last_modified_date timestamp,
    last_modified_by   varchar(200)
);

-- 휴가 신청서
create table documents_vacations
(
    id                  bigint            not null primary key,
    vacation_type       varchar(50)       not null,
    vacation_sub_type   varchar(50),
    start_date          date              not null,
    end_date            date              not null,
    used_days           int               not null,
    reason              varchar(500)      not null,
    used_comp_leave_ids json default '[]' not null,

    foreign key (id) references documents (id)
)
