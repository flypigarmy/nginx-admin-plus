# nginx-admin plus

Nginx admin plus is an open source multi-platform manager for nginx software,
original fork from [nginx-admin](https://github.com/jslsolucoes/nginx-admin).

## features plus

more configurations items support
- [x] listen port for root server
- [x] self defined listen port for HTTP/HTTPS
- [x] transaction queue size and queue priority configuration for nginx plugin (link TBD)
- [x] additionalLine for self define configuration in
    - location
    - upstream
- [x] ...

multiple nginx instances deploy support
- [x] status check based on pid file instead of nginx process name
- [ ] ...

improvements
- [x] auto work path file privilege setting
- [ ] ...

## deploy guide

nginx install references
- [install nginx in centos 6](docs/install_nginx_in_centos_6.md)
- [nginx configuration guide](docs/nginx_configuration_guide.md)

install nginx admin plus
- [nginx admin plus install guide](docs/install_nginx_admin_guide.md)

refer below scripts for understand nginx admin plus install
- `install/install-nginx-admin-agent.sh`
- `install/install-nginx-admin-ui.sh`

## build guide

> use mvn to build first

```
mvn clean install -DskipTests=true
```

build process will generate some source code via JAXB.

## user guide

- [nginx admin plus manual](docs/nginx-admin-plus-manual.md)

## debug guide

### h2 database

download h2 console
- https://www.h2database.com/html/download.html

connect url
```
jdbc:h2:tcp://localhost:9123/./opt/nginx-admin-2.0.3/database/nginx_admin
```
