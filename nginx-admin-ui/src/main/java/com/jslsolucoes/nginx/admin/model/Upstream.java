package com.jslsolucoes.nginx.admin.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@SuppressWarnings("serial")
@Entity
@Table(name = "upstream")
@SequenceGenerator(name = "upstream_sq", initialValue = 1, allocationSize = 1, sequenceName = "upstream_sq")
public class Upstream implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "upstream_sq")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "additional_lines")
	private String additionalLines;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_strategy")
	private Strategy strategy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_nginx")
	private Nginx nginx;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_resource_identifier")
	private ResourceIdentifier resourceIdentifier;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "upstream")
	private Set<UpstreamServer> servers;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "upstream")
	private Set<VirtualHostLocation> virtualHostLocations;

	public Upstream() {
		// default constructor
	}

	public Upstream(Long id, String name, String additionalLines, Strategy strategy,
					ResourceIdentifier resourceIdentifier, Nginx nginx) {
		this.id = id;
		this.name = name;
		this.additionalLines = additionalLines;
		this.strategy = strategy;
		this.resourceIdentifier = resourceIdentifier;
		this.nginx = nginx;
	}

	public Upstream(Long id) {
		this.id = id;
	}

	@Deprecated
	public Upstream(String name, Strategy strategy, Nginx nginx) {
		this.name = name;
		this.strategy = strategy;
		this.nginx = nginx;
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

	public String getAdditionalLines() {
		return additionalLines;
	}

	public void setAdditionalLines(String additionalLines) {
		this.additionalLines = additionalLines;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public Set<UpstreamServer> getServers() {
		return servers;
	}

	public ResourceIdentifier getResourceIdentifier() {
		return resourceIdentifier;
	}

	public void setResourceIdentifier(ResourceIdentifier resourceIdentifier) {
		this.resourceIdentifier = resourceIdentifier;
	}

	public Set<VirtualHostLocation> getVirtualHostLocations() {
		return virtualHostLocations;
	}

	public Nginx getNginx() {
		return nginx;
	}

	public void setNginx(Nginx nginx) {
		this.nginx = nginx;
	}
}
