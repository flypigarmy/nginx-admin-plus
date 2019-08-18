
# nginx admin plus manual

## prerequisite

- OS: redhat/centos
- software: nginx/openresty

## configuration

### nginx admin ui

- `/opt/nginx-admin-2.0.3/conf/nginx-admin.conf`

```
JAVA=/usr/bin/java

NGINX_ADMIN_BIN=$NGINX_ADMIN_HOME/bin
NGINX_ADMIN_LOG=$NGINX_ADMIN_HOME/log
NGINX_ADMIN_CONF=$NGINX_ADMIN_HOME/conf
NGINX_ADMIN_HTTP_PORT=4000
NGINX_ADMIN_HTTPS_PORT=4443
NGINX_ADMIN_URL_BASE=http://localhost:4000

# change db configuration below
NGINX_ADMIN_DB_DRIVER=h2
NGINX_ADMIN_DB_LOCATION=$NGINX_ADMIN_HOME/database
NGINX_ADMIN_DB_HOST=localhost
NGINX_ADMIN_DB_PORT=9123
NGINX_ADMIN_DB_NAME=nginx_admin
NGINX_ADMIN_DB_USERNAME=nginx_admin
NGINX_ADMIN_DB_PASSWORD=changeit
NGINX_ADMIN_DB_POOL_INITIAL=1
NGINX_ADMIN_DB_POOL_MIN=1
NGINX_ADMIN_DB_POOL_MAX=2
```


### nginx admin agent

- `/opt/nginx-admin-agent-2.0.3/conf/nginx-admin-agent.conf`

```
JAVA=/usr/bin/java
NGINX_BIN=/usr/sbin/nginx

# change nginx setting path below
NGINX_ADMIN_AGENT_SETTINGS=$NGINX_ADMIN_AGENT_HOME/settings
NGINX_ADMIN_AGENT_USER=nginx-admin-agent
NGINX_ADMIN_AGENT_HTTP_PORT=3000
NGINX_ADMIN_AGENT_HTTPS_PORT=3443
NGINX_ADMIN_AGENT_AUTHORIZATION_KEY=changeit

NGINX_ADMIN_AGENT_URL_BASE=http://localhost:3000

# set 1 to enable remote debug
NGINX_ADMIN_AGENT_REMOTE_DEBUG=0
```

## operations

### login nginx admin ui

after nginx admin ui is installed, got to [http://localhost:4000](http://localhost:4000/)

> default port is 4000, refer configuration for check your port



login page


![login page](/images/login.png)

> default user/password is `admin`/`admin`, password change is required after first login.

### add nginx admin agent

after login, goto [Nginx nodes] for manage nginx admin agent nodes.

![add nginx node](/images/nginx-nodes.png)

add nginx admin agent

![nginx agent node configuration](/images/nginx-agent-config.png)

> agent authorization key  should match with `NGINX_ADMIN_AGENT_AUTHORIZATION_KEY` value set in agent configuration file

### select agent for manage

select agent for manage configurations

![select agent session](/images/select-agent-session.png)


### virtual host config

goto virtual host config
![goto virtual host config](/images/goto-virtual-host-config.png)

virtual host config
![virtual host configuration](/images/virtual-host-config.png)

> you may set transaction queue size/ set queue priority and handler for each location. additionalLines for self define configurations.

### upstream config

> for goto, same as virtual host config

upstream config

![upstream configuration](/images/upstream-config.png)

> you may forward servers in upstream, additionalLines for self define configurations also available here.

### dashboard

dashboard page show basic config and status of nginx, you can also manage nginx instance, below operation are supported
- test configuration
- start
- stop
- restart
- reload

dashboard page
![dashboard page](/images/dashboard-page.png)









