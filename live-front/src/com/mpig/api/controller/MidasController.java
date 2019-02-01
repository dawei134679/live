package com.mpig.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpig.api.model.ReturnModel;
import com.mpig.api.service.IMidasService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.ParamHandleUtils;

@Controller
@Scope("prototype")
@RequestMapping("/midas")
public class MidasController extends BaseController{

	@Resource
	private IMidasService midasService;
	
	@RequestMapping("/pay")
	@ResponseBody
	public ReturnModel pay(String openid, String openkey, String pf, String pfkey, String type, HttpServletRequest req, HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "openid","openkey","pf","pfkey","type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			returnModel.setCode(CodeContant.CONAUTHTOKEN);
			returnModel.setMessage("请先登录！");
			return returnModel;
		}
		midasService.pay(uid, openid, openkey, pf, pfkey, type, returnModel);
		return returnModel;
	}
}
