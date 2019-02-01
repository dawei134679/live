package com.mpig.api.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.mpig.api.dao.AbstractBaseRedisDao;
import com.mpig.api.dao.IRedisDAO;
import com.mpig.api.model.DemoModel;

public class DemoDAO extends AbstractBaseRedisDao<String, DemoModel> implements IRedisDAO<DemoModel> {

	@Override
	public boolean add(final DemoModel demoModel,final String strKey) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(String.valueOf(demoModel.getAid()));
				byte[] name = serializer.serialize(String.valueOf(demoModel.getAname()));
				return connection.setNX(key, name);
			}
		});
		return result;
	}

	@Override
	public boolean add(final List<DemoModel> list) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				for (DemoModel demo : list) {
					byte[] key = serializer.serialize(String.valueOf(demo.getAid()));
					byte[] name = serializer.serialize(demo.getAname());
					connection.setNX(key, name);
				}
				return true;
			}
		}, false, true);
		return result;
	}

	@Override
	public boolean add(final String strKey, final String strVal) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(strKey);
				byte[] name = serializer.serialize(strVal);
				connection.setNX(key, name);

				return true;
			}
		}, false, true);
		return result;
	}

	@Override
	public void delete(String key) {
		List<String> list = new ArrayList<String>();  
        list.add(key);  
        delete(list);  
	}

	@Override
	public void delete(List<String> keys) {
		redisTemplate.delete(keys);  
	}

	@Override
	public boolean update(final DemoModel demoModel) {
		 String key = demoModel.getAid() + "";
	        if (get(key,"str") == null) {  
	            throw new NullPointerException("数据行不存在, key = " + key);  
	        }  
	        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
	            public Boolean doInRedis(RedisConnection connection)  
	                    throws DataAccessException {  
	                RedisSerializer<String> serializer = getRedisSerializer();  
	                byte[] key  = serializer.serialize(demoModel.getAid().toString());  
	                byte[] name = serializer.serialize(demoModel.getAname());  
	                connection.set(key, name);
	                return true;  
	            }  
	        });  
	        return result;
	}

	@Override
	public Object get(final String keyId,final String type) {
		Object result = redisTemplate.execute(new RedisCallback<Object>() {  
            public Object doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                RedisSerializer<String> serializer = getRedisSerializer();  
                byte[] key = serializer.serialize(keyId);  
                byte[] value = connection.get(key);  
                if (value == null) {  
                    return null;  
                }
                String val = serializer.deserialize(value);
                if (type == "str") {
					//返回字符串
                	return val;
				}else {
					//返回对象
					return null;
				}
            }  
        });  
        return result;
	}

}
