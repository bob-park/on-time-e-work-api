-- 사용자 고용 정보
create table users_employments
(
    id                 bigserial               not null primary key,
    user_unique_id     varchar(41)             not null,
    effective_date     timestamp               not null,
    status             varchar(50)             not null,
    created_date       timestamp default now() not null,
    created_by         varchar(200)            not null,
    last_modified_date timestamp,
    last_modified_by   varchar(200)
);

-- 연차 증감 정보
create table leave_days_policies
(
    id                 bigserial               not null primary key,
    continuous_years   int                     not null,
    total_leave_days   int       default 0     not null,
    created_date       timestamp default now() not null,
    created_by         varchar(200)            not null,
    last_modified_date timestamp,
    last_modified_by   varchar(200)
)

-- 사용자 연차 정보
create table users_leave_entries
(
    id                    bigserial                   not null primary key,
    user_unique_id        varchar(41)                 not null,
    year                  int                         not null,
    total_leave_days      numeric(3, 1) default 0     not null, -- 전체 휴가 개수
    used_leave_days       numeric(3, 1) default 0     not null,
    total_comp_leave_days numeric(3, 1) default 0     not null, -- 전체 보상 휴가
    used_comp_leave_days  numeric(3, 1) default 0     not null,
    created_date          timestamp     default now() not null,
    last_modified_date    timestamp
)