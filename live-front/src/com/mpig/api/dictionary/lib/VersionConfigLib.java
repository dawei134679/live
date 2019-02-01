package com.mpig.api.dictionary.lib;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.mpig.api.dictionary.VersionConfig;
import com.mpig.api.redis.core.RedisBucket;
import com.mpig.api.utils.RedisContant;

/**
 * 版本配置
 * 
 * @ClassName: VersionConfigLib.java
 * @Description: TODO
 * 
 * @author jackzhang
 * @version V1.0
 * @Date May 25, 2016 11:02:14 AM
 */
public class VersionConfigLib {
	private static final Logger logger = Logger.getLogger(VersionConfigLib.class);
	private static final Map<String, VersionConfig> pool = new HashMap<String, VersionConfig>();

	public static String readFromRedis() {
		ShardedJedis redis = null;
		try {
			ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			if (redis != null) {
				String versionAndroid = redis.get(RedisContant.VersionAndroid);
				if (versionAndroid != null) {
					JSONObject versionAndroidJson = new JSONObject(versionAndroid);
					pool.put("android", new VersionConfig().initWith(versionAndroidJson.getString("ver"),
							versionAndroidJson.getString("desc"), versionAndroidJson.getBoolean("force"), versionAndroidJson.getString("uploadUrl"), 0));
				}

				String verisonIos = redis.get(RedisContant.VersionIos);
				if (verisonIos != null) {
					JSONObject versionIosJson = new JSONObject(verisonIos);
					pool.put("ios", new VersionConfig().initWith(versionIosJson.getString("ver"),
							versionIosJson.getString("desc"), versionIosJson.getBoolean("force"), versionIosJson.getString("uploadUrl"), 0));
				}
			} else {
				logger.info("readFromRedis 没有新新版本");
			}
		} catch (Exception ex) {
			logger.error("readFromRedis exception:", ex);
			return "err";
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return "ok";
	}

	public static VersionConfig getConfig(final String os) {
		return pool.get(os);
	}
}
