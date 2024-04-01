create table ineedit."user"
(
    firstname  varchar(50),
    lastname   varchar(50),
    username   varchar(20),
    email      varchar(50),
    created_at timestamp not null
);

alter table ineedit."user"
    owner to shlomitbitton;

