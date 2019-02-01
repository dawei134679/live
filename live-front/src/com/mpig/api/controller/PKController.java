package com.mpig.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.service.IPkService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.ParamHandleUtils;

@Controller
@Scope("prototype")
@RequestMapping("/pk")
public class PKController extends BaseController {
	
	@Resource
	private IPkService pkService;
	
	/**
	 * 根据UID查询可PK的用户
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "inquireInvitePk", method = RequestMethod.GET)
	public void inquireInvitePK(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","dstuid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		Integer dstuid = Integer.valueOf(req.getParameter("dstuid"));
		if(null == dstuid){
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			writeJson(resp, returnModel);
			return;			
		}
		pkService.pkInquireInvitePK(uid, dstuid, returnModel);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 主播发起或取消PK
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "prepareInvitePk", method = RequestMethod.GET)
	public void prepareInvitePK(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","dstuid","istatus")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if(!ParamHandleUtils.isInt(req, "pkTime", "penaltyTime", "istatus")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		Integer dstuid = Integer.valueOf(req.getParameter("dstuid"));
		Integer istatus = Integer.valueOf(req.getParameter("istatus"));
		Integer pkTime = 0;
		Integer penaltyTime = 0;
		if(istatus==1) {
			pkTime = Integer.valueOf(req.getParameter("pkTime"));
			if(pkTime<=0) {
				returnModel.setCode(CodeContant.ConParamTypeIsErr);
				returnModel.setMessage("pk时间不能小于或者等于0");
				writeJson(resp, returnModel);
				return;
			}
			penaltyTime= Integer.valueOf(req.getParameter("penaltyTime"));
		}
		pkService.prepareInvitePk(uid, dstuid, pkTime, penaltyTime, istatus, returnModel);
		writeJson(resp, returnModel);
	}
	
	@RequestMapping(value = "confirmInvitePk", method = RequestMethod.GET)
	public void confirmInvitePk(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","anchoruid","istatus")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if(!ParamHandleUtils.isInt(req, "pkTime", "penaltyTime", "istatus")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		int dstuid = authService.decryptToken(token, returnModel);
		if (dstuid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		Integer anchoruid = Integer.valueOf(req.getParameter("anchoruid"));
		Integer istatus = Integer.valueOf(req.getParameter("istatus"));
		
		Integer pkTime = 0;
		Integer penaltyTime = 0;
		if(istatus==1) {
			pkTime = Integer.valueOf(req.getParameter("pkTime"));
			if(pkTime<=0) {
				returnModel.setCode(CodeContant.ConParamTypeIsErr);
				returnModel.setMessage("pk时间不能小于或者等于0");
				writeJson(resp, returnModel);
				return;
			}
			penaltyTime= Integer.valueOf(req.getParameter("penaltyTime"));
		}
		
		pkService.confirmInvitePk(anchoruid, dstuid,pkTime,penaltyTime,istatus, returnModel);
		writeJson(resp, returnModel);
	}

	@RequestMapping(value = "getPKInfo", method = RequestMethod.GET)
	public void getPKInfo(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","anchoruid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		int dstuid = authService.decryptToken(token, returnModel);
		if (dstuid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		Integer anchoruid = Integer.valueOf(req.getParameter("anchoruid"));
		pkService.getPKInfo(anchoruid,returnModel);
		writeJson(resp, returnModel);
	}
	
	@RequestMapping(value = "afreshInvitePk", method = RequestMethod.GET)
	public void afreshInvitePk(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","dstuid","pkTime","penaltyTime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if(!ParamHandleUtils.isInt(req, "pkTime", "penaltyTime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		int anchoruid = authService.decryptToken(token, returnModel);
		if (anchoruid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		Integer dstuid = Integer.valueOf(req.getParameter("dstuid"));
		Integer pkTime = Integer.valueOf(req.getParameter("pkTime"));
		if(pkTime<=0) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("pk时间不能小于或者等于0");
			writeJson(resp, returnModel);
			return;
		}
		
		if(OtherRedisService.getInstance().existsPkAnchorById(anchoruid,dstuid)) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("正在连麦PK中~");
			writeJson(resp, returnModel);
			return;
		}
		
		Integer penaltyTime= Integer.valueOf(req.getParameter("penaltyTime"));
		pkService.startPK(anchoruid, dstuid, pkTime, penaltyTime, returnModel);
		writeJson(resp, returnModel);
	}
}
