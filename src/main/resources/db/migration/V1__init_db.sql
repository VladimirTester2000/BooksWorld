create table IF NOT EXISTS comment (
                         comment_id int8 generated by default as identity,
                         filename varchar(255),
                         stars int2 not null,
                         text varchar(2048),
                         user_id int8 not null,
                         primary key (comment_id)
);
create table IF NOT EXISTS userdata (
                          user_id int8 generated by default as identity,
                          email varchar(255),
                          password varchar(100) not null,
                          state varchar(255) not null,
                          username varchar(50) not null,
                          primary key (user_id)
);

create table IF NOT EXISTS activation (
                            user_id int8 not null,
                            activation_code varchar(255) not null,
                            primary key (user_id)
);

create table IF NOT EXISTS user_role (
                           user_id int8 not null,
                           roles varchar(255) not null
);

alter table if exists user_role
    add constraint rolesForUserUnique
        unique (user_id, roles);

alter table if exists comment
    add constraint user_id_comment_FK
        foreign key (user_id) references userdata;

alter table if exists activation
    add constraint user_id_activation_FK
        foreign key (user_id) references userdata;

alter table if exists user_role
    add constraint user_id_role_FK
        foreign key (user_id) references userdata;

ALTER TABLE if exists comment
    ADD CONSTRAINT stars_value
        CHECK ( stars>=0 and stars <=5 );
ALTER TABLE comment ADD CHECK ( stars is not null or text is not null);

alter table if exists userdata
    add constraint username_unique
        unique (username);
