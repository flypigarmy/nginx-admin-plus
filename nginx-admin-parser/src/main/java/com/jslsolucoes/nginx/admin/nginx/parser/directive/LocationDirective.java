package com.jslsolucoes.nginx.admin.nginx.parser.directive;

public class LocationDirective {

	private String  path;
	private Integer queuePriority;
	private String  queueHandler;
	private String  upstream;

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

	public String getUpstream() {
		return upstream;
	}

	public void setUpstream(String upstream) {
		this.upstream = upstream;
	}

}
