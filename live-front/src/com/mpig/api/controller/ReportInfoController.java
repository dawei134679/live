package com.mpig.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpig.api.model.ReturnModel;
import com.mpig.api.service.IReportInfoService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.ParamHandleUtils;

@Controller
@Scope("prototype")
@RequestMapping("/reportInfo")
public class ReportInfoController extends BaseController {

	private static Logger log = Logger.getLogger(ReportInfoController.class);
	@Resource
	private IReportInfoService reportInfoService;

	// 举报
	@RequestMapping("/report")
	@ResponseBody
	public ReturnModel report(HttpServletRequest req, HttpServletResponse resp) {
		try {
			if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "uid", "content", "token")) {
				returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
				returnModel.setMessage("缺少参数或参数为空");
				return returnModel;
			}
			if (!ParamHandleUtils.isInt(req, "uid")) {
				returnModel.setCode(CodeContant.ConParamTypeIsErr);
				returnModel.setMessage("参数类型错误");
				return returnModel;
			}
			// 验证auth及token
			authToken(req, true);
			if (returnModel.getCode() != CodeContant.OK) {
				// 验证失败
				return returnModel;
			}
			Long uid = Long.valueOf(req.getParameter("uid"));
			String content = req.getParameter("content");
			String token = req.getParameter("token");// 举报人
			if(content.length() > 200) {
				returnModel.setCode(201);
				returnModel.setMessage("内容太长");
				return returnModel;
			}
			if(uid <= 0) {
				returnModel.setCode(CodeContant.CONAUTHTOKEN);
				returnModel.setMessage("被举报用户不正确");
				return returnModel;
			}
			if(uid.toString().length() > 13) {
				returnModel.setCode(202);
				returnModel.setMessage("UID不正确");
				return returnModel;
			}
			Long rid = new Long(authService.decryptToken(token, returnModel));
			if(rid <= 0) {
				return returnModel;
			}
			int nums = reportInfoService.isReport(uid, rid);
			if(nums>0) {
				returnModel.setCode(200);
				returnModel.setMessage("已举报,请勿重复举报!");
			}
			return reportInfoService.report(uid, content, rid, returnModel);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("系统异常");
			return returnModel;
		}
	}
}
