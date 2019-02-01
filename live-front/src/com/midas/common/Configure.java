package com.midas.common;

public class Configure {
	public static final String MAC_NAME = "HmacSHA1";
	public static final String ENCODING = "UTF-8";
	
	public static final Integer APP_ID = 1105690584;
	/**
	 * 沙盒相关配置，提交测试时使用
	 */
	public static final String SANDBOX_APPKEY = "FCpTxw7uO3rezqtGImlmkFUSpArWIo3r"; //沙箱key
	public static final String SANDBOX_GETBALANCE = "https://ysdktest.qq.com/mpay/get_balance_m"; //沙箱-获取余额
	public static final String SANDBOX_PAY = "https://ysdktest.qq.com/mpay/pay_m"; //沙箱-扣除金额
	public static final String SANDBOX_CANCELPAY = "https://ysdktest.qq.com/mpay/cancel_pay_m"; //沙箱-扣除金额
	
	/**
	 * 现网相关配置，提交上线时使用
	 */
	public static final String OFFICIAL_APPKEY = "JSl9hjiu48ShaN1DpSRhKDFg8m2o2vgQ"; //现网key
	public static final String OFFICIAL_GETBALANCE = "https://ysdk.qq.com/mpay/get_balance_m"; //现网-获取余额
	public static final String OFFICIAL_PAY = "https://ysdk.qq.com/mpay/pay_m"; //现网-扣除金额
	public static final String OFFICIAL_CANCELPAY = "https://ysdk.qq.com/mpay/cancel_pay_m"; //现网-扣除金额
}
