create sequence hibernate_sequence;

create table if not exists subjects
(
    subject_id     bigserial not null,
    subject_name   text   not null,
    field_of_study text   not null,
    constraint subjects_pk
        primary key (subject_id)
);

create unique index if not exists subjects_subject_id_uindex
    on subjects (subject_id);

create table if not exists representatives
(
    representative_id bigserial not null,
    first_name        text   not null,
    last_name         text   not null,
    email             text   not null,
    telephone_number  text   not null,
    position          text,
    constraint representatives_pk
        primary key (representative_id),
    constraint employer_email_key
        unique (email)
);

create table if not exists users
(
    user_id  bigserial not null,
    email    text   not null,
    password text   not null,
    role     text   not null,
    constraint users_pkey
        primary key (user_id),
    constraint users_email_key
        unique (email)
);

create table if not exists coordinators
(
    user_id          bigserial not null,
    first_name       text   not null,
    last_name        text   not null,
    telephone_number text   not null,
    field_of_study   text   not null,
    position         text   not null,
    constraint coordinators_pk
        primary key (user_id),
    constraint coordinator_user_id_fkey
        foreign key (user_id) references users
);

create unique index if not exists coordinators_user_id_uindex
    on coordinators (user_id);

create unique index if not exists coordinators_field_of_study_uindex
    on coordinators (field_of_study);

create table if not exists addresses
(
    address_id       bigserial not null,
    city             text   not null,
    street           text   not null,
    building_number  text   not null,
    zip_code         text   not null,
    post_office      text   not null,
    apartment_number text,
    constraint addresses_pk
        primary key (address_id)
);

create table if not exists companies
(
    company_id            bigserial                not null,
    representative_id     bigint                not null,
    address_id            bigint                not null,
    free_spaces           integer               not null,
    is_part_of_internship boolean default false not null,
    company_name          text                  not null,
    starting_date         date                  not null,
    ending_date           date                  not null,
    field_of_study        text,
    constraint companies_pk
        primary key (company_id),
    constraint companies_addresses_address_id_fkey
        foreign key (address_id) references addresses,
    constraint companies_representatives_representative_id_fkey
        foreign key (representative_id) references representatives
);

create table if not exists students
(
    user_id             bigint        not null,
    first_name          text          not null,
    last_name           text          not null,
    telephone_number    text          not null,
    student_index       integer       not null,
    field_of_study      text          not null,
    degree              text          not null,
    average_grade       numeric(3, 2) not null,
    coordinator_user_id bigint,
    address_id          bigint        not null,
    company_id          bigint,
    constraint students_pk
        primary key (user_id),
    constraint students_users_user_id_fkey
        foreign key (user_id) references users,
    constraint students_coordinator_id_fkey
        foreign key (coordinator_user_id) references coordinators,
    constraint students_address_address_id_fkey
        foreign key (address_id) references addresses,
    constraint students_companies_company_id_fkey
        foreign key (company_id) references companies
);

create unique index if not exists students_student_index_uindex
    on students (student_index);

create unique index if not exists students_user_id_uindex
    on students (user_id);

create table if not exists grades
(
    student_id bigint not null,
    subject_id bigint not null,
    grade      numeric(2, 1) default NULL::numeric,
    grade_id   bigserial not null,
    constraint grades_pk
        primary key (grade_id),
    constraint grades_students_user_id_fk
        foreign key (student_id) references students,
    constraint grades_subjects_subject_id_fk
        foreign key (subject_id) references subjects
);

create unique index if not exists companies_company_id_uindex
    on companies (company_id);

create table if not exists journals
(
    entry_id      bigserial  not null,
    day           date    not null,
    starting_hour time    not null,
    ending_hour   time    not null,
    description   text    not null,
    hours         integer not null,
    student_id    bigint,
    constraint traineejournal_pk
        primary key (entry_id),
    constraint traineejournal_student_id_fkey
        foreign key (student_id) references students
);

create table if not exists interns
(
    intern_id  bigserial               not null,
    student_id bigint,
    company_id bigint,
    reserve    boolean default true not null,
    constraint interns_pk
        primary key (intern_id),
    constraint interns_students_user_id_fkey
        foreign key (student_id) references students
);

create table if not exists guardians
(
    guardian_id      bigserial not null,
    first_name       text   not null,
    last_name        text   not null,
    telephone_number text   not null,
    email            text   not null,
    position         text   not null,
    company_id       bigint,
    constraint guardians_pk
        primary key (guardian_id),
    constraint guardians_companies_company_id_fkey
        foreign key (company_id) references companies
);

create table if not exists internship_plan
(
    internship_plan_id bigserial not null,
    company_id         bigint not null,
    description        text   not null,
    constraint internship_plan_pk
        primary key (internship_plan_id),
    constraint internship_plan_companies_company_id_fkey
        foreign key (company_id) references companies
);

