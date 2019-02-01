/**
 * 
 */
package com.mpig.api.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mpig.api.model.ReturnModel;

import java.util.Map;

/**
 * @author fang
 *
 */
public interface IAuthService {
	/**
	 * 验证token
	 * 
	 * @param ncode
	 *            客户端请求码
	 * @param os
	 *            平台代码
	 * @param imei
	 *            用户设备唯一标识码
	 * @param reqtime
	 *            请求时间，单位为秒
	 * @param token
	 *            用户唯一码，服务端生成，可解码
	 * @param bl
	 *            当token不为空时有效 true 强制登录 false 忽略
	 * @return
	 */
	public void authToken(String ncode, Byte os, String imei, Long reqtime,
			String token, Boolean bl, ReturnModel _reReturnModel);

	/**
	 * 解析token
	 * 
	 * @param token
	 * @return
	 */
	public Integer decryptToken(String token, ReturnModel returnModel);

	/**
	 * 获取Token
	 * 
	 * @param uid
	 *            用户uid
	 * @param pword
	 *            用户密码 加密后的
	 * @param os
	 *            平台
	 * @param imei
	 *            设备唯一码
	 * @return
	 */
	public String encryptToken(Integer uid, String pword, Byte os, String imei);

	/**
	 * 注册接口
	 * 
	 * @param accountName
	 *            账号名称
	 * @param pword
	 *            密码
	 * @param regiterIp
	 *            注册IP
	 * @param nickName
	 *            昵称
	 * @param authKey
	 *            第三方标识openid + source
	 * @param source
	 *            第三方登陆标识
	 * @param registchannel
	 *            注册渠道
	 * @param subregistchannel
	 *            注册子渠道
	 * @param registos
	 *            注册平台注册平台 0=>默认 1=>android 2=>iphone 3=>ipad
	 * @param registimei
	 *            手机imei
	 * @param headimage
	 *            用户图像
	 * @return
	 */
	public ReturnModel register(String accountName, String pword,
			Long regiterIp, String nickName, String authKey, String source,
			String registchannel, String subregistchannel, Byte registos,
			String registimei, String headimage, Boolean sex, String phone,String unionid,Long salesmanId);

	/**
	 * 获取第三方用户信息
	 * 
	 * @param accessToken
	 * @param openid
	 * @param source
	 *            =weixin 微信 =qq QQ =sina 新浪微博
	 * @return ThirdModel
	 */
	public HttpResponse<JsonNode> getThirdUserInfo(String accessToken,
			String openid, String source, Byte os);

	/**
	 * 用户进入房间获取到的token值
	 * 
	 * @param uid
	 *            //用户UID
	 * @return
	 */
	public String getUserRoomToken(Integer uid);

	/**
	 * 登陆amdin获取comet的id和token
	 * 
	 * @param uid
	 * @param map
	 * @return
	 */
	public boolean rpcAdminLoginComet(int uid, Map<String, Object> map);

	/**
	 * 更新accountId
	 * @param uid
	 */
	public void updAccountid(int uid);
	
	/**
	 * 更新 用户账号unionid
	 * @param uid
	 * @param unionId
	 */
	public void updUnionIdOfAccount(int uid,String unionId);
	
	/**
	 * 记录注册/登录详细
	 * @param uid 用户uid
	 * @param os 注册/登录设备 
	 * @param loginMethod 注册方式
	 * @param mobileModel 机型
	 * @param channel 注册渠道
	 * @param mobileVersion 版本号
	 * @param isType =1注册 =2登录
	 * @param loginTime 登录时间
	 */
	public void insertLoginDetail(int uid, int os, String loginMethod, String mobileModel, String channel,
			String mobileVersion, int isType, Long loginTime);

}
