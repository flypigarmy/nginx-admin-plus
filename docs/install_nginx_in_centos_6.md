## install Nginx in CentOS 6

> refer from https://www.cyberciti.biz/faq/install-nginx-centos-rhel-6-server-rpm-using-yum-command/

### ~~install EPEL yum repo~~

> EPEL yum repo not works in company network

```
cd /tmp

wget https://dl.fedoraproject.org/pub/epel/epel-release-latest-6.noarch.rpm

sudo yum install ./epel-release-latest-*.noarch.rpm
```

### install nginx

install nginx rpm repo
```
cd /tmp
wget http://nginx.org/packages/centos/6/noarch/RPMS/nginx-release-centos-6-0.el6.ngx.noarch.rpm
rpm -ivh nginx-release-centos-6-0.el6.ngx.noarch.rpm

```

install nginx
```
yum install nginx
```

chkconfig
```
chkconfig nginx on
```

### start/run/status

```
service nginx status
service nginx stop
service nginx start
```



