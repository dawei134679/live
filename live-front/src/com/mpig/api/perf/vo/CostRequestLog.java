package com.mpig.api.perf.vo;

/**
 * 耗时请求日志
 * @author jackzhang
 */
public class CostRequestLog {
	private String api;
	private long cost;
	private String remoteAddr;
	private String reqTime;
	
	public static CostRequestLog createWith(String api,long cost,String remoteAddr,String reqTime){
		CostRequestLog log = new CostRequestLog();
		log.setApi(api);
		log.setCost(cost);
		log.setRemoteAddr(remoteAddr);
		log.setReqTime(reqTime);
		return log;
	}
	
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public long getCost() {
		return cost;
	}
	public void setCost(long cost) {
		this.cost = cost;
	}
	public String getRemoteAddr() {
		return remoteAddr;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	public String getReqTime() {
		return reqTime;
	}
	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}
}
