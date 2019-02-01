package com.tinypig.newadmin.web.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.QiniuUpUtils;
import com.tinypig.newadmin.web.entity.RobotBaseInfo;
import com.tinypig.newadmin.web.service.RobotBaseInfoService;

@Controller
@RequestMapping("/roomRobot")
public class RobortBaseInfoAction {	
	

	@Autowired
	private RobotBaseInfoService robotBaseInfoService;
	
	@RequestMapping("getRoomRobotList")
	@ResponseBody
	public String getRoomRobotList(HttpServletRequest request) {
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		Map<String, Object> map = robotBaseInfoService.getRobotBaseInfoList(page, rows);
		return JSONObject.toJSONString(map);
	}
	
	@RequestMapping("reAllRobotRedis")
	@ResponseBody
	public String reAllRobotRedis() {
		Map<String, Object> map = robotBaseInfoService.reAllRobotRedis();
		return JSONObject.toJSONString(map);
	}
	@RequestMapping(value = "/updateRobot", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateRobot(@RequestParam(value = "headimage", required = false) MultipartFile img,
			HttpServletRequest request) {
		RobotBaseInfo robot = new RobotBaseInfo();
		if (img != null && !img.isEmpty()) {
			String imgUrl = QiniuUpUtils.uploadfile(img, Constant.qn_default_bucket, Constant.qn_default_bucket_domain);
			robot.setHeadimage(imgUrl);
		}
		robot.setUid(Long.parseLong(request.getParameter("uid")));
		robot.setNickname(request.getParameter("nickname"));
		robot.setAnchorlevel(Integer.parseInt(request.getParameter("anchorlevel")));
		robot.setUserlevel(Integer.parseInt(request.getParameter("userlevel")));
		robot.setSex(Integer.parseInt(request.getParameter("sex")));
		robot.setIdentity(Integer.parseInt(request.getParameter("identity")));
		robot.setProvince(request.getParameter("province"));
		robot.setCity(request.getParameter("city"));
		Map<String, Object> resultMap = robotBaseInfoService.updateRobot(robot);
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value = "/saveRobot", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveRobot(@RequestParam(value = "headimage", required = false) MultipartFile img,
			HttpServletRequest request) {
		RobotBaseInfo robot = new RobotBaseInfo();
		String imgUrl = QiniuUpUtils.uploadfile(img, Constant.qn_default_bucket, Constant.qn_default_bucket_domain);
		robot.setHeadimage(imgUrl);
		robot.setNickname(request.getParameter("nickname"));
		robot.setAnchorlevel(Integer.parseInt(request.getParameter("anchorlevel")));
		robot.setUserlevel(Integer.parseInt(request.getParameter("userlevel")));
		robot.setSex(Integer.parseInt(request.getParameter("sex")));
		robot.setIdentity(Integer.parseInt(request.getParameter("identity")));
		robot.setProvince(request.getParameter("province"));
		robot.setCity(request.getParameter("city"));
		Map<String, Object> resultMap = robotBaseInfoService.saveRobot(robot);
		return JSONObject.toJSONString(resultMap);
	}
}
	
