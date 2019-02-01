package com.tinypig.admin.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Constant {

	private static Logger logger = Logger.getLogger(Constant.class);

	/** 七牛 accessKey */
	public static String qn_accessKey = null;
	/** 七牛 secretKey */
	public static String qn_secretKey = null;
	/** 七牛 默认空间 */
	public static String qn_default_bucket = null;
	/** 七牛 默认空间域名 */
	public static String qn_default_bucket_domain = null;
	/** 七牛 默认空间域名 */
	public static String qn_default_bucket_domain_full = null;
	/** 七牛 直播封面空间 */
	public static String qn_liveCover_bucket = null;
	/** 七牛 直播封面空间域名 */
	public static String qn_liveCover_bucket_domain = null;
	/** 七牛 直播封面空间域名 */
	public static String qn_liveCover_bucket_domain_full = null;
	/** 七牛 直播封面（截图）域名*/
	public static String qn_liveImage_domain = null;
	
	public static String business_server_url = null;
	
	public static String business_server_url_shark1 = null;
	
	public static String im_publish_room_all_url =  null;
	
	public static String im_publish_room_url =  null;

	/**主播注册地址（家族助理推广注册地址）*/
	public static String anchor_register_url =  null;
	
	public static String es_ip =  null;
	public static String es_port = null;

	public static String qn_liveCover_bucket_hls= null;
	
	public static String anchor_weekStar_reward= null;
	public static String user_weekStar_reward= null;
	
	
	//后台登录用户类型
	public enum AdminUserType {
		Normal("普通用户", 0),
		StrategicPartner("战略合作中心", 5),
		ExtensionCenter("钻石公会", 1), 
		Promoters("铂金公会", 2),
		AgentUser("黄金公会", 3),
		Salesman("家族助理", 4);
		private String name;
		private int val;
        private AdminUserType(String name, int val) {
            this.name = name;
            this.val = val;
        }
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getVal() {
			return val;
		}
		public void setVal(int val) {
			this.val = val;
		}
	}
	
	
	/**
	 * 初始化配置
	 * 
	 * @param configUrl
	 */
	public static void initialize(String configPath) {
		logger.info("初始化system.properties - begin");
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(configPath);
			Properties properties = new Properties();
			properties.load(fileReader);
			qn_accessKey = (String) properties.get("qn.accessKey");
			qn_secretKey = (String) properties.get("qn.secretKey");
			qn_default_bucket = (String) properties.get("qn.default.bucket");
			qn_default_bucket_domain = (String) properties.get("qn.default.bucket.domain");
			qn_default_bucket_domain_full = (String) properties.get("qn.default.bucket.domain.full");
			qn_liveCover_bucket = (String) properties.get("qn.liveCover.bucket");
			qn_liveCover_bucket_domain = (String) properties.get("qn.liveCover.bucket.domain");
			qn_liveCover_bucket_domain_full = (String) properties.get("qn.liveCover.bucket.domain.full");
			qn_liveImage_domain = (String) properties.get("qn.liveImage.domain");
			business_server_url = (String) properties.get("business.server.url");
			business_server_url_shark1 = (String) properties.get("business.server.url.shark1");
			im_publish_room_all_url = (String) properties.get("im.publish.room.all.url");
			im_publish_room_url = (String) properties.get("im.publish.room.url");
			anchor_register_url = (String) properties.get("anchor.register.url");
			es_ip = (String) properties.get("es.ip");
			es_port = (String) properties.get("es.port");
			qn_liveCover_bucket_hls = (String) properties.get("qn.liveCover.bucket.hls");
			anchor_weekStar_reward = (String) properties.get("anchor.weekStar.reward");
			user_weekStar_reward = (String) properties.get("user.weekStar.reward");
		} catch (IOException e) {
			logger.error("初始化system.properties - exception", e);
		} finally {
			if(fileReader!=null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					logger.error("关闭流异常",e);
				}
			}
			logger.info("初始化system.properties - end");
		}
	}
}