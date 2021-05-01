create sequence hibernate_sequence;

create table subjects
(
    subject_id     bigint       not null,
    subject_name   varchar(500) not null,
    field_of_study varchar(200) not null,
    constraint subjects_pk
        primary key (subject_id)
);

create unique index subjects_subject_id_uindex
    on subjects (subject_id);

create table representatives
(
    representative_id bigint       not null,
    first_name        varchar(30)  not null,
    last_name         varchar(30)  not null,
    email             varchar(50)  not null,
    telephone_number  varchar(200) not null,
    position          varchar(200),
    constraint representatives_pk
        primary key (representative_id),
    constraint employer_email_key
        unique (email)
);

create table users
(
    user_id  bigint      not null,
    email    varchar(50) not null,
    password varchar(30) not null,
    role     varchar(20) not null,
    constraint users_pkey
        primary key (user_id),
    constraint users_email_key
        unique (email)
);

create table coordinators
(
    user_id          bigint       not null,
    first_name       varchar(30)  not null,
    last_name        varchar(30)  not null,
    telephone_number varchar(200) not null,
    field_of_study   varchar(300) not null,
    position         varchar(200),
    constraint coordinator_user_id_fkey
        foreign key (user_id) references users
);

create unique index coordinators_user_id_uindex
    on coordinators (user_id);

create table addresses
(
    address_id       bigint       not null,
    city             varchar(200) not null,
    street           varchar(200) not null,
    building_number  varchar(200) not null,
    zip_code         varchar(200) not null,
    post_office      varchar(200) not null,
    apartment_number varchar(200),
    constraint addresses_pk
        primary key (address_id)
);

create table guardians
(
    guardian_id      bigint       not null,
    first_name       varchar(200) not null,
    last_name        varchar(200) not null,
    telephone_number varchar(200) not null,
    email            varchar(200) not null,
    position         varchar(200) not null,
    constraint guardians_pk
        primary key (guardian_id)
);

create table companies
(
    company_id            bigint                not null,
    representative_id     bigint                not null,
    address_id            bigint                not null,
    free_spaces           integer               not null,
    is_part_of_internship boolean default false not null,
    company_name          varchar(200)          not null,
    starting_date         date                  not null,
    ending_date           date                  not null,
    guardian_id           bigint                not null,
    constraint companies_pk
        primary key (company_id),
    constraint companies_addresses_address_id_fkey
        foreign key (address_id) references addresses,
    constraint companies_representatives_representative_id_fkey
        foreign key (representative_id) references representatives,
    constraint comapnies_guardians_guardian_id_fkey
        foreign key (guardian_id) references guardians
);

create table students
(
    user_id             bigint        not null,
    first_name          varchar(30)   not null,
    last_name           varchar(30)   not null,
    telephone_number    varchar(200)  not null,
    student_index       integer       not null,
    field_of_study      varchar(300)  not null,
    degree              varchar(200)  not null,
    average_grade       numeric(3, 2) not null,
    coordinator_user_id bigint,
    address_id          bigint        not null,
    company_id          bigint,
    constraint students_pk
        primary key (user_id),
    constraint students_user_id_fkey
        foreign key (user_id) references users,
    constraint students_coordinator_id_fkey
        foreign key (coordinator_user_id) references coordinators (user_id),
    constraint students_address_address_id_fkey
        foreign key (address_id) references addresses,
    constraint students_companies_company_id_fkey
        foreign key (company_id) references companies
);

create unique index students_student_index_uindex
    on students (student_index);

create unique index students_user_id_uindex
    on students (user_id);

create table grades
(
    student_id bigint                  not null,
    subject_id bigint                  not null,
    grade      numeric(2, 1) default 0 not null,
    grade_id   bigint                  not null,
    constraint grades_pk
        primary key (grade_id),
    constraint grades_students_user_id_fk
        foreign key (student_id) references students,
    constraint grades_subjects_subject_id_fk
        foreign key (subject_id) references subjects
);

create unique index companies_company_id_uindex
    on companies (company_id);

create table journals
(
    entry_id      bigserial     not null,
    day           date          not null,
    starting_hour time          not null,
    ending_hour   time          not null,
    description   text          not null,
    hours         numeric(3, 1) not null,
    student_id    bigint,
    constraint traineejournal_pk
        primary key (entry_id),
    constraint traineejournal_student_id_fkey
        foreign key (student_id) references students
);

create table interns
(
    intern_id  bigint               not null,
    student_id bigint               not null,
    company_id bigint,
    reserve    boolean default true not null,
    constraint interns_pk
        primary key (intern_id),
    constraint interns_students_user_id_fkey
        foreign key (student_id) references students,
    constraint interns_companies_company_id_fkey
        foreign key (company_id) references companies
);

create table internship_plan
(
    internship_plan_id bigint not null,
    intern_id          bigint not null,
    coordinator_id     bigint not null,
    description        text   not null,
    constraint internship_plan_pk
        primary key (internship_plan_id)
);


