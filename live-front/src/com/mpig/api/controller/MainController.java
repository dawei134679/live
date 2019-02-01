package com.mpig.api.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

import com.mpig.api.dictionary.lib.BaseConfigLib;
import com.mpig.api.model.IosVersionModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IWebService;
import com.mpig.api.statistics.Statistics;
import com.mpig.api.utils.BaseContant;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.ParamHandleUtils;

@Controller
@Scope("prototype")
@RequestMapping("/main")
public class MainController extends BaseController {

//	private static final Logger logger = LoggerFactory
//			.getLogger(MainController.class);

	private static final Logger logger = Logger.getLogger(MainController.class);
	@Resource
	private IWebService webService;

	/**
	 * 获取banner列表
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/banners", method = RequestMethod.GET)
	public void getBanners(HttpServletRequest req, HttpServletResponse resp) {
		String os = req.getParameter("os");
		String channel =  req.getParameter("channel") == null ? "":req.getParameter("channel");
		
		List<Map<String, Object>> list = webService.getBanners(os,channel);
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("list", list);
		returnModel.setData(map);
		
		writeJson(resp, returnModel);
	}

	/**
	 * 添加用户反馈
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/feedback", method = RequestMethod.GET)
	public void addFeedback(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime",
				"cls", "des")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "cls")) {
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
		int cls = Integer.valueOf(req.getParameter("cls"));
		String content = req.getParameter("des");
		String mobile = req.getParameter("mobile");

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}

		try {
			content = URLDecoder.decode(content.trim(), "UTF-8");
			int ires = webService.addFeedback(uid, cls, mobile, content);
			if (ires == 1) {

			} else {
				returnModel.setCode(CodeContant.CONSYSTEMERR);
				returnModel.setMessage("系统繁忙，请稍后再试");
			}
		} catch (Exception e) {
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("系统繁忙，请稍后再试");
			logger.error("<addFeedback->UnsupportedEncodingException>"
					+ e.toString());
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 获取ios相关项是否显示
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/iosshow", method = RequestMethod.GET)
	public void getIosShow(HttpServletRequest req, HttpServletResponse resp) {
		String strVer = req.getParameter("version");
		Map<String, String> map = new HashMap<String, String>();
		IosVersionModel iosVersionModel = webService.getIosShow(strVer);
		if (null != iosVersionModel) {
			map.put("pay", iosVersionModel.getPay() + "");
			map.put("payother", iosVersionModel.getPayother() + "");
			map.put("giftshow", iosVersionModel.getGiftshow() + "");
			map.put("tixian", iosVersionModel.getTixian() + "");
			map.put("gameshow", iosVersionModel.getGameshow() + "");
			map.put("adsshow", iosVersionModel.getAdsshow() + "");
			map.put("audit", iosVersionModel.getAudit() + "");
		} else {
			map.put("pay", "0");
			map.put("payother", "0");
			map.put("giftshow", "0");
			map.put("tixian", "0");
			map.put("gameshow", "0");
			map.put("audit", "0");
		}
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}

	/**
	 * 获取支付公告接口
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/guide", method = RequestMethod.GET)
	public void getGuide(HttpServletRequest req,HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
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
		String os = req.getParameter("os")=="2"?"ios":"android";
		
		String payNotice = OtherRedisService.getInstance().getPayNotice(os);
		Map<String, String> map = new HashMap<String,String>();
		map.put("notice", payNotice);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 设置用户
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/broadcast", method = RequestMethod.GET)
	public void setAppBroadcast(HttpServletRequest req,HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","token","devicetoken")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
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
		Integer uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String os = "2".equals(req.getParameter("os"))?"ios":"android";
		
		String devicetoken = req.getParameter("devicetoken");
		OtherRedisService.getInstance().setAppBroadcast(uid.toString(),devicetoken,os);
		Set<String> follows = RelationRedisService.getInstance().getFollows(uid);
		if (follows != null) {
			for (String suid:follows) {
				UserRedisService.getInstance().setBroadcastAnchor(os,suid,String.valueOf(uid), devicetoken);
			}
		}
		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取所有基础配置项
	 * @return
	 */
	@RequestMapping("/baseConf")
	@ResponseBody
	public ReturnModel getBaseConfig(String os,HttpServletRequest req,HttpServletResponse resp){
		if(StringUtils.isEmpty(os)){
			returnModel.setCode(CodeContant.CONAUTHEMPTY);
			returnModel.setMessage("校验数据为空!");
			return returnModel;
		}
		
		ConcurrentHashMap<String, Object> allConfig = BaseConfigLib.getAllConfig();
		ConcurrentHashMap<String, Object> baseAllConfig = new ConcurrentHashMap<String, Object>();
		baseAllConfig.putAll(allConfig);
		if(os.equals("1")){
			baseAllConfig.remove(BaseContant.CONFIG_OPENSCREEM_IOS);

		}else if(os.equals("2")){
			baseAllConfig.remove(BaseContant.CONFIG_OPENSCREEM_ANDROID);
//			baseAllConfig.remove(BaseContant.CONFIG_ACTMIDLLE); // 审核期间 使用
		}
		
		//启动统计
		Statistics.SendPigAnalysis(-1,os,req);
		
		returnModel.setData(baseAllConfig);
		return returnModel;
	}

	@RequestMapping(value = "/applogs",method=RequestMethod.POST)
	@ResponseBody
	public void getAppLogs(HttpServletRequest request,HttpServletResponse response){
		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime","versioncode","msg","code")) {
//			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
//			returnModel.setMessage("缺少参数或参数为空");
//			writeJson(resp, returnModel);
			return;
		}
		// 验证auth及token
		authToken(request, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
//			writeJson(request, returnModel);
			return;
		}
		
		
		logger.info("os:" + request.getParameter("os") + " version:" + request.getParameter("versioncode") + " msg:"+ request.getParameter("msg"));
	}
}