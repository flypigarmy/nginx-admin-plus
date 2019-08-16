package com.jslsolucoes.nginx.admin.agent.model.request.upstream;

import com.jslsolucoes.nginx.admin.agent.model.Endpoint;

import java.util.List;

public class NginxUpstreamUpdateRequest {

	private String         name;
	private String         additionalLines;
	private String         strategy;
	private List<Endpoint> endpoints;

	public NginxUpstreamUpdateRequest() {

	}

	public NginxUpstreamUpdateRequest(String name, String additionalLines, String strategy, List<Endpoint> endpoints) {
		this.name = name;
		this.additionalLines = additionalLines;
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
