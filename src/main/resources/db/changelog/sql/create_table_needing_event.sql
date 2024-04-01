create table ineedit.needing_event
(
    itemname                varchar(50),
    needingeventdatecreated timestamp not null,
    dayslisted              bigint
);

alter table ineedit.needing_event
    owner to shlomitbitton;

