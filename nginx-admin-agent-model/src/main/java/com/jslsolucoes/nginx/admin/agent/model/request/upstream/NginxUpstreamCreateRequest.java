package com.jslsolucoes.nginx.admin.agent.model.request.upstream;

import com.jslsolucoes.nginx.admin.agent.model.Endpoint;

import java.util.List;

public class NginxUpstreamCreateRequest {

	private String         name;
	private String         additionalLines;
	private String         uuid;
	private String         strategy;
	private List<Endpoint> endpoints;

	public NginxUpstreamCreateRequest() {

	}

	public NginxUpstreamCreateRequest(String name, String additionalLines, String uuid, String strategy,
									  List<Endpoint> endpoints) {
		this.name = name;
		this.additionalLines = additionalLines;
		this.uuid = uuid;
		this.strategy = strategy;
		this.endpoints = endpoints;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdditionalLines() {
		return additionalLines;
	}

	public void setAdditionalLines(String additionalLines) {
		this.additionalLines = additionalLines;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

}
