package com.tinypig.admin.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.util.FileUtil;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DataSource {
	public static final DataSource instance = new DataSource();
	private static final Map<String, DruidDataSource> dbMaster = new HashMap<String, DruidDataSource>();
	private static final Map<String, DruidDataSource> dbSlave = new HashMap<String, DruidDataSource>();

	public DruidDataSource getMasterPool(String name) {
		return dbMaster.get(name);
	}
	public DruidDataSource getSlavePool(String name) {
		return dbSlave.get(name);
	}

	public static void read(String configPath, String dbConfigPath) {

		Properties properties = new Properties();
		try {
			properties.load(new FileReader(configPath));

			initialize(properties.getProperty("db.url1"),properties.getProperty("db.url2"), properties.getProperty("db.username"),
					properties.getProperty("db.password"), dbConfigPath);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void initialize(String master,String slave, String username, String password, String dbConfigPath) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String strJson = FileUtil.ReadFileToString(dbConfigPath);
			JSONObject jsonObject = JSONObject.parseObject(strJson);
			JSONArray artJson = jsonObject.getJSONArray("db");
			int len = artJson.size();
			for (int i = 0; i < len; i++) {
				
				JSONObject jo = artJson.getJSONObject(i);
				String dbName = jo.getString("name");
				int maxActive = Integer.parseInt(jo.getString("maxActive"));
				int initialSize = Integer.parseInt(jo.getString("initialSize"));
				int minIdle = Integer.parseInt(jo.getString("minIdle"));

				// 主库
				DruidDataSource druidDataSourceMaster = new DruidDataSource();
				druidDataSourceMaster.setMaxActive(maxActive);
				druidDataSourceMaster.setMinIdle(minIdle);
				druidDataSourceMaster.setInitialSize(initialSize);
				druidDataSourceMaster.setPoolPreparedStatements(true);
				druidDataSourceMaster.setRemoveAbandoned(true);
				String validationQuery = "select 1 from zhu_config.config_giftlist limit 1";
				druidDataSourceMaster.setValidationQuery(validationQuery);
				druidDataSourceMaster.setTestWhileIdle(true);
				druidDataSourceMaster.setTestOnBorrow(true);
				druidDataSourceMaster.setTestOnReturn(true);
				druidDataSourceMaster.setLogAbandoned(true);
				int removeAbandonedTimeout = 10;
				druidDataSourceMaster.setRemoveAbandonedTimeout(removeAbandonedTimeout);
				druidDataSourceMaster.setMinEvictableIdleTimeMillis(30000);
				druidDataSourceMaster.setMaxWaitThreadCount(1000);
				druidDataSourceMaster.setDriverClassName("com.mysql.jdbc.Driver");
				druidDataSourceMaster.setUrl(master + dbName + "?useUnicode=true&characterEncoding=UTF-8");
				druidDataSourceMaster.setUsername(username);
				druidDataSourceMaster.setPassword(password);
				dbMaster.put(dbName, druidDataSourceMaster);
				
				// 从库
				DruidDataSource druidDataSourceSlave = new DruidDataSource();
				druidDataSourceSlave.setMaxActive(maxActive);
				druidDataSourceSlave.setMinIdle(minIdle);
				druidDataSourceSlave.setInitialSize(initialSize);
				druidDataSourceSlave.setPoolPreparedStatements(true);
				druidDataSourceSlave.setRemoveAbandoned(true);
				druidDataSourceSlave.setValidationQuery(validationQuery);
				druidDataSourceSlave.setTestWhileIdle(true);
				druidDataSourceSlave.setTestOnBorrow(true);
				druidDataSourceSlave.setTestOnReturn(true);
				druidDataSourceSlave.setLogAbandoned(true);
				druidDataSourceSlave.setRemoveAbandonedTimeout(removeAbandonedTimeout);
				druidDataSourceSlave.setMinEvictableIdleTimeMillis(30000);
				druidDataSourceSlave.setMaxWaitThreadCount(1000);
				druidDataSourceSlave.setDriverClassName("com.mysql.jdbc.Driver");
				druidDataSourceSlave.setUrl(slave + dbName + "?useUnicode=true&characterEncoding=UTF-8");
				druidDataSourceSlave.setUsername(username);
				druidDataSourceSlave.setPassword(password);
				dbSlave.put(dbName, druidDataSourceSlave);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 销毁
	 */
	public void destroy() {
		for (DruidDataSource druidDataSource : dbMaster.values()) {
			if (druidDataSource != null) {
				druidDataSource.close();
			}
		}
		for (DruidDataSource druidDataSource : dbSlave.values()) {
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
