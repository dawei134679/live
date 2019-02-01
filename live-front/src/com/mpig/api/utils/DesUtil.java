package com.mpig.api.utils;

import java.net.URLDecoder;
import java.security.*;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DesUtil {
	/** 加密、解密key. */
	private static final String PASSWORD_CRYPT_KEY = "Ri_Ge+e[";
	/** 加密算法,可用 DES,DESede,Blowfish. */
	private final static String ALGORITHM = "DES";
	private final static String ALGORITHM_CBC = "DES/CBC/PKCS5Padding";

	public static void main(String[] args) throws Exception {
		String md5Password = "9lFO%2FXdtnHLWqYym5aRogPxuaCZLRgi%2BaFadNXUElQviJEMaENCFyPz6X5jDpwOniG6K6gHsryeVS4OGsfeSx%2B1VMFNxxXgepSNoG4UbgRCb94kEyF%2FBMV8RvlwDgx0FP7CdzD4nzsJ%2BcJykzrGGbAzLGXNJtox97an60snisEEY79ZicIL1b%2FnnwY5B2LXf7rWIaiJ7mvvQ9aqylCoYXZBTTTBo8X46ioOjt5lgUitQI8V3KEIoobiKhSyaC26WJVEesxEkybkzd86VwAyu2C%2BIWbsoMgn9cNhVuG41Bw9TcHsobxp98PnGTiqCPwfbfEEEprEpOhw%3D";
		md5Password = URLDecoder.decode(md5Password);
		System.out.println(md5Password);
//		String str = DesUtil.encrypt(md5Password);
//		System.out.println("str: " + str);
		String str = DesUtil.decrypt(md5Password);
		System.out.println("str: " + str);
	}

	/**
	 * 对数据进行DES加密.
	 * 
	 * @param data
	 *            待进行DES加密的数据
	 * @return 返回经过DES加密后的数据
	 * @throws Exception
	 */
	public final static String decrypt(String data) throws Exception {
		return new String(decrypt(new sun.misc.BASE64Decoder().decodeBuffer(data),
				PASSWORD_CRYPT_KEY.getBytes()));
	}

	/**
	 * 对用DES加密过的数据进行加密.
	 * 
	 * @param data
	 *            DES加密数据
	 * @return 返回解密后的数据
	 * @throws Exception
	 */
	public final static String encrypt(String data) throws Exception {
		return byte2hex(encrypt(data.getBytes(), PASSWORD_CRYPT_KEY.getBytes()));
	}

	/**
	 * 用指定的key对数据进行DES解密.
	 * 
	 * @param data
	 *            待加密的数据
	 * @param key
	 *            DES加密的key
	 * @return 返回DES加密后的数据
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(ALGORITHM_CBC);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(data);
	}

	/**
	 * 用指定的key对数据进行DES解密.
	 * 
	 * @param data
	 *            待解密的数据
	 * @param key
	 *            DES解密的key
	 * @return 返回DES解密后的数据
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		IvParameterSpec iv = new IvParameterSpec(key);
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(ALGORITHM_CBC);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(data);
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
	
	/**
	* 解密
	* @param src byte[]
	* @param password String
	* @return byte[]
	* @throws Exception
	*/
	public static byte[] decrypt(byte[] src, String password) throws Exception {
	// DES算法要求有一个可信任的随机数源
	SecureRandom random = new SecureRandom();
	// 创建一个DESKeySpec对象
	DESKeySpec desKey = new DESKeySpec(password.getBytes());
	// 创建一个密匙工厂
	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	// 将DESKeySpec对象转换成SecretKey对象
	SecretKey securekey = keyFactory.generateSecret(desKey);
	// Cipher对象实际完成解密操作
	Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	// 用密匙初始化Cipher对象
	cipher.init(Cipher.DECRYPT_MODE, securekey, random);
	// 真正开始解密操作
	return cipher.doFinal(src);
	}
}
