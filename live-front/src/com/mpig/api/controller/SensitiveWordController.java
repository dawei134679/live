package com.mpig.api.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.service.impl.SensitiveWordsService;
import com.mpig.api.utils.HttpKit;

@Controller
@Scope("prototype")
@RequestMapping("/sensitiveWord")
public class SensitiveWordController extends BaseController {
	private static final Logger logger = Logger.getLogger(SensitiveWordController.class);

	/**
	 * 检查敏感词
	 */
	@RequestMapping(value = "/checkText", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> checkText(HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("code", 1100);
			String d = HttpKit.readData(request);
			if (StringUtils.isNotBlank(d)) {
				JSONObject jb = JSONObject.parseObject(d);
				if (null != jb) {
					JSONObject jBData = jb.getJSONObject("data");
					String message = jBData.getString("text");
					String replaceSensitiveWords = SensitiveWordsService.getInstance().replaceSensitiveWords(message,message);
					if (!"failed".equals(replaceSensitiveWords)) {
						result.put("riskLevel", "pass");
					} else {
						result.put("riskLevel", "reject");
					}
				}else {
					logger.info("checkText转换数据为空");
				}
			}else {
				logger.info("checkText获取数据为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 201);
			result.put("riskLevel", "error");
		}
		return result;
	}
}