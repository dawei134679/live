package com.mpig.api.dictionary;

public final class VideoLineConfig {
	private Integer id; 
	private String domainPrefix; 
	private String pageUrl; 
	private String shareUrl;
	private String host;
	private String port;
	private String key;
	private String hls;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the domainPrefix
	 */
	public String getDomainPrefix() {
		return domainPrefix;
	}
	/**
	 * @param domainPrefix the domainPrefix to set
	 */
	public void setDomainPrefix(String domainPrefix) {
		this.domainPrefix = domainPrefix;
	}
	/**
	 * @return the pageUrl
	 */
	public String getPageUrl() {
		return pageUrl;
	}
	/**
	 * @param pageUrl the pageUrl to set
	 */
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	/**
	 * @return the shareUrl
	 */
	public String getShareUrl() {
		return shareUrl;
	}
	/**
	 * @param shareUrl the shareUrl to set
	 */
	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
	
	/**
	 * 初始化属性
	 * @param id2
	 * @param domain
	 * @param pageUrl
	 * @param shareUrl
	 * @return
	 */
	public VideoLineConfig initWith(Integer id2, String domain, String pageUrl,
			String shareUrl,String host,String port,String key,String hls) {
		this.id = id2;
		this.domainPrefix = domain;
		this.pageUrl = pageUrl;
		this.shareUrl = shareUrl;
		this.host = host;
		this.port = port;
		this.key = key;
		this.hls = hls;
		return this;
	}
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getHls() {
		return hls;
	}
	public void setHls(String hls) {
		this.hls = hls;
	} 
}