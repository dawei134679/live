package com.mpig.api.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mpig.api.utils.FileUtils;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DataSource {
	public static final DataSource instance = new DataSource();
	private static final Map<String, DruidDataSource> dataSource = new HashMap<String, DruidDataSource>();	
	
	public void registerPool(String name,DruidDataSource druidDataSource){
		dataSource.put(name, druidDataSource);
	}
	public DruidDataSource getPool(String name){
		return dataSource.get(name);
	}
	
	public static void read(String configPath,String dbConfigPath){

		Properties properties = new Properties();
		try {
			properties.load(new FileReader(configPath));

			initialize(properties.getProperty("db.url"), properties.getProperty("db.username"), properties.getProperty("db.password"),dbConfigPath);
			
		}catch(Exception ex){
			
		}
	}
	
	private static void initialize(String dbUrl,String username,String password,String dbConfigPath) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			DruidDataSource druidDataSource;
			String strJson = FileUtils.ReadFileToString(dbConfigPath);
			JSONObject jsonObject = JSONObject.parseObject(strJson);		
			JSONArray artJson = jsonObject.getJSONArray("db");
			int len = artJson.size();
			for(int i = 0; i < len; i++){
				JSONObject jo = artJson.getJSONObject(i);
				String dbName = jo.getString("name");
				int maxActive = Integer.parseInt(jo.getString("maxActive"));
				int initialSize = Integer.parseInt(jo.getString("initialSize"));
				int minIdle = Integer.parseInt(jo.getString("minIdle"));
				
				druidDataSource = new DruidDataSource();
				druidDataSource.setMaxActive(maxActive);
				druidDataSource.setMinIdle(minIdle);
				druidDataSource.setInitialSize(initialSize);
	            druidDataSource.setPoolPreparedStatements(true);
	            druidDataSource.setRemoveAbandoned(true);
				String validationQuery = "select 1 from zhu_config.config_giftlist limit 1";
				druidDataSource.setValidationQuery(validationQuery);
				druidDataSource.setTestWhileIdle(true);
				druidDataSource.setTestOnBorrow(false);
				druidDataSource.setTestOnReturn(false);
	            int removeAbandonedTimeout = 10;
				druidDataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
	            druidDataSource.setMinEvictableIdleTimeMillis(30000);
	            druidDataSource.setMaxWaitThreadCount(1000);
	            druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
	            druidDataSource.setUrl(dbUrl+dbName+"?useUnicode=true&characterEncoding=UTF-8");
	            druidDataSource.setUsername(username);
	            druidDataSource.setPassword(password);
	            dataSource.put(dbName, druidDataSource);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 销毁
	 */
	public void destroy(){
		for(DruidDataSource druidDataSource:dataSource.values()){
			if (druidDataSource != null) {
				druidDataSource.close();
			}
		}
		try {
		    AbandonedConnectionCleanupThread.shutdown();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	}
	
	
	
}