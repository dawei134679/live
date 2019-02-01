package com.mpig.api.reapal;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mpig.api.utils.Constant;

public class DecipherH5 {
	private static Logger log = Logger.getLogger(DecipherH5.class);

	/**
	 * 解密
	 * 
	 * @param merchant_id
	 * @param data
	 * @param encryptkey
	 * @return
	 * @throws com.reapal.common.exception.ServiceException
	 */
	public static String decryptData(String post) throws Exception {

		// 将返回的json串转换为map

		TreeMap<String, String> map = JSON.parseObject(post, new TypeReference<TreeMap<String, String>>() {
		});
		String encryptkey = map.get("encryptkey");
		String data = map.get("data");

		// 获取自己私钥解密
		PrivateKey pvkformPfx = RSA.getPvkformPfx(Constant.reapal_quick_payment_privateKey,
				Constant.reapal_quick_payment_password);
		String decryptData = RSA.decrypt(encryptkey, pvkformPfx);

		post = AES.decryptFromBase64(data, decryptData);

		return post;
	}

	/**
	 * 加密
	 * 
	 * @param merchant_id
	 * @param data
	 * @param encryptkey
	 * @return
	 * @throws com.reapal.common.exception.ServiceException
	 */
	public static Map<String, String> encryptData(String json) throws Exception {
		log.debug("加密json数据=============>" + json);

		// 商户获取融宝公钥
		PublicKey pubKeyFromCrt = RSA.getPubKeyFromCRT(Constant.reapal_quick_payment_pubKeyUrl);
		// 随机生成16数字
		String key = getRandom(16);

		// 使用RSA算法将商户自己随机生成的AESkey加密
		String encryptKey = RSA.encrypt(key, pubKeyFromCrt);
		// 使用AES算法用随机生成的AESkey，对json串进行加密
		String encryData = AES.encryptToBase64(json, key);

		log.debug("加密，密文key============>" + encryptKey);
		log.debug("加密，密文数据===========>" + encryData);

		Map<String, String> map = new HashMap<String, String>();
		map.put("data", encryData);
		map.put("encryptkey", encryptKey);

		return map;
	}

	public static Random random = new Random();

	public static String getRandom(int length) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < length; i++) {
			boolean isChar = (random.nextInt(2) % 2 == 0);// 输出字母还是数字
			if (isChar) { // 字符串
				int choice = (random.nextInt(2) % 2 == 0) ? 65 : 97; // 取得大写字母还是小写字母
				ret.append((char) (choice + random.nextInt(26)));
			} else { // 数字
				ret.append(Integer.toString(random.nextInt(10)));
			}
		}
		return ret.toString();
	}

	/**
	 * 解密
	 * 
	 * @param merchant_id
	 * @param data
	 * @param encryptkey
	 * @return
	 * @throws com.reapal.common.exception.ServiceException
	 */
	public static String decryptData(String encryptkey, String data) throws Exception {

		// 获取自己私钥解密
		PrivateKey pvkformPfx = RSA.getPvkformPfx(Constant.reapal_quick_payment_privateKey,
				Constant.reapal_quick_payment_password);
		String decryptData = RSA.decrypt(encryptkey, pvkformPfx);

		return AES.decryptFromBase64(data, decryptData);

	}

}
