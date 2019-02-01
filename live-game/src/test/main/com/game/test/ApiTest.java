package com.game.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.hkzb.game.common.utils.AESCipher;
import com.hkzb.game.common.utils.EncryptTokenUtils;
import com.hkzb.game.common.utils.MD5;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class ApiTest {

	@Test
	public void test_getGameCarConfig() throws Exception {
		String baseUrl = "http://192.168.20.251:8081/game";
		String url = "/gameCar/getGameCarConfig";
		String tokenStr = "10000034_" + System.currentTimeMillis() / 1000;

		String token = EncryptTokenUtils.encode(tokenStr);
		String timestamp = String.valueOf(System.currentTimeMillis());
		String aesToken = AESCipher.aesEncryptString(token + "_" + MD5.MD(timestamp));
		String sign = MD5.MD(token + url + timestamp);

		Map<String, String> headers = new HashMap<>();
		headers.put("token", aesToken);
		headers.put("sign", sign);
		headers.put("timestamp", timestamp);

		Map<String, Object> params = new HashMap<>();
		
		HttpResponse<String> result = Unirest.get(baseUrl+url).headers(headers).queryString(params).asString();
		System.out.println(result.getBody());
	}
}
