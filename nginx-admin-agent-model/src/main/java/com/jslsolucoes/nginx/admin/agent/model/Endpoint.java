package com.jslsolucoes.nginx.admin.agent.model;

public class Endpoint {

    private String ip;
    private Integer port;
    private String additionalOption;

    public Endpoint() {

    }

    public Endpoint(String ip, Integer port, String additionalOption) {
        this.ip = ip;
        this.port = port;
        this.additionalOption = additionalOption == null ? "" : additionalOption;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAdditionalOption() {
        return additionalOption;
    }

    public void setAdditionalOption(String additionalOption) {
        this.additionalOption = additionalOption;
    }

}
