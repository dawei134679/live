package com.mpig.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dictionary.lib.PayConfigLib;
import com.mpig.api.service.IPayService;
import com.mpig.api.utils.HttpUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * Created by YX on 2016/4/26.
 */
@Service
public class ApplePayServiceImpl implements IPayService {

	private static final Logger logger = Logger.getLogger(ApplePayServiceImpl.class);

	@Override
	public String buildSign(Map<String, String> param) {
		return null;
	}

	@Override
	public boolean checkNotify(HttpServletRequest req) {

		return false;
	}

	/**
	 * 
	 * @param receipt
	 * @param issandbox =1 测试 =0 正式
	 * @param type
	 *            版本号
	 * @return
	 */
	public JSONObject verify(String receipt,int issandbox) {
		try {
			
			// 先正式上验证
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("receipt-data", receipt);
			
			String url = PayConfigLib.getConfig().getApple_buy();
			String res = HttpUtil.doPostSSL(url, jsonObject);
			if (StringUtils.isEmpty(res)) {
				return null;
			}
			JSONObject job = JSON.parseObject(res);
			String states = job.getString("status");
			if (states.equals("0"))// 验证成功
			{
				job.put("pigSandbox", 0); // 0 表示正式环境
				return job;
			}else if ("21007".equals(states)) {
				// 再测试沙盒
				url = PayConfigLib.getConfig().getApple_sandbox();
				res = HttpUtil.doPostSSL(url, jsonObject);
				if (StringUtils.isEmpty(res)) {
					return null;
				}
				job = JSON.parseObject(res);
				states = job.getString("status");
				if ("0".equals(states)) {
					job.put("pigSandbox", 1); // 1 表示沙盒环境
					return job;
				}else {
					return null;
				}
			}else {
				return null;
			}
		} catch (Exception e) {
			logger.error("verify-exception:" + e.toString());
			return null;
		}
	}
	
	/**
	 *  先检验线上 验证失败后 在检验 沙盒环境
	 * @param receipt
	 * @param type
	 *            版本号
	 * @return
	 */
	public JSONObject verifyNew(String receipt,int issandbox) {
		try {
			
			// 先正式环境验证
			String url = PayConfigLib.getConfig().getApple_buy();
			String res = HttpUtil.doPostSSLNew(url, receipt);
			if (StringUtils.isEmpty(res)) {
				return null;
			}
			
			JSONObject job = JSON.parseObject(res);
			String states = job.getString("status");
			if (states.equals("0"))// 验证成功
			{
				job.put("pigSandbox", 0); // 0 表示正式环境
				return job;
			} else if (states.equals("21007")) {
				
				// 再验证沙盒环境
				url = PayConfigLib.getConfig().getApple_sandbox();
				res = HttpUtil.doPostSSLNew(url, receipt);

				if (StringUtils.isEmpty(res)) {
					return null;
				}
				
				job = JSON.parseObject(res);
				states = job.getString("status");
				if (states.equals("0"))// 验证成功
				{
					job.put("pigSandbox", 1); // 1 表示沙盒环境
					return job;
				}else{
					return null;
				}
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.error("verifyNew-exception:" + e.toString());
			return null;
		}
	}
}
