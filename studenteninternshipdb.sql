create table employers
(
    employer_id      bigint       not null
        constraint employer_pkey
            primary key,
    user_id          bigint       not null,
    first_name       varchar(30)  not null,
    last_name        varchar(30)  not null,
    email            varchar(50)
        constraint employer_email_key
            unique,
    telephone_number varchar(200) not null,
    company_id       bigint       not null
);

create table companies
(
    company_id      varchar(200) not null
        constraint company_pkey
            primary key,
    company_name    varchar(500) not null,
    city            varchar(200) not null,
    street          varchar(300) not null,
    building_number varchar(20)  not null
);

create table users
(
    user_id  bigint      not null
        constraint users_pkey
            primary key,
    email    varchar(50) not null
        constraint users_email_key
            unique,
    password varchar(30) not null,
    role     varchar(20) not null
);

create table coordinators
(
    user_id          bigint       not null
        constraint coordinator_user_id_fkey
            references users,
    first_name       varchar(30)  not null,
    last_name        varchar(30)  not null,
    telephone_number varchar(200) not null,
    field_of_study   varchar(300) not null
);

create table students
(
    user_id             bigint       not null
        constraint students_user_id_fkey
            references users,
    first_name          varchar(30)  not null,
    last_name           varchar(30)  not null,
    telephone_number    varchar(200) not null,
    student_index       integer      not null
        constraint student_pkey
            primary key,
    field_of_study      varchar(300) not null,
    degree              varchar(200) not null,
    employer_id         bigint       not null,
    average_grade       numeric(3, 2),
    coordinator_user_id bigint
        constraint students_coordinator_id_fkey
            references coordinators (user_id)
);

create table grades
(
    student_index integer      not null
        constraint grades_student_index_fkey
            references students,
    subject_id    varchar(200) not null
        constraint grades_pk
            primary key,
    grade         numeric(2, 2)
);

create table subjects
(
    subject_id   varchar(200) not null
        constraint subjects_pkey
            primary key
        constraint subjects_subject_id_fkey
            references grades,
    subject_name varchar(500)
);

create unique index coordinators_user_id_uindex
    on coordinators (user_id);


