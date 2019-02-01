package com.mpig.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpig.api.model.PrizeModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.service.IActivitiesModeService;
import com.mpig.api.utils.CodeContant;


/*
 * 模块化活动Controller
 */
@Controller
@Scope("prototype")
@RequestMapping("/activitiesmode")
public class ActivitiesModeController extends BaseController {

	private static final Logger logger = Logger.getLogger(ActivitiesModeController.class);
	
	@Resource
	IActivitiesModeService serviceActmode;
	
	/**
	 * 领取各种活动奖励，例如点击宝箱，领取对应的物品奖励等
	 * @param req	参数1.领取奖励的prize
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/acceptprize", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel acceptPrize(HttpServletRequest req, HttpServletResponse resp) {
		if(false == checkDefaultArgs(req)){
			return returnModel;
		}
		
		String token = req.getParameter("token");
		int srcuid = authService.decryptToken(token, returnModel);
		if(srcuid <= 0){
			return returnModel;
		}
		
		String prize = req.getParameter("prize");
		if(null == prize){
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		
		PrizeModel data = serviceActmode.acceptPrize(srcuid, prize);
		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("prize", data);
		
		returnModel.setData(data);
		return returnModel;
	}
	
	/**
	 * 领取各种活动奖励，例如点击宝箱，领取对应的物品奖励等
	 * @param req	参数1.领取奖励的prize
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/amtopsnap", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel amtopsnap(HttpServletRequest req, HttpServletResponse resp) {
		if(false == checkDefaultArgs(req)){
			return returnModel;
		}
		
		String token = req.getParameter("token");
		int srcuid = authService.decryptToken(token, returnModel);
		if(srcuid <= 0){
			return returnModel;
		}

		String startts = req.getParameter("startts");
		String endts = req.getParameter("endts");
		String amname = req.getParameter("amname");
		if(StringUtils.isEmpty(amname) || StringUtils.isEmpty(startts) || StringUtils.isEmpty(endts) ){
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		long startsec = Long.valueOf(startts);
		long endsec = Long.valueOf(endts);;
		serviceActmode.amtopsnap(srcuid, startsec,endsec,amname,returnModel);
		
		return returnModel;
	}
	
}
