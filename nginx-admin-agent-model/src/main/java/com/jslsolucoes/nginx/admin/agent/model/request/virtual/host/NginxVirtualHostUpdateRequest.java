package com.jslsolucoes.nginx.admin.agent.model.request.virtual.host;

import com.jslsolucoes.nginx.admin.agent.model.Location;

import java.util.List;

public class NginxVirtualHostUpdateRequest {

	private Boolean        https;
	private Long           queueSize;
	private String         certificateUuid;
	private String         certificatePrivateKeyUuid;
	private List<String>   aliases;
	private List<Location> locations;

	public NginxVirtualHostUpdateRequest() {

	}

	public NginxVirtualHostUpdateRequest(Boolean https, Long queueSize, String certificateUuid,
										 String certificatePrivateKeyUuid,
										 List<String> aliases, List<Location> locations) {
		this.https = https;
		this.queueSize = queueSize;
		this.certificateUuid = certificateUuid;
		this.certificatePrivateKeyUuid = certificatePrivateKeyUuid;
		this.aliases = aliases;
		this.locations = locations;
	}

	public Boolean getHttps() {
		return https;
	}

	public void setHttps(Boolean https) {
		this.https = https;
	}

	public Long getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(Long queueSize) {
		this.queueSize = queueSize;
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
