alter table station_entry alter column measured int;
alter table station_entry drop column calculated;
alter table station_entry alter column predicted int;

alter table summary alter column measured int;

alter table station alter column flood_threshold int;
alter table station alter column attention_threshold int;
alter table station alter column warning_threshold int; 
