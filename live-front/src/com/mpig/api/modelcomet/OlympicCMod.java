package com.mpig.api.modelcomet;

public class OlympicCMod extends BaseCMod {
	public OlympicCMod() {
		this.setCometProtocol(CModProtocol.olympic);
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private String url;
	
}
