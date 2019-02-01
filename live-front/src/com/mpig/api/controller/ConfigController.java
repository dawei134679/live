package com.mpig.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.mpig.api.model.ConfigGiftModel;
import com.mpig.api.model.IosVersionModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.IWebService;
import com.mpig.api.service.impl.WebServiceImpl;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.ParamHandleUtils;

@Controller
@Scope("prototype")
@RequestMapping("/config")
public class ConfigController extends BaseController {
	private static final Logger logger = Logger.getLogger(ConfigController.class);

	@Resource
	IConfigService configService;
	
	@Resource
	private IWebService webService;
	
	/**
	 * 礼物列表（作废）
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/giftlist", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getGiftList(HttpServletRequest req,HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","ver")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","ver")) {
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
		int ver = Integer.valueOf(req.getParameter("ver"));
		configService.getGiftList(ver, returnModel);
		return returnModel;
	}
	
	@RequestMapping(value = "/giftlist/v1", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getGiftListV1(HttpServletRequest req,HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","ver")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","ver")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}
		int ver = Integer.valueOf(req.getParameter("ver"));
		configService.getGiftListNew(ver, returnModel);
		return returnModel;
	}
	
	@RequestMapping(value = "/giftlist/pc")
	@ResponseBody
	public ReturnModel getGiftListPC(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}

		int uid = 0;
		String token = req.getParameter("token");// 充值操作对象
		if (!StringUtils.isEmpty(token)) {
			uid = authService.decryptToken(token, returnModel);
			if (uid <= 0) {
				return returnModel;
			}
		}
		
		configService.getGiftListPC(uid, returnModel);
		
		return returnModel;
	}

	
	@RequestMapping(value = "/giftlist/pc/baglist")
	@ResponseBody
	public ReturnModel getBaglist(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
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

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		
		List<Map<String, Object>> baglists = configService.getBaglists(uid);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("bags", baglists);
		returnModel.setData(map);
		
		return returnModel;
	}
	

	@RequestMapping(value = "/giftlist/h5", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getGiftListH1(HttpServletRequest req,HttpServletResponse resp){
//		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
//			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
//			returnModel.setMessage("缺少参数或参数为空");
//			return returnModel;
//		}
//		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
//			returnModel.setCode(CodeContant.ConParamTypeIsErr);
//			returnModel.setMessage("参数类型错误");
//			return returnModel;
//		}
//		// 验证auth及token
//		authToken(req, true);
//		if (returnModel.getCode() != CodeContant.OK) {
//			// 验证失败
//			return returnModel;
//		}
		configService.getGiftListH5(returnModel);
		return returnModel;
	}

	@RequestMapping(value = "/giftinfo", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getGiftInfoByGid(HttpServletRequest req,HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","gid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","gid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}
		int gid = Integer.valueOf(req.getParameter("gid"));
		ConfigGiftModel giftInfoBean = configService.getGiftInfoByGidNew(gid);
		ConfigGiftModel giftInfoByGidNew = new ConfigGiftModel();
		if(null != giftInfoBean) {
			//复制一个对象，下边要改对象
			{
				giftInfoByGidNew.setAct(giftInfoBean.getAct());
				giftInfoByGidNew.setBimgs(giftInfoBean.getBimgs());
				giftInfoByGidNew.setCategory(giftInfoBean.getCategory());
				giftInfoByGidNew.setCharm(giftInfoBean.getCharm());
				giftInfoByGidNew.setCreateAt(giftInfoBean.getCreateAt());
				giftInfoByGidNew.setCredit(giftInfoBean.getCredit());
				giftInfoByGidNew.setGcover(giftInfoBean.getGcover());
				giftInfoByGidNew.setGduration(giftInfoBean.getGduration());
				giftInfoByGidNew.setGframeurl(giftInfoBean.getGframeurl());
				giftInfoByGidNew.setGframeurlios(giftInfoBean.getGframeurlios());
				giftInfoByGidNew.setGid(giftInfoBean.getGid());
				giftInfoByGidNew.setGname(giftInfoBean.getGname());
				giftInfoByGidNew.setGnumtype(giftInfoBean.getGnumtype());
				giftInfoByGidNew.setGpctype(giftInfoBean.getGpctype());
				giftInfoByGidNew.setGprice(giftInfoBean.getGprice());
				giftInfoByGidNew.setGpriceaudit(giftInfoBean.getGpriceaudit());
				giftInfoByGidNew.setGsort(giftInfoBean.getGsort());
				giftInfoByGidNew.setGtype(giftInfoBean.getGtype());
				giftInfoByGidNew.setGver(giftInfoBean.getGver());
				giftInfoByGidNew.setIcon(giftInfoBean.getIcon());
				giftInfoByGidNew.setIsshow(giftInfoBean.getIsshow());
				giftInfoByGidNew.setIsvalid(giftInfoBean.getIsvalid());
				giftInfoByGidNew.setPimgs(giftInfoBean.getPimgs());
				giftInfoByGidNew.setSimgs(giftInfoBean.getSimgs());
				giftInfoByGidNew.setSkin(giftInfoBean.getSkin());
				giftInfoByGidNew.setSubtype(giftInfoBean.getSubtype());
				giftInfoByGidNew.setSver(giftInfoBean.getSver());
				giftInfoByGidNew.setType(giftInfoBean.getType());
				giftInfoByGidNew.setUseDuration(giftInfoBean.getUseDuration());
				giftInfoByGidNew.setWealth(giftInfoBean.getWealth());
			}
			
			
			//如果是IOS系统
			if(Integer.valueOf(req.getParameter("os")) == 2) {
				//ios 使用比较大的图
				giftInfoByGidNew.setGframeurl(giftInfoByGidNew.getGframeurlios());
				//根据版本 控制是否显示审核金额
				String iosVer = req.getParameter("iosver");
				logger.debug("iosVer:"+iosVer);
				if(StringUtils.isNotBlank(iosVer)) {
					IosVersionModel iosVersionModel = webService.getIosShow(iosVer);
					if(null != iosVersionModel) {
						logger.debug("iosVer:"+iosVer+" 审核版本："+iosVersionModel.getAudit());
						if(iosVersionModel.getAudit() == 1) {//1审核版本 0非审核版本
							giftInfoByGidNew.setGprice(giftInfoByGidNew.getGpriceaudit());
						}
					}
				}//end 根据版本 控制是否显示审核金额
				
			}//end ios系统
		}
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("list", giftInfoByGidNew);
		returnModel.setData(map);
		return returnModel;
	}
	
	/**
	 * 获取徽章列表
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/badges", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getBadgeInfoByGid(HttpServletRequest req,HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}
		
		List<Map<String, Object>> badges = configService.getBadges();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("list", badges);
		returnModel.setData(map);
		return returnModel;
	}
	
}
