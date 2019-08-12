## nginx configuration guide

### config test

use `-t` to test config
```
sudo nginx -t
```

sample output
```
$sudo nginx -t
nginx: the configuration file /etc/nginx/nginx.conf syntax is ok
nginx: configuration file /etc/nginx/nginx.conf test is successful
```

### config reload

```
sudo nginx -s reload
```

### status check

> for how to enable stub_status, refer https://www.tecmint.com/enable-nginx-status-page/

```
    server {
        listen 80;
        server_name __;
        location /status {
            stub_status on;
            allow 127.0.0.1;
            deny all;
        }
    }
```
