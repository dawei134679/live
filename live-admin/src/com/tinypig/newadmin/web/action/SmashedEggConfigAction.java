package com.tinypig.newadmin.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.newadmin.common.ConstantsAction;
import com.tinypig.newadmin.web.service.ISmashedEggConfigService;

@Controller
@RequestMapping("/smashedEggConfig")
public class SmashedEggConfigAction {

	@Autowired
	private ISmashedEggConfigService smashedEggConfigService;

	@RequestMapping(value = "/reSmashedEggConfigRedis", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String reSmashedEggConfigRedis() {
		Map<String, Object> map = smashedEggConfigService.reSmashedEggConfigRedis();
		return JSONObject.toJSONString(map);
	}

	@RequestMapping(value = "/getSmashedEggConfig", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getSmashedEggConfig() {
		List<Map<String, Object>> list = smashedEggConfigService.getSmashedEggMoneyConfig();
		return JSONObject.toJSONString(list);
	}

	@RequestMapping(value = "/saveSmashedEggConfig", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveSmashedEggConfig(HttpServletRequest request, Long id, Long smashed1money, Long smashed2money,
			Long smashed3money) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int row = smashedEggConfigService.saveSmashedEggConfig(id, smashed1money, smashed2money, smashed3money);
		if (row == 1) {
			resultMap.put(ConstantsAction.RESULT, true);
			resultMap.put(ConstantsAction.MSG, "保存成功");
		} else {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "保存失败");
		}
		return JSONObject.toJSONString(resultMap);
	}
}
