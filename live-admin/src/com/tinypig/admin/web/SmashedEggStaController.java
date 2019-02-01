package com.tinypig.admin.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.newadmin.web.entity.SmashedEggStaDto;
import com.tinypig.newadmin.web.service.ISmashedEggStaService;

/**
 * 砸蛋统计（开心敲敲乐）
 */
@Controller
@RequestMapping("/smashedEggSta")
public class SmashedEggStaController {

	@Autowired
	private ISmashedEggStaService smashedEggStaService;

	@RequestMapping(value = "/smashedEggList")
	@ResponseBody
	public String smashedEggList(HttpServletRequest req, SmashedEggStaDto smashedEggStaDto) {
		Map<String, Object> map = smashedEggStaService.smashedEggList(smashedEggStaDto);
		return JSONObject.toJSONString(map);
	}

	@RequestMapping(value = "/getSmashedEggTotal")
	@ResponseBody
	public List<Map<String, Object>> getSmashedEggTotal(SmashedEggStaDto smashedEggStaDto) {
		return smashedEggStaService.getSmashedEggTotal(smashedEggStaDto);
	}
}
