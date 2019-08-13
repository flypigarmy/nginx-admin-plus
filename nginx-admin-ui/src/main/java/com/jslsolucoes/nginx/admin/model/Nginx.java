package com.jslsolucoes.nginx.admin.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@SuppressWarnings("serial")
@Entity
@Table(name = "nginx")
@SequenceGenerator(name = "nginx_sq", initialValue = 1, allocationSize = 1, sequenceName = "nginx_sq")
public class Nginx implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nginx_sq")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "endpoint")
	private String endpoint;

	@Column(name = "service_name")
	private String serviceName;

	@Column(name = "settings_path")
	private String settingsPath;

	@Column(name = "authorization_key")
	private String authorizationKey;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "nginx")
	private Configuration configuration;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "nginx")
	private Set<Server> servers;

	public Nginx() {

	}

	public Nginx(Long id, String name, String endpoint, String serviceName, String settingsPath,
				 String authorizationKey) {
		this.id = id;
		this.name = name;
		this.endpoint = endpoint;
		this.serviceName = serviceName;
		this.settingsPath = settingsPath;
		this.authorizationKey = authorizationKey;
	}

	public Nginx(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public String getSettingsPath() {
		return settingsPath;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setSettingsPath(String settingsPath) {
		this.settingsPath = settingsPath;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAuthorizationKey() {
		return authorizationKey;
	}

	public void setAuthorizationKey(String authorizationKey) {
		this.authorizationKey = authorizationKey;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public Set<Server> getServers() {
		return servers;
	}

}
