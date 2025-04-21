-- 휴일 근무 보고서
create table documents_overtime_works
(
    id                 bigserial               not null primary key,
    created_date       timestamp default now() not null,
    created_by         varchar(50)             not null,
    last_modified_date timestamp,
    last_modified_by   varchar(50)
);

create table overtime_works_times
(
    id                          bigserial                   not null primary key,
    document_id                 bigint                      not null,
    start_date                  timestamp                   not null,
    end_date                    timestamp                   not null,
    applied_hours               numeric(8, 4) default 0     not null,
    user_unique_id              varchar(41),
    username                    varchar(50)                 not null,
    contents                    varchar(250)                not null,
    is_day_off                  bool          default false not null,
    is_extra_payment            bool          default false not null,
    applied_extra_payment_hours numeric(8, 4) default 0     not null,

    foreign key (document_id) references documents_overtime_works (id)
);

create table overtime_works_times_reports
(
    id      bigserial not null primary key,
    time_id bigint    not null,
    report  text      not null,

    foreign key (time_id) references overtime_works_times (id)
);

