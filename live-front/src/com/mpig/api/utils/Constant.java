package com.mpig.api.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Constant {

	private static Logger logger = Logger.getLogger(Constant.class);

	/** 默认每页显示条数 */
	public static int defaultPageSize = 10;

	/** 默认UTF-8字符集 */
	public static String defaultCharset = "UTF-8";

	/** 内部文件服务器地址 */
	public static String file_upload_server_url = null;

	/** 七牛 accessKey */
	public static String qn_accessKey = null;
	/** 七牛 secretKey */
	public static String qn_secretKey = null;

	public static String qn_hub = null;

	public static String qn_streamId_pre = null;
	/** 七牛 默认空间 */
	public static String qn_default_bucket = null;
	/** 七牛 默认空间域名 */
	public static String qn_default_bucket_domain = null;
	/** 七牛 动态广场空间 */
	public static String qn_feed_bucket = null;
	/** 七牛 动态广场空间 */
	public static String qn_feed_bucket_domain = null;
	/** 七牛 相册空间 */
	public static String qn_photo_bucket = null;
	/** 七牛 相册空间域名 */
	public static String qn_photo_bucket_domain = null;
	/** 七牛 头像空间 */
	public static String qn_headImage_bucket = null;
	/** 七牛 头像空间域名 */
	public static String qn_headImage_bucket_domain = null;
	/** 七牛 直播封面空间 */
	public static String qn_liveCover_bucket = null;
	/** 七牛 直播封面空间域名 */
	public static String qn_liveCover_bucket_domain = null;
	/** 阿里云 短信Access Key ID */
	public static String sms_Access_Key_ID = null;
	/** 阿里云 短信Access Key Secret */
	public static String sms_Access_Key_Secret = null;
	/** 阿里云 注册功能-短信签名名称 */
	public static String sms_reg_sign_name = null;
	/** 阿里云 注册功能-短信模板编号*/
	public static String sms_reg_template_code = null;

	/** ios下载地址*/
	public static String apk_iosDownUrl = null;
	/** android下载地址*/
	public static String apk_androidDownUrl = null;

	public static String mall_props1_des = null;

	/** 充值aes加密Key 16位*/
	public static String aesKey = "_mai_ya_pay_key_";

	/** 微信APPID*/
	public static String webChat_appId = null;
	/** 融宝商户ID*/
	public static String reapal_merchantId = null;
	/** 融宝私钥*/
	public static String reapal_privateKey = null;
	/** 证书密码*/
	public static String reapal_certificatePwd = null;
	/** 安全校验码*/
	public static String reapal_securityKey = null;
	/** 支付状态回调URL*/
	public static String reapal_payStatus_notifyUrl = null;

	/** 充值金币倍数，一块钱等于多少个金币 */
	public static String recharge_multiple = null;

	/** 支付宝支付 网关*/
	public static String alipay_getway = null;
	/** 支付宝 APPID*/
	public static String alipay_appId = null;
	/** 支付宝支付 商家私钥*/
	public static String alipay_app_privateKey = null;
	/** 支付宝提供的公钥*/
	public static String alipay_publicKey = null;
	/** 支付宝状态回调url*/
	public static String alipay_payStatus_notifyUrl = null;
	/** 支付宝状态回调页面url*/
	public static String alipay_payStatus_returnUrl = null;

	/**苹果应用内支付验证订单地址**/
	public static String apple_inner_pay_url = null;
	/**苹果应用内支付验证订单密码**/
	public static String apple_inner_pay_password = null;

	/**UNPay商户ID*/
	public static String unpay_merchantId = null;
	/**UNPay商户key*/
	public static String unpay_key = null;
	/**UNPay发起支付接口*/
	public static String unpay_api_payPayMegerHandler = null;
	/**UNPay查询订单接口*/
	public static String unpay_api_PayOrderQueryHandler = null;
	/**UNPay异步通知回调地址*/
	public static String unpay_payStatus_notifyUrl = null;
	/**UNPay跳转商户的url地址*/
	public static String unpay_backurl = null;
	/**UNPay微信redirectUrl地址*/
	public static String unpay_weixin_redirectUrl = null;

	/**微信h5支付appid**/
	public static String wechat_h5pay_unifiedOrder = null;
	/**#微信APPID号**/
	public static String wechat_h5pay_appid = null;
	/**#微信商户号**/
	public static String wechat_h5pay_mch_id = null;
	/**#微信h5支付异步回调**/
	public static String wechat_h5pay_notify_url = null;
	/**#微信h5支付wap_url**/
	public static String wechat_h5pay_wap_url = null;
	/**#微信h5支付签名秘钥**/
	public static String wechat_h5pay_signkey = null;
	/**#微信h5支付完成同步跳转地址**/
	public static String wechat_h5pay_redirectUrl = null;
	
	
	/** ↓↓↓↓↓↓↓↓↓↓融宝h5快捷支付↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ **/
	
	// 需要更换的信息
	// 商户ID，由纯数字组成的字符串
	public static String reapal_quick_payment_merchant_id;
	// 交易安全检验码，由数字和字母组成的64位字符串
	public static String reapal_quick_payment_key;
	// 签约融宝支付账号或卖家收款融宝支付帐户
	public static String reapal_quick_payment_seller_email;
	// 通知地址，由商户提供
	public static String reapal_quick_payment_notify_url;
	// 返回地址，由商户提供
	public static String reapal_quick_payment_return_url;
	// 商户私钥
	public static String reapal_quick_payment_privateKey;
	// 商户私钥密码
	public static String reapal_quick_payment_password;
	// 测试环境地址
	public static String reapal_quick_payment_rongpay_api;
	// 版本号
	public static String reapal_quick_payment_version;
	// 融宝公钥 正式环境不用修改
	public static String reapal_quick_payment_pubKeyUrl;
	// 字符编码格式 目前支持 utf-8
	public static String reapal_quick_payment_charset;
	// 签名方式 不需修改
	public static String reapal_quick_payment_sign_type;
	// 访问模式,根据自己的服务器是否支持ssl访问，若支持请选择https；若不支持请选择http
	public static String reapal_quick_payment_transport;

	/**↑↑↑↑↑↑↑↑↑↑融宝h5快捷支付↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/

	/**用户注册默认头像七牛地址*/
	public static String user_defaultHeadImage = null;
	/**PK时间间隔列表*/
	public static String pk_time_list = null;
	/**PK惩罚时间*/
	public static String pk_penalty_time_list = null;
	/**PK状态 (未PK中)*/
	public static int pk_status_0 = 0;
	/**PK状态 (PK中)*/
	public static int pk_status_1 = 1;
	/**未连麦*/
	public static int lianmai_status_0 = 0;
	/**连麦中*/
	public static int lianmai_status_1 = 1;

	/**注册跳转地址*/
	public static String anchor_register_url = null;

	public class AlipayTradeStatus {
		/** 交易创建，等待买家付款*/
		public final static String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
		/** 未付款交易超时关闭，或支付完成后全额退款*/
		public final static String TRADE_CLOSED = "TRADE_CLOSED";
		/** 交易支付成功*/
		public final static String TRADE_SUCCESS = "TRADE_SUCCESS";
		/** 交易结束，不可退款*/
		public final static String TRADE_FINISHED = "TRADE_FINISHED";
	}

	public static void initialize(String configPath) {
		logger.info("初始化system.properties - begin");
		FileReader fileReader = null;
		try {
			String classPath = new File(Constant.class.getResource("/").getFile()).getCanonicalPath();
			classPath = classPath.substring(0, classPath.lastIndexOf("classes"));
			String systemConfigPatch = classPath + "system.properties";
			fileReader = new FileReader(systemConfigPatch);
			Properties properties = new Properties();
			properties.load(fileReader);
			file_upload_server_url = (String) properties.get("fileserver.upload.url");
			qn_accessKey = (String) properties.get("qn.accessKey");
			qn_secretKey = (String) properties.get("qn.secretKey");
			qn_default_bucket = (String) properties.get("qn.default.bucket");
			qn_default_bucket_domain = (String) properties.get("qn.default.bucket.domain");
			qn_feed_bucket = (String) properties.get("qn.feed.bucket");
			qn_feed_bucket_domain = (String) properties.get("qn.feed.bucket.domain");
			qn_photo_bucket = (String) properties.get("qn.photo.bucket");
			qn_photo_bucket_domain = (String) properties.get("qn.photo.bucket.domain");
			qn_headImage_bucket = (String) properties.get("qn.headImage.bucket");
			qn_headImage_bucket_domain = (String) properties.get("qn.headImage.bucket.domain");
			qn_liveCover_bucket = (String) properties.get("qn.liveCover.bucket");
			qn_liveCover_bucket_domain = (String) properties.get("qn.liveCover.bucket.domain");
			sms_Access_Key_ID = (String) properties.get("sms.aliyun.access.key.id");
			sms_Access_Key_Secret = (String) properties.get("sms.aliyun.access.key.secret");
			sms_reg_sign_name = (String) properties.get("sms.aliyun.reg.sign.name");
			sms_reg_template_code = (String) properties.get("sms.aliyun.reg.template.code");
			qn_hub = (String) properties.get("qn.hub");
			qn_streamId_pre = (String) properties.get("qn.streamId.pre");
			apk_iosDownUrl = (String) properties.get("apk.iosDownUrl");
			apk_androidDownUrl = (String) properties.get("apk.androidDownUrl");
			mall_props1_des = (String) properties.get("mall.props1.des");

			webChat_appId = (String) properties.get("webChat.appId");
			reapal_merchantId = (String) properties.get("reapal.merchantId");
			reapal_privateKey = (String) properties.get("reapal.privateKey");
			reapal_certificatePwd = (String) properties.get("reapal.certificatePwd");
			reapal_securityKey = (String) properties.get("reapal.securityKey");
			reapal_payStatus_notifyUrl = (String) properties.get("reapal.payStatus.notifyUrl");

			recharge_multiple = (String) properties.get("recharge.multiple");

			alipay_getway = (String) properties.get("alipay.getway");
			alipay_appId = (String) properties.get("alipay.appId");
			alipay_app_privateKey = (String) properties.get("alipay.app.privateKey");
			alipay_publicKey = (String) properties.get("alipay.publicKey");
			alipay_payStatus_notifyUrl = (String) properties.get("alipay.payStatus.notifyUrl");
			alipay_payStatus_returnUrl = (String) properties.get("alipay.payStatus.returnUrl");

			apple_inner_pay_url = (String) properties.get("apply.inner.pay.url");
			apple_inner_pay_password = (String) properties.get("apply.inner.pay.password");

			unpay_merchantId = (String) properties.get("unpay.merchantId");
			unpay_key = (String) properties.get("unpay.api.key");
			unpay_api_payPayMegerHandler = (String) properties.get("unpay.api.payPayMegerHandler");
			unpay_api_PayOrderQueryHandler = (String) properties.get("unpay.api.PayOrderQueryHandler");
			unpay_payStatus_notifyUrl = (String) properties.get("unpay.payStatus.notifyUrl");
			unpay_backurl = (String) properties.get("unpay.backurl");
			unpay_weixin_redirectUrl = (String) properties.get("unpay.weixin.redirectUrl");

			wechat_h5pay_unifiedOrder = (String) properties.get("wechat.h5pay.unifiedOrder");
			wechat_h5pay_appid = (String) properties.get("wechat.h5pay.appid");
			wechat_h5pay_mch_id = (String) properties.get("wechat.h5pay.mch_id");
			wechat_h5pay_notify_url = (String) properties.get("wechat.h5pay.notify_url");
			wechat_h5pay_wap_url = (String) properties.get("wechat.h5pay.wap_url");
			wechat_h5pay_signkey = (String) properties.get("wechat.h5pay.signkey");
			wechat_h5pay_redirectUrl = (String) properties.get("wechat.h5pay.redirectUrl");

			reapal_quick_payment_merchant_id = (String) properties.get("reapal.quick.payment.merchant_id");
			reapal_quick_payment_key = (String) properties.get("reapal.quick.payment_key");
			reapal_quick_payment_seller_email = (String) properties.get("reapal.quick.payment.seller_email");
			reapal_quick_payment_notify_url = (String) properties.get("reapal.quick.payment.notify_url");
			reapal_quick_payment_return_url = (String) properties.get("reapal.quick.payment.return_url");
			reapal_quick_payment_privateKey = (String) properties.get("reapal.quick.payment.privateKey");
			reapal_quick_payment_password = (String) properties.get("reapal.quick.payment.password");
			reapal_quick_payment_rongpay_api = (String) properties.get("reapal.quick.payment.rongpay_api");
			reapal_quick_payment_version = (String) properties.get("reapal.quick.payment.version");
			reapal_quick_payment_pubKeyUrl = (String) properties.get("reapal.quick.payment.pubKeyUrl");
			reapal_quick_payment_charset = (String) properties.get("reapal.quick.payment.charset");
			reapal_quick_payment_sign_type = (String) properties.get("reapal.quick.payment.sign_type");
			reapal_quick_payment_transport = (String) properties.get("reapal.quick.payment.transport");

			anchor_register_url = (String) properties.get("anchor.register.url");
			user_defaultHeadImage = (String) properties.get("user.defaultHeadImage");
			pk_time_list = (String) properties.get("pk.time.list");
			pk_penalty_time_list = (String) properties.get("pk.penalty.time.list");
		} catch (IOException e) {
			logger.error("初始化system.properties - exception", e);
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					logger.error("关闭流异常", e);
				}
			}
			logger.info("初始化system.properties - end");
		}
	}
}