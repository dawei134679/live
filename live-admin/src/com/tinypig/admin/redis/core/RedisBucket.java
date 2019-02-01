package com.tinypig.admin.redis.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinypig.admin.util.RedisContant;

import redis.clients.jedis.Jedis;

public class RedisBucket {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisBucket.class);
			
	private final static RedisBucket instance = new RedisBucket();

	public static RedisBucket getInstance() {
		return instance;
	}

	public Jedis initialize(String host,int port) {		
		Jedis jedis = null;
		try{
			jedis = new Jedis(host,port);
			jedis.auth(RedisContant.auth);
		}catch(Exception e){
			logger.error("<initialize->Exception>" + e.toString());
		} finally {
			jedis.close();
		}
		return jedis;
	}
}
