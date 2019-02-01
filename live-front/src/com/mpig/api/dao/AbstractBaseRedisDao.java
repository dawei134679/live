package com.mpig.api.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;


/**
 * 所有RedisDAO父类
 * 
 * @author weber
 *
 * @param <K>
 * @param <V>
 */
public abstract class AbstractBaseRedisDao<K, V> {
	@Autowired
	protected RedisTemplate<K, V> redisTemplate;
	
	/**
	 * 设置redisTemplate
	 * 
	 * @param redisTemplate
	 *            the redisTemplate to set
	 */
	public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
		this.redisTemplate = redisTemplate;
		
		
	}

	/**
	 * 获取 RedisSerializer <br>
	 * ------------------------------<br>
	 */
	protected RedisSerializer<String> getRedisSerializer() {
		return redisTemplate.getStringSerializer();
	}
	
//	private class RedisRouter{
//		//single
//		
//		public RedisTemplate<K, V> getRedisTemplate(final RedisConnectionFactory factory){
//			RedisTemplate<K, V> redisTemplate = new RedisTemplate<K, V>();
//			redisTemplate.setConnectionFactory(factory);
//			return redisTemplate;
//		}
//	}
}
