package com.jslsolucoes.nginx.admin.model;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "virtual_host_location")
@SequenceGenerator(name = "virtual_host_location_sq", initialValue = 1, allocationSize = 1, sequenceName = "virtual_host_location_sq")
public class VirtualHostLocation implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "virtual_host_location_sq")
	private Long id;

	@Column(name = "path")
	private String path;

	@Column(name = "queue_priority")
	private Integer queuePriority;

	@Column(name = "queue_handler")
	private String queueHandler;

	@Column(name = "additional_lines")
	private String additionalLines;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_upstream")
	private Upstream upstream;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_virtual_host")
	private VirtualHost virtualHost;

	public VirtualHostLocation() {
		// default constructor
	}

	@Deprecated
	public VirtualHostLocation(String path, Integer queuePriority, String queueHandler,
							   Upstream upstream) {
		this.path = path;
		this.queuePriority = queuePriority;
		this.queueHandler = queueHandler;
		this.upstream = upstream;
	}

	public VirtualHostLocation(String path, Integer queuePriority, String queueHandler, String additionalLines,
							   Upstream upstream) {
		this.path = path;
		this.queuePriority = queuePriority;
		this.queueHandler = queueHandler;
		this.additionalLines = additionalLines;
		this.upstream = upstream;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getQueuePriority() {
		return queuePriority;
	}

	public void setQueuePriority(Integer queuePriority) {
		this.queuePriority = queuePriority;
	}

	public String getQueueHandler() {
		return queueHandler;
	}

	public void setQueueHandler(String queueHandler) {
		this.queueHandler = queueHandler;
	}

	public String getAdditionalLines() {
		return additionalLines;
	}

	public void setAdditionalLines(String additionalLines) {
		this.additionalLines = additionalLines;
	}

	public Upstream getUpstream() {
		return upstream;
	}

	public void setUpstream(Upstream upstream) {
		this.upstream = upstream;
	}

	public VirtualHost getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(VirtualHost virtualHost) {
		this.virtualHost = virtualHost;
	}

}
