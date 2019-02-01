package com.tinypig.newadmin.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.QiniuUpUtils;
import com.tinypig.newadmin.common.ConstantsAction;
import com.tinypig.newadmin.web.entity.GameDoll;
import com.tinypig.newadmin.web.service.GameDollService;

@Controller
@RequestMapping("ggd")
public class GameDollAction {
	
	@Autowired
	private GameDollService gameDollService;
	
	@RequestMapping(value = "/getDollList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getDollList(HttpServletRequest req) {
		String name = req.getParameter("name");
		Integer status = null;
		if (StringUtils.isNotBlank(req.getParameter("status"))) {
			status = Integer.valueOf(req.getParameter("status"));
		}
		Integer page = Integer.valueOf(req.getParameter("page"));
		Integer rows = Integer.valueOf(req.getParameter("rows"));
		List<GameDoll> list = gameDollService.getDollList(name, status, page, rows);
		return JSONObject.toJSONString(list);
	}
	
	@RequestMapping(value = "/doValid")
	@ResponseBody
	public String doValid(Long id, Integer status, HttpServletRequest request) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Long updateUserId = Long.valueOf(adminUser == null ? 0 : adminUser.getUid());
		Long updateTime = new Date().getTime();
		Map<String, Object> map = gameDollService.doValid(id, status, updateUserId, updateTime);
		return JSONObject.toJSONString(map);
	}
	
	@RequestMapping(value = "/saveDoll", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveDoll(@RequestParam(value = "imageUrl", required = false) MultipartFile Image,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GameDoll gameDoll = new GameDoll();
		
		gameDoll.setName(request.getParameter("name"));
		gameDoll.setProbability(Double.parseDouble(request.getParameter("probability")));
		gameDoll.setMultiple(Double.parseDouble(request.getParameter("multiple")));
		gameDoll.setSort(Integer.parseInt(request.getParameter("sort")));
		gameDoll.setStatus(Integer.parseInt(request.getParameter("status")));
		gameDoll.setCreateTime(new Date().getTime());
		
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		gameDoll.setCreateUserId(Long.valueOf(adminUser == null ? 0 : adminUser.getUid()));
		String imageUrl = QiniuUpUtils.uploadfile(Image, Constant.qn_default_bucket, Constant.qn_default_bucket_domain);
		gameDoll.setImageUrl(imageUrl);
		
		int row = gameDollService.saveDoll(gameDoll);
		if(row==1) {
			resultMap.put(ConstantsAction.RESULT, true);
			resultMap.put(ConstantsAction.MSG, "保存成功");
		}else {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "保存失败");
		}
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value = "/updateDoll", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateDoll(@RequestParam(value = "imageUrl", required = false) MultipartFile Image,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GameDoll gameDoll = new GameDoll();
		
		gameDoll.setId(Integer.parseInt(request.getParameter("id")));
		gameDoll.setName(request.getParameter("name"));
		gameDoll.setProbability(Double.parseDouble(request.getParameter("probability")));
		gameDoll.setMultiple(Double.parseDouble(request.getParameter("multiple")));
		gameDoll.setSort(Integer.parseInt(request.getParameter("sort")));
		gameDoll.setStatus(Integer.parseInt(request.getParameter("status")));
		gameDoll.setUpdateTime(new Date().getTime());
		
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		gameDoll.setUpdateUserId(Long.valueOf(adminUser == null ? 0 : adminUser.getUid()));
		
		if(Image != null && !Image.isEmpty()) {
			String imageUrl = QiniuUpUtils.uploadfile(Image, Constant.qn_default_bucket, Constant.qn_default_bucket_domain);
			gameDoll.setImageUrl(imageUrl);
		}
		
		int row = gameDollService.updateDoll(gameDoll);
		if(row==1) {
			resultMap.put(ConstantsAction.RESULT, true);
			resultMap.put(ConstantsAction.MSG, "修改成功");
		}else {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "修改失败");
		}
		return JSONObject.toJSONString(resultMap);
	}
	@RequestMapping(value = "/reGameDollConfigRedis")
	@ResponseBody
	public String reGameDollConfigRedis() {
		Map<String, Object> map = gameDollService.reGameCarConfigRedis();
		return JSONObject.toJSONString(map);
	}
}
