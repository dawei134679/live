package com.mpig.api.utils;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

public class RandomUtil {

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    //private static final Random RANDOM = new SecureRandom();

	public static Random random = new Random();

	public static String getRandom(int length) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < length; i++) {
			boolean isChar = (random.nextInt(2) % 2 == 0);// 输出字母还是数字
			if (isChar) { // 字符串
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
				ret.append((char) (choice + random.nextInt(26)));
			} else { // 数字
				ret.append(Integer.toString(random.nextInt(10)));
			}
		}
		return ret.toString();
	}

	public static String getRandomNum(int length) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < length; i++) {
			ret.append(Integer.toString(random.nextInt(10)));
		}
		return ret.toString();
	}

	/**
	 * 获取时间戳+3位随机数
	 * 
	 * @return
	 */
	public static String getTime3Random() {
		return System.currentTimeMillis() + RandomStringUtils.randomNumeric(3);
	}

	/**
	* 获取随机字符串 Nonce Str
	*
	* @return String 随机字符串
	*/
	public static String generateNonceStr() {
		char[] nonceChars = new char[32];
		for (int index = 0; index < nonceChars.length; ++index) {
			nonceChars[index] = SYMBOLS.charAt(random.nextInt(SYMBOLS.length()));
		}
		return new String(nonceChars);
	}
}