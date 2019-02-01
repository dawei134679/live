package com.mpig.api.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mpig.api.dictionary.lib.TaskConfigLib;
import com.mpig.api.model.TaskConfigModel;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dictionary.lib.TaskConfigLib.TaskFor;
import com.mpig.api.task.TaskService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.ParamHandleUtils;

@Controller
@Scope("prototype")
@RequestMapping("/task")
public class TaskController extends BaseController {
	private static final Logger logger = Logger.getLogger(TaskController.class);

	/**
	 * 获取用户签到摘要信息
	 * 接口参数:"ncode", "os", "imei", "reqtime", "token","ts"(当前时间戳)
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/sign_summary", method = RequestMethod.GET)
	public void signSummary(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","ts")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","ts")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		long ts = Long.parseLong(req.getParameter("ts"));
		Map<String, Object> map = TaskService.getInstance().getSignSummaryInfo(String.valueOf(uid),ts);
		if (map.containsKey("code")) {
			returnModel.setCode(Integer.parseInt(map.get("code").toString()));
			returnModel.setMessage(map.get("message").toString());
		} else {
			returnModel.setData(map);
		}
		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取用户签到摘要信息
	 * 接口参数：无
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/sign_rule", method = RequestMethod.GET)
	public void signSummaryAnyOne(HttpServletRequest req, HttpServletResponse resp) {
		returnModel.setData(TaskService.getInstance().getSignSummaryInfoForAnyOne());
		writeJson(resp, returnModel);
	}
	
	/**
	 * 提交签到 领取奖励
	 * 接口参数："ncode", "os", "imei", "reqtime", "token"
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/sign_take", method = RequestMethod.GET)
	public void signTake(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		Map<String, Object> map = TaskService.getInstance().getSignTake(String.valueOf(uid));
		if (map.containsKey("code")) {
			returnModel.setCode(Integer.parseInt(map.get("code").toString()));
			returnModel.setMessage(map.get("message").toString());
		} else {
			returnModel.setData(map);
		}
		writeJson(resp, returnModel);
	}
	

	/**
	 * tosy
	 * 补签 领取奖励
	 * 接口参数："ncode", "os", "imei", "reqtime", "token"
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/resign_take", method = RequestMethod.GET)
	public void signReTake(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","day")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		int day = Integer.valueOf(req.getParameter("day"));
		
		Map<String, Object> map = TaskService.getInstance().getReSignTake(uid,day);
		if (map.containsKey("code")) {
			returnModel.setCode(Integer.parseInt(map.get("code").toString()));
			returnModel.setMessage(map.get("message").toString());
		} else {
			returnModel.setData(map);
		}
		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取用户任务进度基本信息
	 * 接口参数:"ncode", "os", "imei", "reqtime", "token"
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/summary", method = RequestMethod.GET)
	public void summary(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		//获取任务相关的摘要信息
		Map<String, Object> map = TaskService.getInstance().getSummaryInfo(uid);
		if (map == null) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("该用户异常，请重新登录");
		} else {
			returnModel.setData(map);
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 获取用户任务列表
	 * 接口参数列表："ncode", "os", "imei", "reqtime", "token","type"
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void list(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		String typeStringfy = req.getParameter("type");
		TaskFor typeFor = Integer.parseInt(typeStringfy)==0?TaskFor.Newbie:TaskFor.Daily;
		Map<String, List<JSONObject>> taskAllList = TaskService.getInstance().taskAllList(uid,typeFor);
		returnModel.setData(taskAllList);
		writeJson(resp, returnModel);
	}

	/**
	 * 游客获取用户任务列表
	 * 接口参数列表："type"
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/listForGuest", method = RequestMethod.GET)
	public void listForAnonymous(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req,"type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}

		String typeStringfy = req.getParameter("type");
		TaskFor typeFor = Integer.parseInt(typeStringfy)==0?TaskFor.Newbie:TaskFor.Daily;
		Collection<TaskConfigModel> taskAllList = TaskConfigLib.getInstance().getTaskListFor(typeFor);
		returnModel.setData(taskAllList);
		writeJson(resp, returnModel);
	}

	/**
	 * 提交任务 领取奖励
	 * 接口参数列表："ncode", "os", "imei", "reqtime", "token","taskId"
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/commit", method = RequestMethod.GET)
	public void commit(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","taskId")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		String taskIdStringfy = req.getParameter("taskId");
		int taskId = Integer.parseInt(taskIdStringfy);
		int code = TaskService.getInstance().taskCommitById(String.valueOf(uid),taskId);
		returnModel.setCode(code);
		if(code == CodeContant.TaskUnFinishedOrCommited){
			returnModel.setMessage("任务已完成");
		}
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("taskId", taskId);
		//"info_changed":[{"unit":"经验值","count":2},{"unit":"金币","count":20}] ,
		returnModel.setData(data);
		writeJson(resp, returnModel);
		return;
	}
}
