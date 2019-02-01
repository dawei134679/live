package com.mpig.api.perf.vo;

/**
 * 异常发生日志
 * @author jackzhang
 */
public class ErrorOccurredLog {
	private String api;
	private String cause;
	private String reqTime;
	
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public String getReqTime() {
		return reqTime;
	}
	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
}
