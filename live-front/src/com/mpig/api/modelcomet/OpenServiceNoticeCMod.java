package com.mpig.api.modelcomet;

public class OpenServiceNoticeCMod extends BaseCMod {
	public OpenServiceNoticeCMod() {
		this.setCometProtocol(CModProtocol.open_service);
	}

	private Object data;
	private Integer tid;
	private Long creditTotal;
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public Long getCreditTotal() {
		return creditTotal;
	}

	public void setCreditTotal(Long creditTotal) {
		this.creditTotal = creditTotal;
	}
	
	
	
}
