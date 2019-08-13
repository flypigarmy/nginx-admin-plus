
server {
	<#if virtualHost.https == 1>
		listen               ${ listenPort } ssl;
		ssl_certificate      /opt/nginx-admin/settings/ssl/${ virtualHost.sslCertificate.resourceIdentifierCertificate.hash };
		ssl_certificate_key  /opt/nginx-admin/settings/ssl/${ virtualHost.sslCertificate.resourceIdentifierCertificatePrivateKey.hash };
		ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
        ssl_ciphers "EECDH+AESGCM:EDH+AESGCM:ECDHE-RSA-AES128-GCM-SHA256:AES256+EECDH:DHE-RSA-AES128-GCM-SHA256:AES256+EDH:ECDHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:DHE-RSA-AES256-SHA256:DHE-RSA-AES128-SHA256:DHE-RSA-AES256-SHA:DHE-RSA-AES128-SHA:ECDHE-RSA-DES-CBC3-SHA:EDH-RSA-DES-CBC3-SHA:AES256-GCM-SHA384:AES128-GCM-SHA256:AES256-SHA256:AES128-SHA256:AES256-SHA:AES128-SHA:DES-CBC3-SHA:HIGH:!aNULL:!eNULL:!EXPORT:!DES:!MD5:!PSK:!RC4";
        ssl_prefer_server_ciphers on;
        ssl_session_cache shared:SSL:10m;
        ssl_dhparam  /opt/nginx-admin/settings/ssl/dhparam.pem;
	<#else>
	 	listen               ${ listenPort };
	</#if>
          
       	server_name <#list aliases as alias> ${ alias } </#list>;

        set_by_lua_file $queue_size ../lualib/ob/ob_queue_set.lua ${ queueSize };

    <#list locations as location>

    	location ${ location.path } {
            set $queue_priority ${ location.queuePriority };
            set $queue_handler ${ location.queueHandler };
            content_by_lua_file ../lualib/ob/ob_que_handle.lua;
      		proxy_pass  http://${ location.upstream };
    	}
    	
    </#list>
}
