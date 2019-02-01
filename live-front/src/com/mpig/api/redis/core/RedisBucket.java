package com.mpig.api.redis.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mpig.api.utils.FileUtils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

public class RedisBucket {
	private final static RedisBucket instance = new RedisBucket();

	public static RedisBucket getInstance() {
		return instance;
	}

	/**
	 * 分片的redis池
	 */
	private final Map<String, ShardedJedisPool> pools = new HashMap<String, ShardedJedisPool>();
	
	/**
	 * 固定分片的redis池
	 */
	private final Map<String,JedisPool> noShardspools = new HashMap<String,JedisPool>();

	public ShardedJedisPool getShardPool(String name) {
		return pools.get(name);
	}
	
	public JedisPool getNoShardPool(String name) {
		return noShardspools.get(name);
	}

	public void initialize(String configPath) {		
		String strJson = FileUtils.ReadFileToString(configPath);
		JSONObject jsonObject = JSONObject.parseObject(strJson);		
		
		JSONArray artJson = jsonObject.getJSONArray("redis");
		int len = artJson.size();
		if (len >= 0) {
			for (int i = 0; i < len; i++) {
				JSONObject jo = artJson.getJSONObject(i);
				String shard = jo.getString("shard");
				
				int maxTotal = Integer.parseInt(jo.getString("maxTotal"));
				int maxIdle = Integer.parseInt(jo.getString("maxIdle"));
				int minIdle = Integer.parseInt(jo.getString("minIdle"));
				
				JedisPoolConfig poolConfig = new JedisPoolConfig();
				poolConfig.setMaxTotal(maxTotal);
				poolConfig.setMaxIdle(maxIdle);
				poolConfig.setMinIdle(minIdle);
				poolConfig.setTestWhileIdle(true);
				poolConfig.setTestOnBorrow(true);
				poolConfig.setTestOnReturn(true);
//				poolConfig.setMinEvictableIdleTimeMillis(1800000);
				poolConfig.setBlockWhenExhausted(false);
//				poolConfig.setMaxWaitMillis(5000);
				poolConfig.setMinEvictableIdleTimeMillis(60000);
				
				String auth = jo.getString("auth");
				
				if(shard.equalsIgnoreCase("on")){
					JedisShardInfo redisShard = new JedisShardInfo(jo.getString("host"),Integer.parseInt(jo.getString("port")));
					if(!auth.isEmpty()){
						redisShard.setPassword(auth);
					}
					redisShard.setConnectionTimeout(10000);
					pools.put(jo.getString("name"), new ShardedJedisPool(poolConfig, Arrays.asList(redisShard)));
				}
				else{
					noShardspools.put(jo.getString("name"), new JedisPool(poolConfig, jo.getString("host"),Integer.parseInt(jo.getString("port")),10000,auth));
				}
			}
		}
	}
	
	/**
	 * 销毁
	 * @param configPath
	 */
	public void destroy(){
		for(ShardedJedisPool shardedJedisPool:pools.values()){
			if (shardedJedisPool != null) {
				shardedJedisPool.destroy();
			}
		}
	}
}
