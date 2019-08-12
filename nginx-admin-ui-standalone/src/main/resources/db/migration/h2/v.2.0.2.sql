alter table virtual_host_location add  queue_priority INT(10) not null default 0;
alter table virtual_host_location add  queue_handler VARCHAR(64) not null default 'ob.delivery';

alter table virtual_host add  queue_size BIGINT(10) not null default 10;
