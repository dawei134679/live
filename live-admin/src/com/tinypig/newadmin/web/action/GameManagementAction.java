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
import com.tinypig.newadmin.web.entity.RoomGameManagement;
import com.tinypig.newadmin.web.service.GameManagementService;

@Controller
@RequestMapping("gmgt")
public class GameManagementAction {
	
	@Autowired
	private GameManagementService gameManagementService;
	
	@RequestMapping(value = "/getGameList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getGameList(HttpServletRequest res) {
		String name = res.getParameter("name");
		Integer status = null;
		if (res.getParameter("status") != null) {
			status = Integer.valueOf(res.getParameter("status"));
		}
		Integer page = Integer.valueOf(res.getParameter("page"));
		Integer rows = Integer.valueOf(res.getParameter("rows"));
		List<RoomGameManagement> list = gameManagementService.getRoomGameList(name, status, page, rows);
		return JSONObject.toJSONString(list);
	}
	
	@RequestMapping(value = "/saveRoomGame",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveRoomGame(@RequestParam(value = "img", required = false) MultipartFile img,
			@RequestParam(value = "gameIconUrl", required = false) MultipartFile gameIcon,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		RoomGameManagement roomGameManagement = new RoomGameManagement();
		roomGameManagement.setName(request.getParameter("name"));
		roomGameManagement.setGameKey(request.getParameter("gameKey"));
		roomGameManagement.setType(Integer.parseInt(request.getParameter("type")));
		roomGameManagement.setStatus(Integer.parseInt(request.getParameter("status")));
		roomGameManagement.setInitUrl(request.getParameter("initUrl"));
		roomGameManagement.setDestoryUrl(request.getParameter("destoryUrl"));
		roomGameManagement.setServerUrl(request.getParameter("serverUrl"));
		roomGameManagement.setPageUrl(request.getParameter("pageUrl"));
		//roomGameManagement.setGameCommission(Double.parseDouble(request.getParameter("gameCommission")));
		roomGameManagement.setCreateTime(new Date().getTime());
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		roomGameManagement.setCreateUserId(Long.valueOf(adminUser==null?0:adminUser.getUid()));
		String imgUrl = QiniuUpUtils.uploadfile(img,Constant.qn_default_bucket,Constant.qn_default_bucket_domain);
		String gameIconUrl = QiniuUpUtils.uploadfile(gameIcon,Constant.qn_default_bucket,Constant.qn_default_bucket_domain);
		roomGameManagement.setImgUrl(imgUrl);
		roomGameManagement.setGameIconUrl(gameIconUrl);
		int row = gameManagementService.saveRoomGame(roomGameManagement);
		if(row==1){
			resultMap.put(ConstantsAction.RESULT, true);
			resultMap.put(ConstantsAction.MSG, "保存成功");
		}else {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "保存失败");
		}
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value = "/doValid")
	@ResponseBody
	public String doValid(Long id, Integer status,HttpServletRequest request) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Long updateUserId = Long.valueOf(adminUser==null?0:adminUser.getUid());
		Long updateTime = new Date().getTime();
		Map<String, Object> map = gameManagementService.doValid(id, status, updateUserId, updateTime);
		return JSONObject.toJSONString(map);
	}
	
	@RequestMapping(value = "/updateRoomGame",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateCar(@RequestParam(value = "img", required = false) MultipartFile img,
							@RequestParam(value = "gameIconUrl", required = false) MultipartFile gameIcon,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		RoomGameManagement roomGameManagement = new RoomGameManagement();
		roomGameManagement.setId(Long.parseLong(request.getParameter("id")));
		roomGameManagement.setGameKey(request.getParameter("gameKey"));
		roomGameManagement.setName(request.getParameter("name"));
		roomGameManagement.setType(Integer.parseInt(request.getParameter("type")));
		roomGameManagement.setStatus(Integer.parseInt(request.getParameter("status")));
		roomGameManagement.setInitUrl(request.getParameter("initUrl"));
		roomGameManagement.setDestoryUrl(request.getParameter("destoryUrl"));
		roomGameManagement.setServerUrl(request.getParameter("serverUrl"));
		roomGameManagement.setPageUrl(request.getParameter("pageUrl"));
		roomGameManagement.setUpdateTime(new Date().getTime());
		//roomGameManagement.setGameCommission(Double.parseDouble(request.getParameter("gameCommission")));
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		roomGameManagement.setUpdateUserId(Long.valueOf(adminUser==null?0:adminUser.getUid()));
		if (img != null && !img.isEmpty()) {
			String imgUrl = QiniuUpUtils.uploadfile(img,Constant.qn_default_bucket,Constant.qn_default_bucket_domain);
			roomGameManagement.setImgUrl(imgUrl);
		}
		if (gameIcon != null && !gameIcon.isEmpty()) {
			String gameIconUrl = QiniuUpUtils.uploadfile(gameIcon,Constant.qn_default_bucket,Constant.qn_default_bucket_domain);
			roomGameManagement.setGameIconUrl(gameIconUrl);
		}
		int row = gameManagementService.updateRoomGame(roomGameManagement);
		if(row==1){
			resultMap.put(ConstantsAction.RESULT, true);
			resultMap.put(ConstantsAction.MSG, "修改成功");
		}else {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "修改失败");
		}
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value = "/reRoomGameRedis")
	@ResponseBody
	public String reRoomGameRedis() {
		Map<String, Object> map = gameManagementService.reRoomGameRedis();
		return JSONObject.toJSONString(map);
	}
}