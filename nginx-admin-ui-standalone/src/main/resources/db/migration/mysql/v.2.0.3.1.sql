alter table upstream_server add additionalOption VARCHAR(1024) null default null;

alter table virtual_host_location modify  queue_priority INT(10) null default null;
alter table virtual_host_location modify  queue_handler VARCHAR(64) null default null;
alter table virtual_host modify  queue_size BIGINT(10) null default null;