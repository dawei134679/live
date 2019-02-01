package com.mpig.api.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 传参处理
 * @author fang
 *
 */
public class ParamHandleUtils {

	public static String getParam(String strVal){
		return null;
	}
	
	/**
	 * 判断某字符串是否为空或长度为0或由空白符(whitespace)构成
	 * @param req
	 * @param strings
	 * @return true 是空或NULL false 有值
	 */
	public static Boolean isBlank(HttpServletRequest req,String...strings){
		for (int i = 0; i < strings.length; i++) {
			if (StringUtils.isBlank(req.getParameter(strings[i]))) {
				System.out.println("参数不存在："+strings[i]);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断该字符串是不是为数字(0~9)组成，如果是，返回true 但该方法不识别有小数点和 请注意
	 * @param req
	 * @param strings
	 * @return true 是数字 false 不是数字
	 */
	public static Boolean isInt(HttpServletRequest req,String...strings){
		for (int i = 0; i < strings.length; i++) {
			if(!StringUtils.isNumeric(req.getParameter(strings[i]))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 获取请求中的所有参数
	 * @param paramValues
	 * @return
	 */
	public static String getParamValue(String[] paramValues) {
		if (paramValues == null) {
			return StringUtils.EMPTY;
		}
		String paramValue = null;
		if (paramValues.length == 1) {
			paramValue = paramValues[0];
		} else if (paramValues.length > 1) {
			paramValue = StringUtils.join(paramValues, ",");
		}
		return StringUtils.defaultString(paramValue);
	}
}
