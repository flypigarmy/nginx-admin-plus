package com.jslsolucoes.nginx.admin.agent.model;

public class Location {

	private String  path;
	private Integer queuePriority;
	private String  queueHandler;
	private String  upstream;
	private String  additionalLines;

	public Location() {

	}

	@Deprecated
	public Location(String path, Integer queuePriority, String queueHandler, String upstream) {
		this.path = path;
		this.queuePriority = queuePriority;
		this.queueHandler = queueHandler;
		this.upstream = upstream;
	}

	public Location(String path, Integer queuePriority, String queueHandler, String upstream, String additionalLines) {
		this.path = path;
		this.queuePriority = queuePriority;
		this.queueHandler = queueHandler;
		this.upstream = upstream;
		this.additionalLines = additionalLines;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUpstream() {
		return upstream;
	}

	public void setUpstream(String upstream) {
		this.upstream = upstream;
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
}
