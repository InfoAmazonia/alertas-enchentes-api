drop table alert if exists;
drop table station if exists;

create table alert (
	station_id bigint not null, 
	timestamp bigint not null, 
	calculated bigint, 
	measured bigint,
	predicted bigint, 
	primary key (station_id, timestamp)
);

create table summary (
	station_id bigint not null, 
	timestamp varchar(255) not null, 
	measured bigint,
	primary key (station_id ASC, timestamp ASC)
);

create table station (
	id bigint not null, 
	flood_threshold bigint, 
	lst_station bigint, 
	name varchar(255), 
	oldest_measure_date varchar(255), 
	predict boolean, 
	bacia integer not null, 
	subbacia integer not null, 
	view_state varchar(10000), 
	warning_threshold bigint, 
	primary key (id)
);

alter table alert add constraint FKivldapnlyu3bxcj1t0263jvro foreign key (station_id) references station;
