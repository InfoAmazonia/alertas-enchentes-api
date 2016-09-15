drop table alert if exists;
drop table station if exists;

create table alert (
	station_id bigint not null, 
	timestamp bigint not null, 
	calculated bigint, 
	measured bigint,
	measured_status varchar(255), 
	predicted bigint, 
	predicted_status varchar(255), 
	primary key (station_id, timestamp)
);

create table station (
	id bigint not null, 
	flood_threshold bigint, 
	lst_station bigint, 
	name varchar(255), 
	oldest_measure_date varchar(255), 
	predict boolean, 
	subbacia integer not null, 
	view_state varchar(5000), 
	warning_threshold bigint, 
	primary key (id)
);

alter table alert add constraint FKivldapnlyu3bxcj1t0263jvro foreign key (station_id) references station;
