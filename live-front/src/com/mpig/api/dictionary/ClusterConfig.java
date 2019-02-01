package com.mpig.api.dictionary;

public class ClusterConfig {
	/***
	 * elasticsearch相关配置
	 */
	private String esHost;
	private Integer esPort;
	private Boolean esEnable;
	
	
	
	public String getEsHost() {
		return esHost;
	}
	public void setEsHost(String esHost) {
		this.esHost = esHost;
	}
	public Integer getEsPort() {
		return esPort;
	}
	public void setEsPort(Integer esPort) {
		this.esPort = esPort;
	}
	
	public Boolean getEsEnable() {
		return esEnable;
	}
	public void setEsEnable(Boolean esEnable) {
		this.esEnable = esEnable;
	}
	public ClusterConfig initWith(String esHost,Integer esPort,Boolean esEnable){
		this.esHost = esHost;
		this.esPort = esPort;
		this.esEnable = esEnable;
		return this;
	}
}
