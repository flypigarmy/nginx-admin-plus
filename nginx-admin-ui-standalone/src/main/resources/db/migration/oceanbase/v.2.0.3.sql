alter table nginx add  service_name VARCHAR(64) not null default 'nginx';
alter table nginx add  settings_path VARCHAR(256) not null default '/opt/nginx-admin-agent-2.0.3/settings/';
alter table nginx drop INDEX nginx_uk2;
alter table nginx add constraint nginx_uk_3 unique(endpoint, settings_path);
alter table nginx add constraint nginx_uk_4 unique(endpoint, service_name);

