package com.jslsolucoes.nginx.admin.agent.client.api.impl.configure;

import com.jslsolucoes.nginx.admin.agent.client.api.NginxAgentClientApiBuilder;
import com.jslsolucoes.nginx.admin.agent.client.api.impl.DefaultNginxAgentClientApi;

import java.util.concurrent.ScheduledExecutorService;

public class NginxConfigureBuilder extends DefaultNginxAgentClientApi implements NginxAgentClientApiBuilder {

	private ScheduledExecutorService scheduledExecutorService;
	private String                   endpoint;
	private String                   authorizationKey;
	private Integer                  maxPostSize;
	private Integer                  rootPort;
	private Boolean                  gzip;

	private NginxConfigureBuilder() {

	}

	@Override
	public NginxConfigure build() {
		return new NginxConfigure(scheduledExecutorService, authorizationKey, endpoint, maxPostSize, rootPort, gzip);
	}

	public static NginxConfigureBuilder newBuilder() {
		return new NginxConfigureBuilder();
	}

	public NginxConfigureBuilder withScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
		this.scheduledExecutorService = scheduledExecutorService;
		return this;
	}

	public NginxConfigureBuilder withEndpoint(String endpoint) {
		this.endpoint = endpoint;
		return this;
	}

	public NginxConfigureBuilder withMaxPostSize(Integer maxPostSize) {
		this.maxPostSize = maxPostSize;
		return this;
	}

	public NginxConfigureBuilder withRootPort(Integer rootPort) {
		this.rootPort = rootPort;
		return this;
	}

	public NginxConfigureBuilder withGzip(Boolean gzip) {
		this.gzip = gzip;
		return this;
	}

	public NginxConfigureBuilder withAuthorizationKey(String authorizationKey) {
		this.authorizationKey = authorizationKey;
		return this;
	}

}
