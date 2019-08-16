# install nginx admin plus

## download install script
```
cd /tmp
wget -O install-nginx-admin-agent.sh https://github.com/alibaba/nginx-admin-plus/blob/master/install/install-nginx-admin-agent.sh
wget -O install-nginx-admin-ui.sh https://github.com/alibaba/nginx-admin-plus/blob/master/install/install-nginx-admin-ui.sh
```

## may need line breaker convert
```
dos2unix install-nginx-admin-*.sh
chmod a+x install-nginx-admin-*.sh
```

## execute install script
```
sudo ./install-nginx-admin-agent.sh
sudo ./install-nginx-admin-ui.sh
```
