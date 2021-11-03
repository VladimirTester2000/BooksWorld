create table author (
                        author_id int8 generated by default as identity,
                        name varchar(255) not null ,
                        birthday date not null,
                        date_of_death date,
                        primary key (author_id)
);
create table book (
                         book_id int8 generated by default as identity,
                         title varchar(255) not null,
                         description varchar(2048),
                         primary key (book_id)
);

create table author_book(
    author_id int8,
    book_id int8,
    primary key (author_id,book_id)
);

alter table if exists author_book
    add constraint author_book_author_id_FK
        foreign key (author_id) references author;
alter table if exists author_book
    add constraint author_book_book_id_FK
        foreign key (book_id) references book;

alter table if exists author
    add constraint author_unique
        unique (name,birthday);
alter table if exists author
    add constraint birthday_earlier_now
        check (birthday<now());
alter table if exists author
    add constraint date_of_death_earlier_now
        check (date_of_death<now());