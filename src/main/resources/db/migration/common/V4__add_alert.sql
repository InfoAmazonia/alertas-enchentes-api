create table alert (
	station_id bigint not null, 
	timestamp bigint not null, 
	message varchar(255), 
	primary key (station_id, timestamp)
);
