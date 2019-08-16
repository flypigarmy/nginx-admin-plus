alter table nginx add  service_name VARCHAR(64) not null default 'nginx';
alter table nginx add  settings_path VARCHAR(256) not null default '/opt/nginx-admin-agent-2.0.3/settings/';
alter table nginx drop INDEX nginx_uk2;
alter table nginx add constraint nginx_uk_3 unique(endpoint, settings_path);
alter table nginx add constraint nginx_uk_4 unique(endpoint, service_name);

alter table virtual_host add  listen_port INT(5) not null default 8080;
alter table configuration add  root_port INT(5) not null default 80;

alter table upstream add additional_lines VARCHAR(1024) null default null;


