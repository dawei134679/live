package com.tinypig.admin.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class GlobalConfig {

	private static GlobalConfig instance = null;
	private static Object lock = new Object();
	
	private static Logger logger = Logger.getLogger(GlobalConfig.class);

	private GlobalConfig() {

	}

	public static GlobalConfig getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new GlobalConfig();
			}
		}
		return instance;
	}

	private String publishWordUrl;
	private String appKey;
	private String dbUrl;
	private String dbUrl2;
	private String dbUserName;
	private String dbPassword;

	public String getPublishWordUrl() {
		return publishWordUrl;
	}

	public void setPublishWordUrl(String publishWordUrl) {
		this.publishWordUrl = publishWordUrl;
	}

	public void loadFromFile(String filePath) {
		Properties properties = new Properties();
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(filePath);
			properties.load(fileReader);
			setPublishWordUrl(properties.getProperty("im.publish.word.url"));
			setAppKey(properties.getProperty("appKey"));
			String dbUrl = properties.getProperty("dbUrl");
			String dbUrl2 = properties.getProperty("dbUrl2");
			setDbUrl(dbUrl);
			setDbUrl2(dbUrl2);
			setDbUserName(properties.getProperty("dbUserName"));
			setDbPassword(properties.getProperty("dbPassword"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally {
			if(fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					logger.error("关闭流异常",e);
				}
			}
		}
	}

	
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbUrl2() {
		return dbUrl2;
	}

	public void setDbUrl2(String dbUrl2) {
		this.dbUrl2 = dbUrl2;
	}
}
