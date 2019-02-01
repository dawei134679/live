package com.mpig.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpig.api.model.ReturnModel;
import com.mpig.api.service.IStatisticalInfoService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.ParamHandleUtils;

@Controller
@Scope("prototype")
@RequestMapping("/statistical")
public class StatisticalController extends BaseController {
	private static Logger log = Logger.getLogger(StatisticalController.class);
	
	@Resource
	private IStatisticalInfoService statisticalInfoService;

	/**
	 * 上报窜礼物、窜聊天问题
	 * @return
	 */
	@RequestMapping("/reportFleeGiftAndMsg")
	@ResponseBody
	public ReturnModel reportFleeGiftAndMsg(HttpServletRequest req, HttpServletResponse resp) {
		try {
			if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","appVersion","msg")) {
				returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
				returnModel.setMessage("缺少参数或参数为空");
				return returnModel;
			}
			if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
				returnModel.setCode(CodeContant.ConParamTypeIsErr);
				returnModel.setMessage("参数类型错误");
				return returnModel;
			}
			authToken(req, true);
			if (returnModel.getCode() != CodeContant.OK) {
				// 验证失败
				return returnModel;
			}
			String token = req.getParameter("token");
			int uid = authService.decryptToken(token, returnModel);
			if (uid <= 0) {
				return returnModel;
			}
			int anchorId = StringUtils.isBlank(req.getParameter("anchorId"))?0:Integer.parseInt(req.getParameter("anchorId"));
			String msg = req.getParameter("msg");
			String os = req.getParameter("os");
			String appVersion = req.getParameter("appVersion");
			statisticalInfoService.report(uid,anchorId,os,appVersion,msg);
			
			return returnModel;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("系统异常");
			return returnModel;
		}
	}
}
