package com.mpig.api.modelcomet;

public class FeedNoticeCmod extends BaseCMod{
	public FeedNoticeCmod(){
		this.setCometProtocol(CModProtocol.feed_notice);
	}
	public static final int TYPE_LAUD = 1;
	public static final int TYPE_REPLY = 2;
	public static final int TYPE_REWARD = 3;

	public static final int TYPE_REWARDGIFT = 4;	//tosy debug 送花
	
	private Object data;
	private Integer feedId;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Integer getFeedId() {
		return feedId;
	}
	public void setFeedId(Integer feedId) {
		this.feedId = feedId;
	}
	
}
