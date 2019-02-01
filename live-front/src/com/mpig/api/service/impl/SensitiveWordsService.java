package com.mpig.api.service.impl;

import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpig.api.utils.SensitivewordFilter;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 敏感词服务
 */
public class SensitiveWordsService{
	protected ShardedJedisPool jedisPool;

	public void setJedisPool(ShardedJedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	public final static String SignString = "  ˉˇ¨‘’々～‖∶”’‘｜〃〔〕《》「」『』．`·，,〖〗【【】（）〔〕｛｝[]()~!！@#$￥、%…^&*-+<>《》:：\"“”.。?？≈≡≠＝≤≥＜＞≮≯∷±＋－×÷／∫∮∝∞∧∨∑∏∪∩∈∵∴⊥‖∠⌒⊙≌∽√°′〃＄￡￥‰％℃¤￠";
	private final static SensitiveWordsService inst = new SensitiveWordsService();
	private final static String WORDS_SENSITIVE_QUEUE = "words_sensitive_queue";
	private final static Logger LOG = LoggerFactory.getLogger(SensitiveWordsService.class);
	
	public static SensitiveWordsService getInstance() {
		return inst;
	}

	/**
	 * 装载敏感词库
	 * @param fileName
	 * @return
	 */
	public void loadWordsConfig(){
		ShardedJedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> sensitiveSet = jedis.smembers(WORDS_SENSITIVE_QUEUE);
			SensitivewordFilter filter = SensitivewordFilter.getSensitivewordFilter();
			filter.addSensitiveWordToHashMap(sensitiveSet);
		} catch(Exception e){
			LOG.error(e.getMessage(),e);
		}
		finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	public String replaceSensitiveWords(String message, String content) {
		message = message.toLowerCase();
		String SignStr = SensitiveWordsService.SignString;
		int strLen = SignStr.length();
		for (int k = 0; k < strLen; k++) {
			String str = SignStr.substring(k, k + 1);
			message = message.replace(str, "");
		}
		boolean flag = SensitivewordFilter.getSensitivewordFilter().isContaintSensitiveWord(message, 1);
		if(flag) {
			return "failed";
		}
		return content;
	}
	
	public static String escapeHtml(String replaceString) {
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			String regEx_html = "<[^>]+>";
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(replaceString);
			replaceString = m_html.replaceAll("");
		} catch (Exception e) {

		}
		return replaceString;
	}
}
