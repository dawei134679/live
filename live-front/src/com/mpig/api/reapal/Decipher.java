package com.mpig.api.reapal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mpig.api.utils.Constant;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Decipher {

	private static final String privateKey = Decipher.class.getResource("/").getPath()+"cert/itrus001.pfx";
	private static final String pubKeyUrl = Decipher.class.getResource("/").getPath()+"cert/itrus001.cer";
	/**
	 * 解密1
	 * @param merchant_id
	 * @param data
	 * @param encryptkey
	 * @return
	 * @throws
	 */
	public static String decryptData(String post) throws Exception {
		// 将返回的json串转换为map
		TreeMap<String, String> map = JSON.parseObject(post,new TypeReference<TreeMap<String, String>>() {});
		String encryptkey = map.get("encryptkey");
		String data = map.get("data");
		// 获取自己私钥解密
		PrivateKey pvkformPfx = RSA.getPvkformPfx(privateKey,Constant.reapal_certificatePwd);
		String decryptData = RSA.decrypt(encryptkey, pvkformPfx);
		post = AES.decryptFromBase64(data, decryptData);
		return post;
	}

    /**
	 * 解密2
	 * @param merchant_id
	 * @param data
	 * @param encryptkey
	 * @return
	 * @throws com.reapal.common.exception.ServiceException
	 */
	public static String decryptData(String encryptkey, String data) throws Exception {
		// 获取自己私钥解密


        PrivateKey pvkformPfx = RSA.getPvkformPfx(privateKey, Constant.reapal_certificatePwd);

		String decryptKey = RSA.decrypt(encryptkey, pvkformPfx);

		return AES.decryptFromBase64(data, decryptKey);
	}
	
	/**
	 * 加密
	 * @param merchant_id
	 * @param data
	 * @param encryptkey
	 * @return
	 * @throws com.reapal.common.exception.ServiceException
	 */
	public static Map<String, String> encryptData(String json) throws Exception {
		System.out.println("json数据=============>" + json);
		// 商户获取融宝公钥
		PublicKey pubKeyFromCrt = RSA.getPubKeyFromCRT(pubKeyUrl);
		// 随机生成16数字
		String key = getRandom(16);
		// 使用RSA算法将商户自己随机生成的AESkey加密
		String encryptKey = RSA.encrypt(key, pubKeyFromCrt);
		// 使用AES算法用随机生成的AESkey，对json串进行加密
		String encryData = AES.encryptToBase64(json, key);
		System.out.println("密文key============>" + encryptKey);
		System.out.println("密文数据===========>" + encryData);
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

	public static void main(String[] args) {
		System.out.println(Decipher.class.getResource("/").getPath()+"cert_gjy/");
		/*String encryptkey = "F3bi+tQydkwTlUO2XYMqXyq/zaDdXiiP0A5TvNdq23ADKietKxCuHfxVcD+r8MwsrptGFBlxOOC4KffTr5PXB1Fxj5GFpAJY6lo2ZpcRrirfKWd5NrX9D7jmgkqM/QVu7GkHloBJAgPV610Q+k9bFrQT+YklvftkoMFNInaeG10=";
		String data = "r8c5ZbRv+4KuWOhah4S9Muw0QKS38x36NGyfM0Ix/rGVYMVQU9k5x2x2amnKjy9w1fJcka9k0Ay1B4oNei4Dfv3lCW0Y++aLHeAyNSWDZBsny5np0+0vtEOup63QWkfx8aUKJT3EYgvHbQJFPp8B7X0QY0kFMRHhPp9pi4LBXq5oe8CjTON09M3Kuasb+u2FOWR4mDnCoGgTRA2hccFLWDIAmeMUAIfOZH2RAJAen9QljlGxk1ddJhQkdJPlbVr1/FedI8WvTHB7UGaZuFEvdCS2mEI9I0wsA2JCnP7e9bU=";
		try {
			System.out.println("解密内容为：" + decryptData("{\n" +
                    "    \"data\": \"Knk+qJWriAVrNNrMOoTGlkqYHHCsx4SM02HCqx/Q6vt6QJ6+cQ1CUK47hp0qSlFDwnrrH8CF+xf4WoW+HsMpUMuvoxl9TNkFmH5SuUFyHJxOfDKRpCnNcRtxMZyyft/ewQE6IsLz9chCkVBBVCQ8uTQcQElksMOFKHa7SVibfl0H6slq2AH+XiLuZnKnHZCIDqInTBgizvZYUbhJ/Ey0nj6iQwuPfWciHW9CbYCLKJDULIwHu5L49PRwBY209eu+W+trYCXC75k0tZLERxukDvp1lDg++j7bv7j6qagBZF8eIR71qYHEaXxCpy61R4I2U0G/7sCOYOmwPTWPn1+ReT3CpgIY00b0ICRi36HyjSMpF+UYZF4enxIJw/CdVGo1Fu4fX21f1/ApsybWGuuKnOwHtEfFKAFZXV7gchMHycaROKim5k0iYpvMgqTGLYOv8wbxJPVbJQiVWo2cZ8xoNClurQC66nr+dCXHhO1M2BqgY3eHNIB7+wo/v7+vEQ37VznMme90erIMqyx4I3t4G3UpvoxMn1S02KzfqsQs2zVJtkf0Kz0SpWR9Pcg3zvXOj6VMLRqyYXl8GeAl5Ffz12lVQHcXzdv5HIp/nPbrR9bAhyf15MhkTUssOXa7x0+7kL230Z/2fqPDP5Aa4AQXb3R7/cnr9Iv3uns/pr4uli9NTv0yiG6qG7ojXT1MrgCiYG0qj7w1IEMPgXgNwPnKTzPqYOajSar4AJN2hPt9P/v3J71OKxtArsH3I2hDkRlhnX4FaeJYttphDnQFRJn5HiM1FIOizZzYAwamGfarIYO0msuPu96oELde/zbKUbeChXsE3Q+dsYyB2GdKB4DvWQ==\",\n" +
                    "    \"encryptkey\": \"dq+kfjiYj/69DPpVnOl51d/Y2aSYiJylJYGDa1VyPxoGa5oLSmYB7RoclZnmCtlpWMuKDefDwSM3raps7FWp+ENgMJVQFPrCCJG+D5Pq6GwoKbCxthTPTTJpwpJ5+Hrhu+LFkSoaE96ES1yCNpUAYuQu6Z66PRaBI9icHZ8zSVcmeY5OWCRGBOOWE+mAitTEXuaoSk4j6fYhkBnNh5pJegHGSW72L3PEOn3531xhLufweYNExo5l4+kL7xMbCJ9DXA448CTLuMKzwu35Il/84C7KGOsE0YTHPIB/ghh+EPZEKAKCaSaP+uIRCf3XocROpe7YsxA6QhuIJCq1+LOZ9A==\",\n" +
                    "    \"merchant_id\": \"100000000011015\"\n" +
                    "}"));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
//		System.out.println("==================================");
/*
		String ss ="{"
				+"\"merchant_id\""+":"+"\"100000000075980\""+","+"\"notify_id\""
				+":"+"\"8a0ace9092bc4abeaccf2cb5afc948a4\""+","+"\"order_no\""
				+":"+"\"IGWAMOYH6P88\""+","+"\"sign\""+":"+"\"a0fcb65af9f937c4355a3330d79f4197\""
				+","+"\"status\""+":"+"\"TRADE_FINISHED\""+","+"\"total_fee\""+":"+"1"
				+","+"\"trade_no\""+":"+"\"101511125564257\""+"}" ;
			 
       //System.out.println("*****************"+ss);
		try {
			//System.out.println("加密内容为：" + encryptData(decryptData(encryptkey, data)));
			System.out.println("加密内容为：" + encryptData(ss));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
