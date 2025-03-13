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