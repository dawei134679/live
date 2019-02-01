package com.tinypig.newadmin.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.tinypig.newadmin.web.entity.GameCar;
import com.tinypig.newadmin.web.service.GameCarService;

@Controller
@RequestMapping("/gcar")
public class GameCarAction {

	@Autowired
	private GameCarService gameCarService;

	@RequestMapping(value = "/getCarList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getCarList(HttpServletRequest res) {
		String name = res.getParameter("name");
		Integer status = null;
		if (res.getParameter("status") != null) {
			status = Integer.valueOf(res.getParameter("status"));
		}
		Integer page = Integer.valueOf(res.getParameter("page"));
		Integer rows = Integer.valueOf(res.getParameter("rows"));
		List<GameCar> list = gameCarService.getCarList(name, status, page, rows);
		return JSONObject.toJSONString(list);
	}

	@RequestMapping(value = "/doValid")
	@ResponseBody
	public String doValid(Long id, Integer status, HttpServletRequest request) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Long updateUserId = Long.valueOf(adminUser == null ? 0 : adminUser.getUid());
		Long updateTime = new Date().getTime();
		Map<String, Object> map = gameCarService.doValid(id, status, updateUserId, updateTime);
		return JSONObject.toJSONString(map);
	}

	@RequestMapping(value = "/saveCar", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveCar(@RequestParam(value = "img", required = false) MultipartFile img,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GameCar gameCar = new GameCar();
		
		gameCar.setName(request.getParameter("name"));
		gameCar.setProbability1(Double.parseDouble(request.getParameter("probability1")));
		gameCar.setMultiple1(Double.parseDouble(request.getParameter("multiple1")));
		gameCar.setProbability2(Double.parseDouble(request.getParameter("probability2")));
		gameCar.setMultiple2(Double.parseDouble(request.getParameter("multiple2")));
		gameCar.setProbability3(Double.parseDouble(request.getParameter("probability3")));
		gameCar.setMultiple3(Double.parseDouble(request.getParameter("multiple3")));
		gameCar.setSort(Integer.parseInt(request.getParameter("sort")));
		gameCar.setCreateTime(new Date().getTime());
		
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		gameCar.setCreateUserId(Long.valueOf(adminUser == null ? 0 : adminUser.getUid()));
		String imgUrl = QiniuUpUtils.uploadfile(img, Constant.qn_default_bucket, Constant.qn_default_bucket_domain);
		gameCar.setImg(imgUrl);
		if ((Double) gameCarService.getSumProbability(0L).get("sp") + gameCar.getProbability1() <= 1) {
			resultMap = gameCarService.saveCar(gameCar);
			resultMap.put(ConstantsAction.RESULT, true);
			resultMap.put(ConstantsAction.MSG, "保存成功");
		} else {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "所有启用的水果中奖概率之和不能大于1!");
		}
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/updateCar", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateCar(@RequestParam(value = "img", required = false) MultipartFile img,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GameCar gameCar = new GameCar();
		gameCar.setId((Long.valueOf(request.getParameter("id"))));
		gameCar.setName(request.getParameter("name"));
		gameCar.setProbability1(Double.parseDouble(request.getParameter("probability1")));
		gameCar.setMultiple1(Double.parseDouble(request.getParameter("multiple1")));
		gameCar.setProbability2(Double.parseDouble(request.getParameter("probability2")));
		gameCar.setMultiple2(Double.parseDouble(request.getParameter("multiple2")));
		gameCar.setProbability3(Double.parseDouble(request.getParameter("probability3")));
		gameCar.setMultiple3(Double.parseDouble(request.getParameter("multiple3")));
		gameCar.setSort(Integer.parseInt(request.getParameter("sort")));
		gameCar.setUpdateTime(new Date().getTime());
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		gameCar.setUpdateUserId(Long.valueOf(adminUser == null ? 0 : adminUser.getUid()));
		if (img != null && !img.isEmpty()) {
			String imgUrl = QiniuUpUtils.uploadfile(img, Constant.qn_default_bucket, Constant.qn_default_bucket_domain);
			gameCar.setImg(imgUrl);
		}
		if ((Double) gameCarService.getSumProbability(gameCar.getId()).get("sp") + gameCar.getProbability1() <= 1) {
			resultMap = gameCarService.updateCar(gameCar);
			resultMap.put(ConstantsAction.RESULT, true);
			resultMap.put(ConstantsAction.MSG, "修改成功");
		} else {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "所有启用的水果中奖概率之和不能大于1!");
		}
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/reGameCarConfigRedis")
	@ResponseBody
	public String reGameCarConfigRedis() {
		Map<String, Object> map = gameCarService.reGameCarConfigRedis();
		return JSONObject.toJSONString(map);
	}
}
