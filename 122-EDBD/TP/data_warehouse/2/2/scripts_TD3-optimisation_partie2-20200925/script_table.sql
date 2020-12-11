drop table ville;
drop table departement;
drop table region;


create table ville
(
	insee varchar2(5) NOT NULL,
	nom varchar2(100),
	dep varchar(5)
);

create table region
(
	id integer primary key,
	nom varchar2(50)
);

create table departement
(
	id varchar2(5) primary key,
	nom varchar2(50),
	reg integer
);

alter table departement add foreign key(reg) references region(id);
alter table ville add foreign key(dep) references departement(id);
