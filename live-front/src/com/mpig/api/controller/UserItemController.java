package com.mpig.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mpig.api.service.IUserItemService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.ParamHandleUtils;

@Controller
@Scope("prototype")
@RequestMapping("/useritem")
public class UserItemController extends BaseController {
	
	@Resource
	private IUserItemService userItemService;
	
	/**
	 * 获取用户基本信息
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/baglist", method = RequestMethod.GET)
	public void getBaglist(HttpServletRequest req, HttpServletResponse resp) {

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

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		List<Map<String, Object>> itemlist = userItemService.getBagByUid(uid);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("list", itemlist);
		returnModel.setData(map);
		
		writeJson(resp, returnModel);
	}
}
