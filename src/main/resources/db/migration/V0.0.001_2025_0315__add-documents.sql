-- 사용자 보상 휴가 정보
create table users_comp_leave_entries
(
    id                 bigserial                   not null primary key,
    user_unique_id     varchar(41)                 not null,
    contents           varchar(500)                not null,
    description        text,
    effective_date     date                        not null,
    leave_days         numeric(3, 1) default 0     not null,
    used_days          numeric(3, 1) default 0     not null,
    created_date       timestamp     default now() not null,
    created_by         varchar(200)                not null,
    last_modified_date timestamp,
    last_modified_by   varchar(200)
);

-- 업무 일정
create table work_schedules
(
    id                 bigserial               not null primary key,
    contents           varchar(50)             not null,
    description        text,
    start_date         date                    not null,
    end_date           date                    not null,
    is_repeated        bool      default false not null,
    is_closed          bool      default false not null,
    created_date       timestamp default now() not null,
    created_by         varchar(200)            not null,
    last_modified_date timestamp,
    last_modified_by   varchar(200)
);

-- 문서
create table documents
(
    id                 bigserial               not null primary key,
    type               varchar(50)             not null,
    status             varchar(50)             not null,
    user_unique_id     varchar(41)             not null,
    created_date       timestamp default now() not null,
    created_by         varchar(200)            not null,
    last_modified_date timestamp,
    last_modified_by   varchar(200)
);

-- 휴가 신청서
create table documents_vacations
(
    id                bigint        not null primary key,
    vacation_type     varchar(50)   not null,
    vacation_sub_type varchar(50),
    start_date        date          not null,
    end_date          date          not null,
    used_days         numeric(3, 1) not null,
    reason            varchar(500)  not null,

    foreign key (id) references documents (id)
);

-- 보상 휴가 사용 목록
create table users_vacations_used_comp_leaves
(
    id                  bigserial                   not null primary key,
    document_id         bigint                      not null,
    comp_leave_entry_id bigint                      not null,
    used_days           numeric(3, 1) default 0     not null,
    created_date        timestamp     default now() not null,
    last_modified_date  timestamp,

    foreign key (document_id) references documents_vacations (id),
    foreign key (comp_leave_entry_id) references users_comp_leave_entries (id)
);