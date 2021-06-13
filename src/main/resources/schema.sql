create sequence if not exists hibernate_sequence;

create table if not exists subjects
(
    subject_id     bigserial not null,
    subject_name   text      not null,
    field_of_study text      not null,
    constraint subjects_pk
        primary key (subject_id),
    constraint subjects_subject_name_ck
        check ( subject_name <> '' ),
    constraint subjects_field_of_study_ck
        check ( field_of_study <> '' )
);

create unique index if not exists subjects_subject_id_uindex
    on subjects (subject_id);

create table if not exists representatives
(
    representative_id bigserial not null,
    first_name        text      not null,
    last_name         text      not null,
    email             text      not null,
    telephone_number  text      not null,
    position          text,
    constraint representatives_pk
        primary key (representative_id),
    constraint employer_email_key
        unique (email),
    constraint subjects_first_name_ck
        check ( first_name <> '' ),
    constraint subjects_last_name_ck
        check ( last_name <> '' ),
    constraint subjects_email_ck
        check ( email <> '' ),
    constraint subjects_telephone_number_ck
        check ( telephone_number <> '' ),
    constraint subjects_position_ck
        check ( position <> '' )
);

create table if not exists users
(
    user_id  bigserial not null,
    email    text      not null,
    password text      not null,
    role     text      not null,
    constraint users_pkey
        primary key (user_id),
    constraint users_email_key
        unique (email),
    constraint subjects_email_ck
        check ( email <> '' ),
    constraint subjects_password_ck
        check ( password <> '' ),
    constraint subjects_role_ck
        check ( role <> '' )
);

create table if not exists coordinators
(
    user_id          bigserial not null,
    first_name       text      not null,
    last_name        text      not null,
    telephone_number text      not null,
    field_of_study   text      not null,
    position         text      not null,
    constraint coordinators_pk
        primary key (user_id),
    constraint coordinator_user_id_fkey
        foreign key (user_id) references users,
    constraint subjects_first_name_ck
        check ( first_name <> '' ),
    constraint subjects_last_name_ck
        check ( last_name <> '' ),
    constraint subjects_telephone_number_ck
        check ( telephone_number <> '' ),
    constraint subjects_position_ck
        check ( position <> '' ),
    constraint subjects_field_of_study_ck
        check ( field_of_study <> '' )

);

create unique index if not exists coordinators_user_id_uindex
    on coordinators (user_id);

create unique index if not exists coordinators_field_of_study_uindex
    on coordinators (field_of_study);

create table if not exists addresses
(
    address_id       bigserial not null,
    city             text      not null,
    street           text      not null,
    building_number  text      not null,
    zip_code         text      not null,
    post_office      text      not null,
    apartment_number text,
    constraint addresses_pk
        primary key (address_id),
    constraint subjects_city_ck
        check ( city <> '' ),
    constraint subjects_street_ck
        check ( street <> '' ),
    constraint subjects_building_number_ck
        check ( building_number <> '' ),
    constraint subjects_zip_code_ck
        check ( zip_code <> '' ),
    constraint subjects_post_office_ck
        check ( post_office <> '' )
);

create table if not exists companies
(
    company_id            bigserial             not null,
    representative_id     bigint                not null,
    address_id            bigint                not null,
    free_spaces           integer               not null,
    is_part_of_internship boolean default false not null,
    company_name          text                  not null,
    starting_date         date                  not null,
    ending_date           date                  not null,
    field_of_study        text                  not null,
    constraint companies_pk
        primary key (company_id),
    constraint companies_addresses_address_id_fkey
        foreign key (address_id) references addresses,
    constraint companies_representatives_representative_id_fkey
        foreign key (representative_id) references representatives,
    constraint subjects_company_name_ck
        check ( company_name <> '' ),
    constraint subjects_field_of_study_ck
        check ( field_of_study <> '' )
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
        foreign key (company_id) references companies,
    constraint subjects_first_name_ck
        check ( first_name <> '' ),
    constraint subjects_last_name_ck
        check ( last_name <> '' ),
    constraint subjects_telephone_number_ck
        check ( telephone_number <> '' ),
    constraint subjects_field_of_study_ck
        check ( field_of_study <> '' ),
    constraint subjects_degree_ck
        check ( degree <> '' )
);

create unique index if not exists students_student_index_uindex
    on students (student_index);

create unique index if not exists students_user_id_uindex
    on students (user_id);

create table if not exists grades
(
    student_id bigint    not null,
    subject_id bigint    not null,
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
    entry_id      bigserial not null,
    day           date      not null,
    starting_hour time      not null,
    ending_hour   time      not null,
    description   text      not null,
    hours         integer   not null,
    student_id    bigint,
    constraint traineejournal_pk
        primary key (entry_id),
    constraint traineejournal_student_id_fkey
        foreign key (student_id) references students,
    constraint subjects_description_ck
        check ( description <> '' )
);

