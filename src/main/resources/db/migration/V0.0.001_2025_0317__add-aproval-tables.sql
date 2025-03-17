-- 결재 라인
create table approval_lines
(
    id                 bigserial               not null primary key,
    p_id               bigint,
    team_id            bigint                  not null,
    user_unique_id     varchar(41)             not null,
    contents           varchar(50)             not null,
    description        text,
    created_date       timestamp default now() not null,
    last_modified_date timestamp,

    foreign key (p_id) references approval_lines (id)
);

-- 문서 결재 history
create table documents_approval_histories
(
    id                 bigserial                     not null primary key,
    document_id        bigint                        not null,
    line_id            bigint                        not null,
    status             varchar(50) default 'WAITING' not null,
    reason             varchar(500),
    created_date       timestamp   default now()     not null,
    created_by         varchar(50),
    last_modified_date timestamp,
    last_modified_by   varchar(50),

    foreign key (document_id) references documents (id),
    foreign key (line_id) references approval_lines (id)
)