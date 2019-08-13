package com.jslsolucoes.nginx.admin.agent.model.request.virtual.host;

import com.jslsolucoes.nginx.admin.agent.model.Location;

import java.util.List;

public class NginxVirtualHostCreateRequest {

	private String         uuid;
	private Boolean        https;
	private Integer        listenPort;
	private Long           queueSize;
	private String         certificateUuid;
	private String         certificatePrivateKeyUuid;
	private List<String>   aliases;
	private List<Location> locations;

	public NginxVirtualHostCreateRequest() {

	}

	public NginxVirtualHostCreateRequest(String uuid, Boolean https, Integer listenPort, Long queueSize,
										 String certificateUuid,
										 String certificatePrivateKeyUuid, List<String> aliases,
										 List<Location> locations) {
		this.uuid = uuid;
		this.https = https;
		this.listenPort = listenPort;
		this.queueSize = queueSize;
		this.certificateUuid = certificateUuid;
		this.certificatePrivateKeyUuid = certificatePrivateKeyUuid;
		this.aliases = aliases;
		this.locations = locations;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Boolean getHttps() {
		return https;
	}

	public Integer getListenPort() {
		return listenPort;
	}

	public void setListenPort(Integer listenPort) {
		this.listenPort = listenPort;
	}

	public Long getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(Long queueSize) {
		this.queueSize = queueSize;
	}

	public void setHttps(Boolean https) {
		this.https = https;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public String getCertificateUuid() {
		return certificateUuid;
	}

	public void setCertificateUuid(String certificateUuid) {
		this.certificateUuid = certificateUuid;
	}

	public String getCertificatePrivateKeyUuid() {
		return certificatePrivateKeyUuid;
	}

	public void setCertificatePrivateKeyUuid(String certificatePrivateKeyUuid) {
		this.certificatePrivateKeyUuid = certificatePrivateKeyUuid;
	}

}
