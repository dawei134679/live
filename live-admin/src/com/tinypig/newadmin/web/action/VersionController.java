package com.tinypig.newadmin.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.web.entity.IosVersion;
import com.tinypig.newadmin.web.service.IIosVersionService;

@Controller
@RequestMapping("/version")
public class VersionController {

	@Autowired
	private IIosVersionService iosVersionService;

	@RequestMapping(value = "/getVersionList")
	@ResponseBody
	public String getVersion(HttpServletRequest request, HttpServletResponse response) {
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		List<IosVersion> list = iosVersionService.getIosVersionList(page, rows);
		return JSONObject.toJSONString(list);
	}

	@RequestMapping(value = "/saveIosVersion", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveiosVersion(HttpServletRequest request, IosVersion iosVersion) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int row = iosVersionService.saveIosVersion(iosVersion);
		if (row == 1) {
			resultMap.put("code", 200);
			resultMap.put("msg", "保存成功");
		} else {
			resultMap.put("code", 201);
			resultMap.put("msg", "保存失败");
		}
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/updateIosVersion", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateiosVersion(HttpServletRequest request, IosVersion iosVersion, String oldVersion) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int row = iosVersionService.updateIosVersion(iosVersion);
		if (row == 1) {
			if (!StringUtils.equals(oldVersion, iosVersion.getVersion())) {
				RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379,
						RedisContant.verifyIos + ":" + oldVersion);
			}
			resultMap.put("code", 200);
			resultMap.put("msg", "修改成功");
		} else {
			resultMap.put("code", 201);
			resultMap.put("msg", "修改失败");
		}
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/delIosVersion", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delIosVersion(HttpServletRequest request, Long id, String version) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int row = iosVersionService.delIosVersion(id);
		if (row == 1) {
			RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379,
					RedisContant.verifyIos + ":" + version);
			resultMap.put("code", 200);
			resultMap.put("msg", "删除成功");
		} else {
			resultMap.put("code", 201);
			resultMap.put("msg", "删除失败");
		}
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/refreshRedisCache", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String reiosVersionRedis() {
		Map<String, Object> map = iosVersionService.refreshRedisCache();
		return JSONObject.toJSONString(map);
	}

}
