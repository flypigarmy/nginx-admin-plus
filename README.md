# nginx-admin plus

Nginx admin plus is an open source multiplatform manager for nginx software

original fork from https://github.com/jslsolucoes/nginx-admin

# user guide

The default user for nginx admin ui manager is:
Login :    admin
Password : admin

> For premium support or to commercial, please contact : TBD@somesite.com

---

# features plus

more configurations items support
- listen port for root server
- self defined listen port for HTTP/HTTPS
- transaction queue size and queue priority configuration for nginx plugn (TBD)
- ...

multiple nginx instances deploy support
- status check based on pid file instead of nginx process name

improvements
- auto work path file privilege setting
- ...

---

# build guide

> use mvn to build first

```
mvn clean install -DskipTests=true
```

build process will generate some source code via JAXB.

---

# debug guide

## h2 database

download h2 console
- https://www.h2database.com/html/download.html

connect url
```
jdbc:h2:tcp://localhost:9123/./opt/nginx-admin-2.0.3/database/nginx_admin
```
