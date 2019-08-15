package com.jslsolucoes.nginx.admin.agent.model.request;

public class NginxConfigureRequest {

	private Integer maxPostSize;
	private Integer rootPort;
	private Boolean gzip;

	public NginxConfigureRequest() {

	}

	public NginxConfigureRequest(Integer maxPostSize, Integer rootPort, Boolean gzip) {
		this.maxPostSize = maxPostSize;
		this.rootPort = rootPort;
		this.gzip = gzip;
	}

	public Integer getMaxPostSize() {
		return maxPostSize;
	}

	public void setMaxPostSize(Integer maxPostSize) {
		this.maxPostSize = maxPostSize;
	}

	public Integer getRootPort() {
		return rootPort;
	}

	public void setRootPort(Integer rootPort) {
		this.rootPort = rootPort;
	}

	public Boolean getGzip() {
		return gzip;
	}

	public void setGzip(Boolean gzip) {
		this.gzip = gzip;
	}

}
