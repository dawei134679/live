package com.hkzb.game.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class UserUtil {

	/**
	 * 在request header的 token中解析出uid
	 * 
	 * @param request
	 * @return
	 */
	public static String getUid(HttpServletRequest request) {
		String aesToken = request.getHeader("token");
		if(StringUtils.isBlank(aesToken)) {
			aesToken = request.getParameter("token");
		}
		if(StringUtils.isBlank(aesToken)) {
			return null;
		}
		String str = AESCipher.aesDecryptString(aesToken);
		if(StringUtils.isBlank(str)) {
			return null;
		}
		String userInfo = EncryptTokenUtils.decode(str.split("_")[0]);
		if (StringUtils.isBlank(userInfo)) {
			return null;
		}
		return userInfo.split("_")[0];
	}
}