create table if not exists interns
(
    intern_id  bigserial            not null,
    student_id bigint               not null,
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
    first_name       text      not null,
    last_name        text      not null,
    telephone_number text      not null,
    email            text      not null,
    position         text      not null,
    company_id       bigint,
    constraint guardians_pk
        primary key (guardian_id),
    constraint guardians_companies_company_id_fkey
        foreign key (company_id) references companies,
    constraint subjects_first_name_ck
        check ( first_name <> '' ),
    constraint subjects_last_name_ck
        check ( last_name <> '' ),
    constraint subjects_email_ck
        check ( email <> '' ),
    constraint subjects_telephone_number_ck
        check ( telephone_number <> '' ),
    constraint subjects_position_ck
        check ( position <> '' )

);

create table if not exists internship_plan
(
    internship_plan_id bigserial not null,
    company_id         bigint    not null,
    description        text      not null,
    constraint internship_plan_pk
        primary key (internship_plan_id),
    constraint internship_plan_companies_company_id_fkey
        foreign key (company_id) references companies,
    constraint subjects_description_ck
        check ( description <> '' )
);

INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (975, 'Subject1', 'INFORMATYKA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (976, 'Subject2', 'INFORMATYKA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (977, 'Subject3', 'INFORMATYKA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (978, 'Subject4', 'HUMANISTYKA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (979, 'Subject5', 'HUMANISTYKA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (980, 'Subject6', 'HUMANISTYKA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (981, 'Subject7', 'EKONOMIA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (982, 'Subject8', 'EKONOMIA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (983, 'Subject9', 'EKONOMIA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (984, 'Subject10', 'BIOLOGIA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (985, 'Subject11', 'BIOLOGIA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (986, 'Subject12', 'BIOLOGIA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (987, 'Subject13', 'CHEMIA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (988, 'Subject14', 'CHEMIA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (989, 'Subject15', 'CHEMIA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (990, 'Subject16', 'FIZYKA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (991, 'Subject17', 'FIZYKA') on conflict (subject_id) do nothing;
INSERT INTO subjects (subject_id, subject_name, field_of_study) VALUES (992, 'Subject18', 'FIZYKA') on conflict (subject_id) do nothing;

INSERT INTO representatives (representative_id, first_name, last_name, email, telephone_number, position) VALUES (2514, 'Julian', 'Sadowski', 'j.sad@yahoo.mail', '567409123', 'właściciel') on conflict (representative_id) do nothing;
INSERT INTO representatives (representative_id, first_name, last_name, email, telephone_number, position) VALUES (2522, 'Marcel', 'Borkowski', 'm.bor@yahoo.mail', '410533065', 'kierownik') on conflict (representative_id) do nothing;
INSERT INTO representatives (representative_id, first_name, last_name, email, telephone_number, position) VALUES (2529, 'Korneliusz', 'Zieliński', 'k.ziel@yahoo.mail', '951824980', 'właściciel') on conflict (representative_id) do nothing;
INSERT INTO representatives (representative_id, first_name, last_name, email, telephone_number, position) VALUES (2534, 'Borys', 'Głowacka', 'b.glo@yahoo.mail', '544341700', 'kierownik') on conflict (representative_id) do nothing;
INSERT INTO representatives (representative_id, first_name, last_name, email, telephone_number, position) VALUES (2542, 'Borys', 'Włodarczyk', 'b.wlo@yahoo.mail', '722655536', 'kierownik') on conflict (representative_id) do nothing;
INSERT INTO representatives (representative_id, first_name, last_name, email, telephone_number, position) VALUES (2547, 'Remigiusz', 'Andrzejewski', 'r.andrz@yahoo.mail', '884502906', 'pracownik') on conflict (representative_id) do nothing;
INSERT INTO representatives (representative_id, first_name, last_name, email, telephone_number, position) VALUES (2555, 'Maurycy', 'Gajewska', 'm.maj@yahoo.mail', '787453432', 'właściciel') on conflict (representative_id) do nothing;
INSERT INTO representatives (representative_id, first_name, last_name, email, telephone_number, position) VALUES (2559, 'Konstanty', 'Woźniak', 'k.woz@yahoo.mail', '723820667', 'właściciel') on conflict (representative_id) do nothing;
INSERT INTO representatives (representative_id, first_name, last_name, email, telephone_number, position) VALUES (2563, 'Cezary', 'Marciniak', 'c.mar@yahoo.mail', '326364943', 'właściciel') on conflict (representative_id) do nothing;
INSERT INTO representatives (representative_id, first_name, last_name, email, telephone_number, position) VALUES (2569, 'Emil', 'Chmielewski', 'e.chm@yahoo.mail', '856016568', 'kierownik') on conflict (representative_id) do nothing;
INSERT INTO representatives (representative_id, first_name, last_name, email, telephone_number, position) VALUES (2574, 'Ignacy', 'Malinowski', 'i.mal@yahoo.mail', '885920638', 'właściciel') on conflict (representative_id) do nothing;
INSERT INTO representatives (representative_id, first_name, last_name, email, telephone_number, position) VALUES (2579, 'Bogumił', 'Walczak', 'bo.wal@yahoo.mail', '330720431', 'kierownik') on conflict (representative_id) do nothing;

INSERT INTO users (user_id, email, password, role) VALUES (1, 'admin@admin.com', '$2y$12$3XeuDsPWJOWAnzCpGaOfCu6wUYHoHW/0OdfKhI8Jv8qnEaCsEPlMy', 'ROLE_ADMIN') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (969, 'a.witkowski@gmail.com', '$2a$10$MzqRm6IpUwcQ3pVu1DL1TOaIeikkGD0ucUM2SrKjqzIXCRf4ndSZa', 'ROLE_COORDINATOR') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (970, 'a.szewczyk@gmail.com', '$2a$10$uR8.3zxikI0eYX47nOYcQOSX5kGvkGBInM24aS.kvhVPJqkJR6ng2', 'ROLE_COORDINATOR') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (971, 'm.kubiak@gmail.com', '$2a$10$FqCk4XNaY8vvfzw0tkdxYuddRQta1CS.OR7b087j3M.Cyr/s40xeq', 'ROLE_COORDINATOR') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (972, 'f.jaworski@gmail.com', '$2a$10$pQtFDNWDoD8iO.T4sMSTJOylz4P5cpXDPq1J.QQJEpZjPssj8SES.', 'ROLE_COORDINATOR') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (973, 'a.wroblewski@gmail.com', '$2a$10$aUv3u2F11S1ox03RkJlDveRRK31Kk7P6AA8b5yATJ2WrNo5trqRzG', 'ROLE_COORDINATOR') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (974, 'l.kaczmarczyk@gmail.com', '$2a$10$9bK1WRQMD0xUN4DVCuOn3O1tOcoz4dqbgmT60296fM3sLbRuZKUBa', 'ROLE_COORDINATOR') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2206, 'b.szcze@yahoo.com', '$2a$10$Q1eq5T8HGgFBSBTUqdgAwOD/fzRsLauUeqxot7u85R1lehrxqK4p2', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2216, 'a.sobczak@yahoo.com', '$2a$10$9NsTGAs9bOvvSoGYSaKFcO.GX9jlEJI2T9fMMZDpwVObfx0q7/LWC', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2241, 'e.dabrowski@yahoo.com', '$2a$10$PIKRGentZNNDCNFwu4t9LuTACT7wqlzSlDLC4xImBWHcc4LzjdSda', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2246, 'j.michalak@yahoo.com', '$2a$10$G/pt5h4cbf6U2dMt5/unqudbvRP4yADy8y1cwyI/iNERrltPeEDxa', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2251, 'd.baran@yahoo.com', '$2a$10$e.sTj5GWRcGldO2HHNJ76um17.s.QlVLprLgt9JX3.o1IZXqvO1pW', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2276, 'i.glowacka@yahoo.com', '$2a$10$lhHzbIiWoVQxraSmmnxt7uL1hP/1Y9FX1fq7jSSfD/y8p4XWSzmlG', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2296, 'l.jakubowski@yahoo.com', '$2a$10$Fap./ySQrwOqjSbb/6D0tO9AZymEpA9A1QWijqU6ybP6oAiWtlp62', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2301, 'd.maciejewski1@yahoo.com', '$2a$10$hQQPV.BfG0x56hUCFESTme0u3emeKsQxQbBGQfuZ5MjR/jmBY9YQW', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2331, 'j.krajewska1@yahoo.com', '$2a$10$ufKqjWl2c3FRhvHIqM7pR.85KBIt7uSFaaOdhRSLMy02LMvLtHp8O', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2336, 'a.sawicki@yahoo.com', '$2a$10$2rWU4PlcjEvZkI0aMqc3zeK8Lj1yAsmAr25TiiiuZKpB472HeF3tu', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2306, 'k.zawadzki@yahoo.com', '$2a$10$PMfiNFXPHQSj8zdYouFIquDY3QJc3J2Ryq7I75Bwz8S9mL6Ru/y3O', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2321, 'a.bak@yahoo.com', '$2a$10$rj7E54Tjv9/pQn9AXenT2eA9LLtaMB3S78mukC4IQBwabHYiegkbq', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2236, 'a.chmielewski@yahoo.com', '$2a$10$cJl./b09wG0kl2JpRFAsoOdNelYscblex80.iqWg3.aJ5GsywMUJG', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2311, 'k.zalewski@yahoo.com', '$2a$10$MVTkiM40KOgC8qt1wZTFW.F4scAS9D9dlD2kdSDh6txTKlDZnT3PC', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2231, 'r.michalak@yahoo.com', '$2a$10$wotxu1r63F0XzqiYA1ZDjOzKbw05VbTXlOPzA5Ny3bqcriLrJBioq', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2226, 'j.krajewska@yahoo.com', '$2a$10$sms.1wrFhvQgdDfRqjVY6.xO07hF4YsSnv.VHrQiUo/4jpk1HnPM2', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2221, 'a.szymczak@yahoo.com', '$2a$10$VqmZZf6caogCF8RHw3.8CujrOtAePdEsoryOqmk6pk3h9nzTgSQe6', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2346, 'c.sokolowski@yahoo.com', '$2a$10$zlBeNup4c5n4ZJndoBgZd.aRkhFg87vNzHFMU4X9GlsBzljqfddGG', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2341, 'd.adamska@yahoo.com', '$2a$10$KatKOeTRMwW6f19S3uH8veus4Q.NwYu07zXL4Y/LVmTy3YiunQ4Qq', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2326, 'a.piotrowski@yahoo.com', '$2a$10$LJ.IQD30syBwXU8wIgs4EuL5g83ZTpg88bpOUJYtMmPgiruCQzdn2', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2351, 'k.czerwinski@yahoo.com', '$2a$10$.3c8HWD2Q7Wu/rFb41/FueaVoEmJwfGP88hKrw0nTSMh9Dcb7M1pC', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2211, 'j.maciejewski@yahoo.com', '$2a$10$mjb0ye4V.O/cWRW3qYHEz.ac.b.6Mzqrl7gRLk1la97D4lrEr9DoC', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2256, 'm.jakubowski@yahoo.com', '$2a$10$VEwa.wZKiQsmLHPpTSrruuvRIASdKVR0LaOBe2zqI3CrqQUWmG5Bq', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2261, 'd.maciejewski@yahoo.com', '$2a$10$BS6uBPVASAchfGKIHqVeH.a7rxiZnOGiFOnOHODaSwkinVkQL5u4e', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2271, 'd.kaminski@yahoo.com', '$2a$10$S2Hi5ies5pKJXCSNPdEpx.iN4bBwTNnLNBfNyHRZoqwz81oATE9Ga', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2281, 'f.mroz@yahoo.com', '$2a$10$Hqd1wIFLKwDJfA1kie5tGO7VyXTXLlTlKWjj55GBr7weLQz8YHulm', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2286, 'a.brzezinski@yahoo.com', '$2a$10$7iqfreN6CatbD2EUu2RI7OG0tv/EIltwPIRV37w8rvvwj8KE6Q9zq', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2291, 'e.nowak@yahoo.com', '$2a$10$JOxsPJTvwhZ6hS9UNdiCY.76GvviokrXaFvKwgeRMQSYmEUJK7Hai', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2266, 'k.jakubowski@yahoo.com', '$2a$10$/1VvFLf4/n8CCZJBlkppXeioisDsp.Hx0dLmDkkP3WjnVy0Trjt.6', 'ROLE_STUDENT') on conflict (user_id) do nothing;
INSERT INTO users (user_id, email, password, role) VALUES (2316, 'k.nowak@yahoo.com', '$2a$10$np2.lwbzNNZXmyrxrhGruu8X2e3DGnsiFC.nb4aA4f5cs4aQM96ri', 'ROLE_STUDENT') on conflict (user_id) do nothing;

INSERT INTO coordinators (user_id, first_name, last_name, telephone_number, field_of_study, position) VALUES (969, 'Ariel', 'Witkowski', '687556432', 'INFORMATYKA', 'pracownik') on conflict (user_id) do nothing;
INSERT INTO coordinators (user_id, first_name, last_name, telephone_number, field_of_study, position) VALUES (970, 'Aleks', 'Szewczyk', '227278545', 'HUMANISTYKA', 'pracownik') on conflict (user_id) do nothing;
INSERT INTO coordinators (user_id, first_name, last_name, telephone_number, field_of_study, position) VALUES (971, 'Maciej', 'Kubiak', '302442829', 'EKONOMIA', 'pracownik') on conflict (user_id) do nothing;
INSERT INTO coordinators (user_id, first_name, last_name, telephone_number, field_of_study, position) VALUES (972, 'Fryderyk', 'Jaworski', '222915240', 'BIOLOGIA', 'pracownik') on conflict (user_id) do nothing;
INSERT INTO coordinators (user_id, first_name, last_name, telephone_number, field_of_study, position) VALUES (973, 'Arkadiusz', 'Wróblewski', '621989477', 'CHEMIA', 'pracownik') on conflict (user_id) do nothing;
INSERT INTO coordinators (user_id, first_name, last_name, telephone_number, field_of_study, position) VALUES (974, 'Leonardo', 'Kaczmarczyk', '950057941', 'FIZYKA', 'pracownik') on conflict (user_id) do nothing;

INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2207, 'Warszawa', 'Polna', '87', '23543', 'Warszawa', '5') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2212, 'Łomża', 'Leśna', '28', '23543', 'Łomża', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2217, 'Białystok', 'Długa', '27', '23543', 'Białystok', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2222, 'Kraków', 'Słoneczna', '80', '23543', 'Kraków', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2227, 'Warszawa', 'Krótka', '4', '23543', 'Warszawa', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2232, 'Łomża', 'Szkolna', '56', '23543', 'Łomża', '11') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2237, 'Białystok', 'Łąkowa', '26', '23543', 'Białystok', '9') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2242, 'Kraków', 'Sosnowa', '2', '23543', 'Kraków', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2247, 'Warszawa', 'Kolejowa', '77', '23543', 'Warszawa', '3') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2252, 'Łomża', 'Akacjowa', '44', '23543', 'Łomża', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2257, 'Białystok', 'Parkowa', '99', '23543', 'Białystok', '7') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2262, 'Kraków', 'Długa', '100', '23543', 'Kraków', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2267, 'Warszawa', 'Lipowa', '91', '23543', 'Warszawa', '23') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2272, 'Łomża', 'Lipowa', '63', '23543', 'Łomża', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2277, 'Białystok', 'Kościelna', '79', '23543', 'Białystok', '10') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2282, 'Kraków', 'Długa', '84', '23543', 'Kraków', '14') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2287, 'Łomża', 'Kościelna', '95', '23543', 'Łomża', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2292, 'Białystok', 'Parkowa', '1', '23543', 'Białystok', '15') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2297, 'Kraków', 'Szkolna', '19', '23543', 'Kraków', '11') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2302, 'Warszawa', 'Kościelna', '29', '23543', 'Warszawa', '21') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2307, 'Łomża', 'Lipowa', '7', '23543', 'Łomża', '9') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2312, 'Białystok', 'Parkowa', '30', '23543', 'Białystok', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2317, 'Kraków', 'Szkolna', '55', '23543', 'Kraków', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2322, 'Warszawa', 'Zielona', '21', '23543', 'Warszawa', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2327, 'Łomża', 'Szkolna', '69', '23543', 'Łomża', '8') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2332, 'Białystok', 'Zielona', '74', '23543', 'Białystok', '6') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2337, 'Kraków', 'Kościelna', '39', '23543', 'Kraków', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2342, 'Warszawa', 'Lipowa', '110', '23543', 'Warszawa', '4') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2347, 'Łomża', 'Zielona', '42', '23543', 'Łomża', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2352, 'Białystok', 'Parkowa', '90', '23543', 'Białystok', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2513, 'Poznań', 'Łąkowa', '11', '13800', 'Poznań', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2521, 'Białystok', 'Krótka', '2', '15000', 'Białystok', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2528, 'Warszawa', 'Brzozowa', '7', '16800', 'Warszawa', '2') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2533, 'Poznań', 'Szkolna', '81', '13400', 'Poznań', '5') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2541, 'Warszawa', 'Akacjowa', '101', '16800', 'Warszawa', '8') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2546, 'Białystok', 'Łąkowa', '99', '15500', 'Białystok', '6') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2554, 'Białystok', 'Ogrodowa', '5', '15800', 'Białystok', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2558, 'Poznań', 'Akacjowa', '21', '13400', 'Poznań', '7') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2562, 'Warszawa', 'Kościelna', '16', '16800', 'Warszawa', '4') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2568, 'Białystok', 'Kolejowa', '70', '15800', 'Białystok', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2573, 'Poznań', 'Słoneczna', '25', '13800', 'Poznań', '') on conflict (address_id) do nothing;
INSERT INTO addresses (address_id, city, street, building_number, zip_code, post_office, apartment_number) VALUES (2578, 'Warszawa', 'Polna', '1', '16800', 'Warszawa', '2') on conflict (address_id) do nothing;

