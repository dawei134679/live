package com.mpig.api.model;

/**
 * 用户进直播间后获取的主播、视频流及聊天配置信息
 * @author fang
 *
 */
public class EnterLiveModel {

	private ArtistInfoModel artistInfoModel;
	private String host;		//域名
	private String domain;		//流地址 主播：推流 用户 拉流 
	private String pageUrl;		//key秘钥，对方使用
	private String port;		//推流端口
	private String chatPort;	//聊天服务器端口
	private String chatIp;		//聊天服务器IP
	private Integer uid;		//用户uid
	private String shareUrl;	//分享url
	
	public ArtistInfoModel getArtistInfoModel() {
		return artistInfoModel;
	}
	public void setArtistInfoModel(ArtistInfoModel artistInfoModel) {
		this.artistInfoModel = artistInfoModel;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getChatPort() {
		return chatPort;
	}
	public void setChatPort(String chatPort) {
		this.chatPort = chatPort;
	}
	public String getChatIp() {
		return chatIp;
	}
	public void setChatIp(String chatIp) {
		this.chatIp = chatIp;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getShareUrl() {
		return shareUrl;
	}
	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
}
