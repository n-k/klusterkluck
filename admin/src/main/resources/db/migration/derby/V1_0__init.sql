create table kf_connectors (
    id bigint generated by default as identity,
    display_name varchar(255),
    exposed boolean not null,
    image varchar(255),
    port integer not null,
    primary key (id)
);

create table kf_flows (
    id bigint generated by default as identity,
    contents clob(1048576),
    display_name varchar(255),
    name varchar(255),
    owner_id bigint not null,
    primary key (id)
);

create table kf_functions (
    id bigint generated by default as identity,
    commit_id varchar(255),
    deployment varchar(255),
    git_url varchar(255),
    ingress_url varchar(255),
    name varchar(255),
    namespace varchar(255),
    service varchar(255),
    type integer,
    owner_id bigint not null,
    primary key (id)
);

create table user_namespaces (
    id bigint generated by default as identity,
    display_name varchar(255),
    git_password varchar(255),
    git_user varchar(255),
    name varchar(255),
    primary key (id)
);

create table users (
    email varchar(255) not null,
    iam_id varchar(255),
    primary key (email)
);

create table users_namespaces (
    user_email varchar(255) not null,
    namespaces_id bigint not null
);

alter table users_namespaces
    add constraint UK_sxnphb9ehr1h928d1f1h5ohbm  unique (namespaces_id);

alter table kf_flows
    add constraint FK_8cghs8sy4xji0qdurj8xxoh2j
    foreign key (owner_id)
    references user_namespaces;

alter table kf_functions
    add constraint FK_hl7ja3hd326toysi5f9cof52v
    foreign key (owner_id)
    references user_namespaces;

alter table users_namespaces
    add constraint FK_sxnphb9ehr1h928d1f1h5ohbm
    foreign key (namespaces_id)
    references user_namespaces;

alter table users_namespaces
    add constraint FK_m2odwv8w34lo6jmoahp8oi11u
    foreign key (user_email)
    references users;