INSERT INTO companies (company_id, representative_id, address_id, free_spaces, is_part_of_internship, company_name, starting_date, ending_date, field_of_study) VALUES (2512, 2514, 2513, 2, true, 'IBM', '2021-07-01', '2021-08-01', 'INFORMATYKA') on conflict (company_id) do nothing;
INSERT INTO companies (company_id, representative_id, address_id, free_spaces, is_part_of_internship, company_name, starting_date, ending_date, field_of_study) VALUES (2520, 2522, 2521, 3, true, 'BP', '2021-07-06', '2021-08-06', 'HUMANISTYKA') on conflict (company_id) do nothing;
INSERT INTO companies (company_id, representative_id, address_id, free_spaces, is_part_of_internship, company_name, starting_date, ending_date, field_of_study) VALUES (2532, 2534, 2533, 3, true, 'Vitol', '2021-07-21', '2021-08-21', 'BIOLOGIA') on conflict (company_id) do nothing;
INSERT INTO companies (company_id, representative_id, address_id, free_spaces, is_part_of_internship, company_name, starting_date, ending_date, field_of_study) VALUES (2540, 2542, 2541, 3, true, 'Samsung', '2021-07-29', '2021-08-29', 'CHEMIA') on conflict (company_id) do nothing;
INSERT INTO companies (company_id, representative_id, address_id, free_spaces, is_part_of_internship, company_name, starting_date, ending_date, field_of_study) VALUES (2545, 2547, 2546, 3, true, 'Chevron', '2021-07-30', '2021-08-30', 'FIZYKA') on conflict (company_id) do nothing;
INSERT INTO companies (company_id, representative_id, address_id, free_spaces, is_part_of_internship, company_name, starting_date, ending_date, field_of_study) VALUES (2553, 2555, 2554, 3, false, 'Allianz', '2021-07-01', '2021-07-21', 'INFORMATYKA') on conflict (company_id) do nothing;
INSERT INTO companies (company_id, representative_id, address_id, free_spaces, is_part_of_internship, company_name, starting_date, ending_date, field_of_study) VALUES (2557, 2559, 2558, 3, false, 'Axa', '2021-07-07', '2021-07-29', 'HUMANISTYKA') on conflict (company_id) do nothing;
INSERT INTO companies (company_id, representative_id, address_id, free_spaces, is_part_of_internship, company_name, starting_date, ending_date, field_of_study) VALUES (2561, 2563, 2562, 10, false, 'Eni', '2021-07-14', '2021-08-07', 'EKONOMIA') on conflict (company_id) do nothing;
INSERT INTO companies (company_id, representative_id, address_id, free_spaces, is_part_of_internship, company_name, starting_date, ending_date, field_of_study) VALUES (2567, 2569, 2568, 6, false, 'E.ON', '2021-07-21', '2021-08-14', 'BIOLOGIA') on conflict (company_id) do nothing;
INSERT INTO companies (company_id, representative_id, address_id, free_spaces, is_part_of_internship, company_name, starting_date, ending_date, field_of_study) VALUES (2572, 2574, 2573, 3, false, 'Cargill', '2021-07-29', '2021-08-21', 'CHEMIA') on conflict (company_id) do nothing;
INSERT INTO companies (company_id, representative_id, address_id, free_spaces, is_part_of_internship, company_name, starting_date, ending_date, field_of_study) VALUES (2577, 2579, 2578, 7, false, 'Verizon', '2021-08-01', '2021-08-21', 'FIZYKA') on conflict (company_id) do nothing;
INSERT INTO companies (company_id, representative_id, address_id, free_spaces, is_part_of_internship, company_name, starting_date, ending_date, field_of_study) VALUES (2527, 2529, 2528, 7, true, 'Sinopec', '2021-07-14', '2021-08-14', 'EKONOMIA') on conflict (company_id) do nothing;

INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2211, 'Julian', 'Maciejewski', '1614286192', 17388, 'INFORMATYKA', 'I', 5.00, 969, 2212, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2206, 'Bartosz', 'Szczepański', '3570923422', 56432, 'INFORMATYKA', 'I', 4.00, 969, 2207, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2216, 'Aleksander', 'Sobczak', '9577846769', 45227, 'INFORMATYKA', 'I', 4.50, 969, 2217, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2221, 'Arkadiusz', 'Szymczak', '2065678809', 49037, 'HUMANISTYKA', 'I', 5.00, 970, 2222, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2226, 'Jarosław', 'Krajewska', '8528323661', 47003, 'HUMANISTYKA', 'I', 5.00, 970, 2227, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2231, 'Roman', 'Michalak', '9334037699', 50387, 'HUMANISTYKA', 'I', 5.00, 970, 2232, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2236, 'Arkadiusz', 'Chmielewski', '8389072679', 68916, 'EKONOMIA', 'I', 5.00, 971, 2237, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2241, 'Edward', 'Dąbrowski', '4334839158', 77355, 'EKONOMIA', 'I', 4.50, 971, 2242, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2246, 'Jacek', 'Michalak', '2595615706', 34601, 'EKONOMIA', 'I', 4.67, 971, 2247, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2251, 'Diego', 'Baran', '4722302189', 66627, 'BIOLOGIA', 'I', 4.56, 972, 2252, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2256, 'Miron', 'Jakubowski', '7756003339', 26545, 'BIOLOGIA', 'I', 5.00, 972, 2257, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2261, 'Dorian', 'Maciejewski', '1096160057', 65513, 'BIOLOGIA', 'I', 5.00, 972, 2262, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2266, 'Krzysztof', 'Jakubowski', '9305977540', 62536, 'CHEMIA', 'I', 4.83, 973, 2267, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2271, 'Dariusz', 'Kamiński', '4282142676', 59772, 'CHEMIA', 'I', 5.00, 973, 2272, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2276, 'Ignacy', 'Głowacka', '2616004767', 51516, 'CHEMIA', 'I', 4.00, 973, 2277, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2281, 'Fryderyk', 'Mróz', '3537366797', 60677, 'FIZYKA', 'I', 5.00, 974, 2282, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2286, 'Anastazy', 'Brzeziński', '1808295303', 30135, 'FIZYKA', 'I', 5.00, 974, 2287, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2291, 'Ernest', 'Nowak', '1560929638', 60756, 'FIZYKA', 'I', 5.00, 974, 2292, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2296, 'Leszek', 'Jakubowski', '2649456854', 54403, 'HUMANISTYKA', 'I', 4.17, 970, 2297, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2301, 'Denis', 'Maciejewski', '1143768704', 60218, 'HUMANISTYKA', 'I', 4.39, 970, 2302, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2311, 'Krystian', 'Zalewski', '4893886494', 51918, 'EKONOMIA', 'I', 5.00, 971, 2312, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2316, 'Kuba', 'Nowak', '2516520737', 69686, 'BIOLOGIA', 'I', 4.67, 972, 2317, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2321, 'Alan', 'Bąk', '7525133327', 35866, 'BIOLOGIA', 'I', 5.00, 972, 2322, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2326, 'Antoni', 'Piotrowski', '5436461605', 82639, 'CHEMIA', 'I', 5.00, 973, 2327, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2331, 'Jędrzej', 'Krajewska', '3969389462', 29871, 'CHEMIA', 'I', 4.50, 973, 2332, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2336, 'Amir', 'Sawicki', '2734863633', 27704, 'FIZYKA', 'I', 4.50, 974, 2337, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2341, 'Diego', 'Adamska', '7970348446', 14120, 'FIZYKA', 'I', 5.00, 974, 2342, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2346, 'Cezary', 'Sokołowski', '6176281871', 31309, 'INFORMATYKA', 'I', 5.00, 969, 2347, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2351, 'Klaudiusz', 'Czerwiński', '9945119501', 18172, 'INFORMATYKA', 'I', 5.00, 969, 2352, null) on conflict (user_id) do nothing;
INSERT INTO students (user_id, first_name, last_name, telephone_number, student_index, field_of_study, degree, average_grade, coordinator_user_id, address_id, company_id) VALUES (2306, 'Konstanty', 'Zawadzki', '5618226314', 90881, 'EKONOMIA', 'I', 5.00, 971, 2307, 2527) on conflict (user_id) do nothing;

INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2206, 975, 3.5, 2208) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2206, 976, 4.5, 2209) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2206, 977, 4.0, 2210) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2211, 975, 4.5, 2213) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2211, 976, 4.5, 2214) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2211, 977, 4.5, 2215) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2216, 975, 2.0, 2218) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2216, 976, 2.0, 2219) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2216, 977, 4.5, 2220) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2221, 978, 4.5, 2223) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2221, 979, 2.0, 2224) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2221, 980, 4.0, 2225) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2226, 978, 4.0, 2228) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2226, 979, 2.0, 2229) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2226, 980, 4.0, 2230) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2231, 978, 4.5, 2233) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2231, 979, 3.0, 2234) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2231, 980, 4.0, 2235) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2236, 981, 3.5, 2238) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2236, 982, 4.5, 2239) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2236, 983, 2.0, 2240) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2241, 981, 3.0, 2243) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2241, 982, 2.0, 2244) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2241, 983, 3.5, 2245) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2246, 981, 3.5, 2248) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2246, 982, 3.0, 2249) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2246, 983, 3.0, 2250) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2251, 984, 3.5, 2253) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2251, 985, 3.5, 2254) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2251, 986, 2.0, 2255) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2256, 984, 3.5, 2258) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2256, 985, 4.5, 2259) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2256, 986, 3.5, 2260) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2261, 984, 3.0, 2263) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2261, 985, 3.5, 2264) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2261, 986, 3.5, 2265) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2266, 987, 2.0, 2268) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2266, 988, 4.5, 2269) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2266, 989, 3.0, 2270) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2271, 987, 3.5, 2273) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2271, 988, 3.5, 2274) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2271, 989, 3.5, 2275) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2276, 987, 2.0, 2278) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2276, 988, 3.0, 2279) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2276, 989, 2.0, 2280) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2281, 990, 4.5, 2283) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2281, 991, 4.0, 2284) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2281, 992, 4.0, 2285) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2286, 990, 4.5, 2288) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2286, 991, 3.5, 2289) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2286, 992, 4.0, 2290) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2291, 990, 3.0, 2293) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2291, 991, 4.0, 2294) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2291, 992, 4.5, 2295) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2296, 978, 3.5, 2298) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2296, 979, 2.0, 2299) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2296, 980, 2.0, 2300) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2301, 978, 3.0, 2303) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2301, 979, 2.0, 2304) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2301, 980, 4.0, 2305) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2306, 981, 4.0, 2308) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2306, 982, 4.5, 2309) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2306, 983, 4.5, 2310) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2311, 981, 4.5, 2313) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2311, 982, 3.0, 2314) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2311, 983, 3.5, 2315) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2316, 984, 3.5, 2318) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2316, 985, 2.0, 2319) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2316, 986, 3.5, 2320) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2321, 984, 3.5, 2323) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2321, 985, 4.0, 2324) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2321, 986, 4.5, 2325) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2326, 987, 4.5, 2328) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2326, 988, 3.5, 2329) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2326, 989, 2.0, 2330) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2331, 987, 2.0, 2333) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2331, 988, 3.5, 2334) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2331, 989, 3.0, 2335) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2336, 990, 2.0, 2338) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2336, 991, 3.0, 2339) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2336, 992, 4.0, 2340) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2341, 990, 4.0, 2343) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2341, 991, 4.5, 2344) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2341, 992, 3.5, 2345) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2346, 975, 4.0, 2348) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2346, 976, 3.5, 2349) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2346, 977, 3.5, 2350) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2351, 975, 4.0, 2353) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2351, 976, 3.0, 2354) on conflict (grade_id) do nothing;
INSERT INTO grades (student_id, subject_id, grade, grade_id) VALUES (2351, 977, 4.0, 2355) on conflict (grade_id) do nothing;

INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2515, 'Kacper', 'Andrzejewski', '596843209', 'k.and@yahoo.mail', 'pracownik', 2512) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2516, 'Denis', 'Lis', '778107281', 'd.lis@yahoo.mail', 'pracownik', 2512) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2523, 'Ariel', 'Sikora', '399330581', 'a.sik@yahoo.mail', 'pracownik', 2520) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2524, 'Kacper', 'Błaszczyk', '468290996', 'k.blasz@yahoo.mail', 'pracownik', 2520) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2530, 'Korneliusz', 'Baran', '627359854', 'k.bar@yahoo.mail', 'pracownik', 2527) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2535, 'Juliusz', 'Kaźmierczak', '917900926', 'j.kaz@yahoo.mail', 'pracownik', 2532) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2536, 'Jan', 'Sikorska', '700754846', 'j.sik@yahoo.mail', 'pracownik', 2532) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2543, 'Mariusz', 'Baranowski', '878007301', 'm.bar@yahoo.mail', 'pracownik', 2540) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2548, 'Konrad', 'Bąk', '525960787', 'k.bak@yahoo.mail', 'pracownik', 2545) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2549, 'Daniel', 'Przybylski', '701930447', 'd.przb@yahoo.mail', 'pracownik', 2545) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2550, 'Maurycy', 'Wróblewski', '118109740', 'm.wrob@yahoo.mail', 'pracownik', 2545) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2556, 'Karol', 'Michalak', '924740804', 'k.mich@yahoo.mail', 'pracownik', 2553) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2560, 'Marek', 'Ostrowski', '330712023', 'k.ostr@yahoo.mail', 'pracownik', 2557) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2564, 'Kacper', 'Włodarczyk', '756765917', 'k.wlo@yahoo.mail', 'pracownik', 2561) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2565, 'Allan', 'Sikora', '303035367', 'a.sikr@yahoo.mail', 'pracownik', 2561) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2566, 'Eryk', 'Sikorska', '756765917', 'e.sikora@yahoo.mail', 'pracownik', 2561) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2570, 'Bogumił', 'Szewczyk', '131000682', 'k.szew@yahoo.mail', 'pracownik', 2567) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2571, 'Heronim', 'Chmielewski', '964434480', 'h.cmil@yahoo.mail', 'pracownik', 2567) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2575, 'Robert', 'Zakrzewska', '472515472', 'k.zakrz@yahoo.mail', 'pracownik', 2572) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2576, 'Aleksander', 'Wójcik', '542699638', 'a.woj@yahoo.mail', 'pracownik', 2572) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2580, 'Alek', 'Wasilewska', '638194462', 'k.wasil@yahoo.mail', 'pracownik', 2577) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2581, 'Amir', 'Mazurek', '365088485', 'a.maz@yahoo.mail', 'pracownik', 2577) on conflict (guardian_id) do nothing;
INSERT INTO guardians (guardian_id, first_name, last_name, telephone_number, email, position, company_id) VALUES (2582, 'Bartłomiej', 'Urbańska', '496376941', 'b.urb@yahoo.mail', 'pracownik', 2577) on conflict (guardian_id) do nothing;

