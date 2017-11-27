create table person
(
  id bigint(11) not null
    primary key,
  firstName varchar(256) null,
  lastName varchar(256) null,
  orgId bigint(11) null,
  resume varchar(256) null,
  salary double null,
  constraint id_UNIQUE
    unique (id)
);

create index ORGID
  on person (orgId);

create index RESUME
  on person (resume);

create index SALARY
  on person (salary);

create index firstname_index
  on person (firstName);