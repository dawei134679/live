package com.mpig.api.dictionary;

import com.alibaba.fastjson.JSONObject;

public class UrlConfig {

	private String adminrpc_publish_room_all;
	private String adminrpc_publish_room;
	private String adminrpc_user_token;
	private String adminrpc_begin_live;
	private String adminrpc_publish_world;
	private String adminrpc_end_live;
	private String adminrpc_publish_live;
	private String silent_on;
	private String silent_off;
	private String kick_out;
	private String channel_weixin;
	private String channel_qq;
	private String channel_weibo;
	private String mobilemsgchannel;
	private String weixinTixianUrl;
	private String weixinBindUrl;
	private String weixin_userInfo;
	private String weixin_userInfoByToken;
	private String weixin_callback;
	private String weixin_unifiedorder;
	private String weixin_redpack_clientIP;
	private String admin_piganalysis; // 启动统计

	private String qn_accessKey;
	private String qn_secretKey;

	public String getAdminrpc_publish_room_all() {
		return adminrpc_publish_room_all;
	}

	public void setAdminrpc_publish_room_all(String adminrpc_publish_room_all) {
		this.adminrpc_publish_room_all = adminrpc_publish_room_all;
	}

	public String getAdminrpc_publish_room() {
		return adminrpc_publish_room;
	}

	public void setAdminrpc_publish_room(String adminrpc_publish_room) {
		this.adminrpc_publish_room = adminrpc_publish_room;
	}

	public String getAdminrpc_user_token() {
		return adminrpc_user_token;
	}

	public void setAdminrpc_user_token(String adminrpc_user_token) {
		this.adminrpc_user_token = adminrpc_user_token;
	}

	public String getAdminrpc_begin_live() {
		return adminrpc_begin_live;
	}

	public void setAdminrpc_begin_live(String adminrpc_begin_live) {
		this.adminrpc_begin_live = adminrpc_begin_live;
	}

	public String getAdminrpc_end_live() {
		return adminrpc_end_live;
	}

	public void setAdminrpc_end_live(String adminrpc_end_live) {
		this.adminrpc_end_live = adminrpc_end_live;
	}

	public String getAdminrpc_publish_live() {
		return adminrpc_publish_live;
	}

	public void setAdminrpc_publish_live(String adminrpc_publish_live) {
		this.adminrpc_publish_live = adminrpc_publish_live;
	}

	public String getSilent_on() {
		return silent_on;
	}

	public String getSilent_off() {
		return silent_off;
	}

	public String getKick_out() {
		return kick_out;
	}

	public void setSilent_on(String silent_on) {
		this.silent_on = silent_on;
	}

	public void setSilent_off(String silent_off) {
		this.silent_off = silent_off;
	}

	public void setKick_out(String kick_out) {
		this.kick_out = kick_out;
	}

	public String getChannel_weixin() {
		return channel_weixin;
	}

	public void setChannel_weixin(String channel_weixin) {
		this.channel_weixin = channel_weixin;
	}

	public String getChannel_qq() {
		return channel_qq;
	}

	public void setChannel_qq(String channel_qq) {
		this.channel_qq = channel_qq;
	}

	public String getChannel_weibo() {
		return channel_weibo;
	}

	public void setChannel_weibo(String channel_weibo) {
		this.channel_weibo = channel_weibo;
	}

	public String getMobilemsgchannel() {
		return mobilemsgchannel;
	}

	public void setMobilemsgchannel(String mobilemsgchannel) {
		this.mobilemsgchannel = mobilemsgchannel;
	}

	public String getWeixinTixianUrl() {
		return weixinTixianUrl;
	}

	public void setWeixinTixianUrl(String weixinTixianUrl) {
		this.weixinTixianUrl = weixinTixianUrl;
	}

	public UrlConfig initWith(JSONObject jo) {
		this.adminrpc_publish_room_all = jo.getString("adminrpc_publish_room_all");
		this.adminrpc_publish_room = jo.getString("adminrpc_publish_room");
		this.adminrpc_user_token = jo.getString("adminrpc_user_token");
		this.adminrpc_begin_live = jo.getString("adminrpc_begin_live");
		this.adminrpc_end_live = jo.getString("adminrpc_end_live");
		this.adminrpc_publish_live = jo.getString("adminrpc_publish_live");
		this.channel_weixin = jo.getString("channel_weixin");
		this.channel_qq = jo.getString("channel_qq");
		this.channel_weibo = jo.getString("channel_weibo");
		this.mobilemsgchannel = jo.getString("mobilemsgchannel");
		this.weixinTixianUrl = jo.getString("weixinTixianUrl");
		this.weixinBindUrl = jo.getString("weixinBindUrl");
		this.weixin_userInfo = jo.getString("weixin_userInfo");
		this.kick_out = jo.getString("kick_out");
		this.silent_on = jo.getString("silent_on");
		this.silent_off = jo.getString("silent_off");
		this.weixin_userInfoByToken = jo.getString("weixin_userInfoByToken");
		this.weixin_callback = jo.getString("weixin_callback");
		this.weixin_unifiedorder = jo.getString("weixin_unifiedorder");
		this.weixin_redpack_clientIP = jo.getString("weixin_redpack_clientIP");
		this.admin_piganalysis = jo.getString("admin_piganalysis");// 启动统计
		this.qn_accessKey = jo.getString("qn_accessKey");// tc
		this.qn_secretKey = jo.getString("qn_secretKey");// tc
		this.adminrpc_publish_world = jo.getString("adminrpc_publish_world");
		return this;
	}

	public String getWeixinBindUrl() {
		return weixinBindUrl;
	}

	public void setWeixinBindUrl(String weixinBindUrl) {
		this.weixinBindUrl = weixinBindUrl;
	}

	public String getWeixin_userInfo() {
		return weixin_userInfo;
	}

	public void setWeixin_userInfo(String weixin_userInfo) {
		this.weixin_userInfo = weixin_userInfo;
	}

	public String getWeixin_userInfoByToken() {
		return weixin_userInfoByToken;
	}

	public void setWeixin_userInfoByToken(String weixin_userInfoByToken) {
		this.weixin_userInfoByToken = weixin_userInfoByToken;
	}

	public String getWeixin_callback() {
		return weixin_callback;
	}

	public void setWeixin_callback(String weixin_callback) {
		this.weixin_callback = weixin_callback;
	}

	public String getWeixin_unifiedorder() {
		return weixin_unifiedorder;
	}

	public void setWeixin_unifiedorder(String weixin_unifiedorder) {
		this.weixin_unifiedorder = weixin_unifiedorder;
	}

	public String getWeixin_redpack_clientIP() {
		return weixin_redpack_clientIP;
	}

	public void setWeixin_redpack_clientIP(String weixin_redpack_clientIP) {
		this.weixin_redpack_clientIP = weixin_redpack_clientIP;
	}

	public String getAdmin_piganalysis() {
		return admin_piganalysis;
	}

	public void setAdmin_piganalysis(String admin_piganalysis) {
		this.admin_piganalysis = admin_piganalysis;
	}

	public String getQn_accessKey() {
		return qn_accessKey;
	}

	public void setQn_accessKey(String qn_accessKey) {
		this.qn_accessKey = qn_accessKey;
	}

	public String getQn_secretKey() {
		return qn_secretKey;
	}

	public void setQn_secretKey(String qn_secretKey) {
		this.qn_secretKey = qn_secretKey;
	}

	public String getAdminrpc_publish_world() {
		return adminrpc_publish_world;
	}

	public void setAdminrpc_publish_world(String adminrpc_publish_world) {
		this.adminrpc_publish_world = adminrpc_publish_world;
	}
}
