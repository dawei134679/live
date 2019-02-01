package com.mpig.api.dictionary;

import java.util.List;

public class FirstPayConfig {

	private int start;
	private int end;
	private List<ActGiftConfig> configs;
	
	public int getStart() {
		return start;
	}
	public int getEnd() {
		return end;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public List<ActGiftConfig> getConfigs() {
		return configs;
	}
	public void setConfigs(List<ActGiftConfig> configs) {
		this.configs = configs;
	}
}
