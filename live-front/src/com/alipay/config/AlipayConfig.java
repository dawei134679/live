package com.alipay.config;

/**
 * 支付宝默认配置 项目在不中不使用
 *
 */
public class AlipayConfig {
	// 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String partner = "2088221188921503";
	
	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_id = partner;

	//商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
	public static String private_key = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAPZL4ExiTvmbd1XJZSsa80QsRWn6E3SNEC7B/x50wWK0NN8lh0MKY8C42z5R23ac8JbpU1lGbgnhSkO0RqQ50r2lniJ68VpGep1XKxkkLKF5D5cQvvW4lXsfKEIcuLHrl+mw4vkjtwIu/dtFhbMjNRjrCaWA45tpdZEHKQrnh/DLAgMBAAECgYAuAsC1JExinuNtOEFZCyDU5HxXLuuBg3S5d25Eb0VNPiL+aNFa+c7myuHSQ5J/FxRu7MmwTNLzr42jFkN4HIE8pKX4qgghF06d72u8jI91VNmruKrCP6A8cZm9xunk3OiJ9yCvxthOaJ8D6moL76iHpQXvqqJzy8sd4YrtoCZOKQJBAP9okxJiRHN6IeyE+tfF/7Bee9x6ksRfjpiNHNAOhzTA8J/Sxv/sflx0jKhlQ5o7RavdBfgHLxKPuAn+THDY9wUCQQD23eY57ySOfHkINkA/+vK9nAm8wcj3IL2g2BcWg9Kf+g/4UC6jAVXdK66k3TlSZCqo10EoAPxR5vZv7K4MNjGPAkEAmWdzJpCVMpLBu3DnxR/sgj9Nv9BzRLVsTV48Vw7S0LGuFSChW+fswZVTQnWIFCPTpfVb92xBLrHZxU7TFNiMDQJBAKI8xpcAYOf7SZdp/+OMSobty0BvomE29SiFmvbC/2L/dgtctqmNsR4/HPFgKdHJ3ndDr6BYtXol7YM06lVAMGUCQQDi9gZLuqHgwNciJ+vJiMLlebQJoh8NC/zqhGPAyDkW9BJjzL2eIOYYsSRzsuHfIC7ZaN4K5AhDr18PjVmjrLr9";
	
	// 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String alipay_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKK    MiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	
	public static String mechartWebBase = "http://localhost:8080/TinyPigWebServer/";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = mechartWebBase +"notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = mechartWebBase +"return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA";
	
	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "./";
		
	// 字符编码格式 目前支持utf-8
	public static String input_charset = "utf-8";
		
	// 支付类型 ，无需修改
	public static String payment_type = "1";
		
	// 调用的接口名，无需修改
	public static String service = "alipay.wap.create.direct.pay.by.user";
}

