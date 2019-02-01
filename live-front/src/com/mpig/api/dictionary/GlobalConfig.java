package com.mpig.api.dictionary;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.mpig.api.service.impl.ConfigServiceImpl;

public class GlobalConfig {

	private static GlobalConfig instance = null;
	private static Object lock = new Object();
	private static String mfilePith = null;
	
	
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

	private boolean byPipelined;

	public void loadFromFile(String filePath) {
		if(null == mfilePith){
			mfilePith = filePath;
		}
		
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(filePath));
			setByPipelined(properties.getProperty("byPipelined").equalsIgnoreCase("true"));

			String nLv = properties.getProperty("micUserLv");
			Integer micUserLv = Integer.valueOf(nLv);
			if(null != micUserLv)
				ConfigServiceImpl.setMicUserLv(micUserLv);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//更新用户连麦等级
	public boolean loadMicUserLv(){
		if(null == mfilePith)
			return false;
		
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(mfilePith));
			
			String nLv = properties.getProperty("micUserLv");
			Integer micUserLv = Integer.valueOf(nLv);
			if(null != micUserLv){
				ConfigServiceImpl.setMicUserLv(micUserLv);
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isByPipelined() {
		return byPipelined;
	}

	public void setByPipelined(boolean byPipelined) {
		this.byPipelined = byPipelined;
	}

}
