package com.mpig.api.modelcomet;

public class MessageCMod extends BaseCMod {
	public MessageCMod() {
		this.setCometProtocol(CModProtocol.private_msg);
	}

	private int msgId;// 消息id
	private String headimage;// 头像
	private String msg; // 消息内容
	private long sendtime; // 发送时间
	private int relation; // =1粉丝 =2关注
	private int gid;
	private int msgType;// =0普通消息 =1语音消息 =2图片消息 =3视频消息 =4赠送礼物
	private String dstNickname;

	public int getMsgId() {
		return msgId;
	}

	public String getHeadimage() {
		return headimage;
	}

	public String getMsg() {
		return msg;
	}

	public long getSendtime() {
		return sendtime;
	}

	public int getRelation() {
		return relation;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setSendtime(long sendtime) {
		this.sendtime = sendtime;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getDstNickname() {
		return dstNickname;
	}

	public void setDstNickname(String dstNickname) {
		this.dstNickname = dstNickname;
	}
}
