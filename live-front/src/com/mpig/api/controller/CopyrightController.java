package com.mpig.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpig.api.model.ReturnModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.RedisContant;

@Controller
@Scope("prototype")
@RequestMapping("/copyright")
public class CopyrightController  extends BaseController {

	private static Logger log = Logger.getLogger(CopyrightController.class);
	
	@RequestMapping("/getCopyrightInfo")
	@ResponseBody
	public ReturnModel getCopyrightInfo() {
		try {
			 String result = OtherRedisService.getInstance().get(RedisContant.RedisNameOther, RedisContant.sysCopyright);
			 Map<String,String> map = new HashMap<String,String>();
			 map.put("licCode", result);
			 returnModel.setData(map);
			 return returnModel;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("系统异常");
			return returnModel;
		}
	}
}
