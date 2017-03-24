alter table station_entry modify column measured int;
alter table station_entry drop column calculated;
alter table station_entry modify column predicted int;

alter table summary modify column measured int;

alter table station modify column flood_threshold int;
alter table station modify column attention_threshold int;
alter table station modify column warning_threshold int; 
alter table station add column lst_origem bigint; 
