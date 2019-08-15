package com.jslsolucoes.nginx.admin.model;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "configuration")
@SequenceGenerator(name = "configuration_sq", initialValue = 1, allocationSize = 1, sequenceName = "configuration_sq")
public class Configuration implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "configuration_sq")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_nginx")
	private Nginx nginx;

	@Column(name = "gzip")
	private Integer gzip;

	@Column(name = "max_post_size")
	private Integer maxPostSize;

	@Column(name = "root_port")
	private Integer rootPort;

	public Configuration() {

	}

	public Configuration(Long id, Integer gzip, Integer maxPostSize, Integer rootPort, Nginx nginx) {
		this.id = id;
		this.gzip = gzip;
		this.maxPostSize = maxPostSize;
		this.rootPort = rootPort;
		this.nginx = nginx;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Nginx getNginx() {
		return nginx;
	}

	public void setNginx(Nginx nginx) {
		this.nginx = nginx;
	}

	public Integer getGzip() {
		return gzip;
	}

	public void setGzip(Integer gzip) {
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
}