INSERT INTO public.internship_plan (internship_plan_id, company_id, description) VALUES (2517, 2512, 'Dziś masz zrobić to...') on conflict (internship_plan_id) do nothing;
INSERT INTO public.internship_plan (internship_plan_id, company_id, description) VALUES (2518, 2512, 'Dziś masz zrobić to...') on conflict (internship_plan_id) do nothing;
INSERT INTO public.internship_plan (internship_plan_id, company_id, description) VALUES (2519, 2512, 'Dziś masz zrobić to...') on conflict (internship_plan_id) do nothing;
INSERT INTO public.internship_plan (internship_plan_id, company_id, description) VALUES (2525, 2520, 'Dziś masz zrobić to...') on conflict (internship_plan_id) do nothing;
INSERT INTO public.internship_plan (internship_plan_id, company_id, description) VALUES (2526, 2520, 'Dziś masz zrobić to...') on conflict (internship_plan_id) do nothing;
INSERT INTO public.internship_plan (internship_plan_id, company_id, description) VALUES (2531, 2527, 'Dziś masz zrobić to...') on conflict (internship_plan_id) do nothing;
INSERT INTO public.internship_plan (internship_plan_id, company_id, description) VALUES (2537, 2532, 'Dziś masz zrobić to...') on conflict (internship_plan_id) do nothing;
INSERT INTO public.internship_plan (internship_plan_id, company_id, description) VALUES (2538, 2532, 'Dziś masz zrobić to...') on conflict (internship_plan_id) do nothing;
INSERT INTO public.internship_plan (internship_plan_id, company_id, description) VALUES (2539, 2532, 'Dziś masz zrobić to...') on conflict (internship_plan_id) do nothing;
INSERT INTO public.internship_plan (internship_plan_id, company_id, description) VALUES (2544, 2540, 'Dziś masz zrobić to...') on conflict (internship_plan_id) do nothing;
INSERT INTO public.internship_plan (internship_plan_id, company_id, description) VALUES (2551, 2545, 'Dziś masz zrobić to...') on conflict (internship_plan_id) do nothing;
INSERT INTO public.internship_plan (internship_plan_id, company_id, description) VALUES (2552, 2545, 'Dziś masz zrobić to...') on conflict (internship_plan_id) do nothing;