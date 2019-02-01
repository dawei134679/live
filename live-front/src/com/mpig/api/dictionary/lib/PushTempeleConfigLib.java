package com.mpig.api.dictionary.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.mpig.api.redis.core.RedisBucket;
import com.mpig.api.utils.RedisContant;

/**
 * 推送模板池配置
 * @ClassName:     VersionConfigLib.java
 * @Description:   TODO 
 * 
 * @author         jackzhang
 * @version        V1.0  
 * @Date           May 25, 2016 11:02:14 AM
 */
public class PushTempeleConfigLib {
	private static final Logger LOG = Logger.getLogger(PushTempeleConfigLib.class);
    private static final Map<String, ArrayList<String>> pool = new HashMap<String, ArrayList<String>>();

    public static String readFromRedis() {
    	ShardedJedis redis = null;
		try {
			ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			if(redis != null){
				String templeteKey = RedisContant.PushTempleteLiveOn;
				String jsonString = redis.get(templeteKey);
				System.err.println("PushTempeleConfigLib readFromRedis>>>"+jsonString);
				if(jsonString != null){
					JSONObject object = new JSONObject(jsonString);
					JSONArray jsonArray = object.getJSONArray(templeteKey);
					int len = jsonArray.length();
					if(len > 0){
						if(!pool.containsKey(templeteKey)){
							pool.put(templeteKey, new ArrayList<String>());
						}
						
						ArrayList<String> templetes = pool.get(templeteKey);
						templetes.clear();
						for(int i = 0;i < len;i++){
							String object2 = (String)jsonArray.get(i);
							if(object2 != null){
								templetes.add(object2);
							}
						}
					}
					
				}
			}
		}catch(Exception ex){
			LOG.error(ex.toString());
			return ex.toString();
		}finally {
			if(redis != null){
				redis.close();
			}
		}
		return "ok";
    }

    public static ArrayList<String> getConfig(final String templeteKey) {
        return pool.get(templeteKey);
    }
}
