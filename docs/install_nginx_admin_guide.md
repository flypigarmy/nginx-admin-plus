## install nginx-admin

```
# download install script
cd /tmp
wget -O install-nginx-admin-agent.sh https://raw.githubusercontent.com/jslsolucoes/nginx-admin/master/nginx-admin-docker/release/nginx-admin-agent/red-hat/build/install.sh
wget -O install-nginx-admin-ui.sh https://raw.githubusercontent.com/jslsolucoes/nginx-admin/master/nginx-admin-docker/release/nginx-admin/red-hat/build/install.sh

# line breaker convert
dos2unix install-nginx-admin-*.sh
chmod a+x install-nginx-admin-*.sh

# execute install script
sudo ./install-nginx-admin-agent.sh
sudo ./install-nginx-admin-ui.sh
```
