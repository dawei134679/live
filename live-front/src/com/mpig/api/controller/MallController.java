package com.mpig.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpig.api.dao.IBillcvgListDao;
import com.mpig.api.dictionary.lib.GiftPromotionConfigLib;
import com.mpig.api.model.ConfigGiftModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.model.UserCarInfoModel;
import com.mpig.api.model.UserGuardInfoModel;
import com.mpig.api.model.UserMallItemModel;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.model.ValueaddPrivilegeModel;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.IMallService;
import com.mpig.api.service.IUserCarInfoService;
import com.mpig.api.service.IUserGuardInfoService;
import com.mpig.api.service.IUserMallItemService;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.IUserVipInfoService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.ValueaddServiceUtil;

@Controller
@Scope("prototype")
@RequestMapping("/mall")
public class MallController extends BaseController{

	private static final Logger logger = Logger.getLogger(MallController.class);

	@Resource
	private IConfigService configService;
	@Resource
	private IUserService userService;
	@Resource
	private IMallService mallService;
	@Resource
	private IUserGuardInfoService userGuardInfoService;
	@Resource
	private IUserVipInfoService userVipInfoService;
	@Resource
	private IUserMallItemService userMallItemService;
	@Resource
	private IUserCarInfoService userCarInfoService;
	@Autowired
	private IBillcvgListDao billcvgDao;
	
	/**
	 * 根据gid和月份获取价格
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value="/getprice")
	@ResponseBody
	public ReturnModel getPrice(HttpServletRequest req, HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","gid","count","type","dstuid")) {
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
			return returnModel;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		Long nowtime = System.currentTimeMillis()/1000;
		Integer gid = Integer.parseInt(req.getParameter("gid"));
		Integer count = Integer.parseInt(req.getParameter("count"));
		Integer type = Integer.parseInt(req.getParameter("type"));
		Integer dstuid = Integer.parseInt(req.getParameter("dstuid"));
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("礼物不存在");
			return returnModel;
		}
		if(count <=0){
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		Map<String, Object> data = new HashMap<String, Object>();
		int price = giftConfigModel.getGprice()*count; //价格 (显示用)
		int realprice = giftConfigModel.getGprice()*count; //实际价格(实际需要支付)
		if(type == 1){ //守护
			//计算价格时 需要考虑当前用户是否拥有对应的守护 如果有则按照续费的价格计算. 续费折扣参见文档
			Date date = new Date();
			Date startdate = new Date();
			if(dstuid != null){
				UserBaseInfoModel dstUserBaseinfo = userService.getUserbaseInfoByUid(dstuid, false);
				if(dstUserBaseinfo == null){
					returnModel.setCode(CodeContant.USERASSETEXITS);
					returnModel.setMessage("用户不存在");
					return returnModel;
				}
				data.put("dstuid", dstUserBaseinfo.getUid());
				data.put("dstnickname", dstUserBaseinfo.getNickname());
				data.put("dstheadimage", dstUserBaseinfo.getHeadimage());
				UserGuardInfoModel userGuardInfoModel = ValueaddServiceUtil.getGuardInfo(uid, dstuid);
				if(userGuardInfoModel != null && nowtime.intValue() <= userGuardInfoModel.getEndtime().intValue()){
					if(userGuardInfoModel.getGid().intValue()==gid.intValue()){ //续费则在之前的有效期上加时间
						long endtime = userGuardInfoModel.getEndtime();
						long starttime = userGuardInfoModel.getStarttime();
						ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(userGuardInfoModel.getGid(), userGuardInfoModel.getLevel());
						double discount = privilegeModel.getRenewalDiscount();
						double discountprice = price*discount;
						price = (int)discountprice;
						realprice = (int)discountprice;
						date = new Date(endtime*1000);
						startdate = new Date(starttime*1000);
					}
				}
			}
			Date newEndtime = DateUtils.getNMonthAfterDate(date, count);
			data.put("starttime", startdate.getTime()/1000);
			data.put("endtime", newEndtime.getTime()/1000);
		}else if(type == 2){ //vip
			UserBaseInfoModel dstUserBaseinfo = userService.getUserbaseInfoByUid(dstuid, false);
			if(dstUserBaseinfo == null){
				returnModel.setCode(CodeContant.USERASSETEXITS);
				returnModel.setMessage("用户不存在");
				return returnModel;
			}
			UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(dstuid);
			Date date = new Date();
			Date startdate = new Date();
			if(userVipInfoModel != null && nowtime.intValue() <= userVipInfoModel.getEndtime().intValue()){
				if(userVipInfoModel.getGid().intValue()==gid.intValue()){
					long endtime = userVipInfoModel.getEndtime();
					long starttime = userVipInfoModel.getStarttime();
					date = new Date(endtime*1000);
					startdate = new Date(starttime*1000);
				}
			}
			data.put("dstuid", dstUserBaseinfo.getUid());
			data.put("dstnickname", dstUserBaseinfo.getNickname());
			data.put("dstheadimage", dstUserBaseinfo.getHeadimage());
			Date newEndtime = DateUtils.getNMonthAfterDate(date, count);
			data.put("starttime", startdate.getTime()/1000);
			data.put("endtime", newEndtime.getTime()/1000);
		}else if(type == 3){ //道具
			UserBaseInfoModel dstUserBaseinfo = userService.getUserbaseInfoByUid(dstuid, false);
			if(dstUserBaseinfo == null){
				returnModel.setCode(CodeContant.USERASSETEXITS);
				returnModel.setMessage("用户不存在");
				return returnModel;
			}
			UserMallItemModel mallItemModel = ValueaddServiceUtil.getMengzhuCard(dstuid);
			Date date = new Date();
			Date startdate = new Date();
			if(mallItemModel != null && nowtime.intValue() <= mallItemModel.getEndtime()){
				long endtime = mallItemModel.getEndtime();
				long starttime = mallItemModel.getStarttime();
				date = new Date(endtime*1000);
				startdate = new Date(starttime*1000);
			}
			data.put("gname", giftConfigModel.getGname());
			data.put("dstuid", dstUserBaseinfo.getUid());
			data.put("dstnickname", dstUserBaseinfo.getNickname());
			data.put("dstheadimage", dstUserBaseinfo.getHeadimage());
			Date newEndtime = DateUtils.getNMonthAfterDate(date, count);
			data.put("starttime", startdate.getTime()/1000);
			data.put("endtime", newEndtime.getTime()/1000);
		}else if(type == 4){ //座驾
			UserBaseInfoModel dstUserBaseinfo = userService.getUserbaseInfoByUid(dstuid, false);
			if(dstUserBaseinfo == null){
				returnModel.setCode(CodeContant.USERASSETEXITS);
				returnModel.setMessage("用户不存在");
				return returnModel;
			}
			UserCarInfoModel userCarInfoModel = ValueaddServiceUtil.getCarInfo(dstuid);
			Date date = new Date();
			Date startdate = new Date();
			if(userCarInfoModel != null  && nowtime.intValue() <= userCarInfoModel.getEndtime().intValue()){
				long endtime = userCarInfoModel.getEndtime();
				long starttime = userCarInfoModel.getStarttime();
				date = new Date(endtime*1000);
				startdate = new Date(starttime*1000);
			}
			data.put("gname", giftConfigModel.getGname());
			data.put("dstuid", dstUserBaseinfo.getUid());
			data.put("dstnickname", dstUserBaseinfo.getNickname());
			data.put("dstheadimage", dstUserBaseinfo.getHeadimage());
			Date newEndtime = DateUtils.getNMonthAfterDate(date, count);
			data.put("starttime", startdate.getTime()/1000);
			data.put("endtime", newEndtime.getTime()/1000);
		}else{
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		data.put("gid", gid);
		data.put("gprice", giftConfigModel.getGprice());
		data.put("count", count);
		data.put("pricetotal", price);
		realprice = GiftPromotionConfigLib.getDisPrice(gid, realprice);
		data.put("realpricetotal", realprice);
		returnModel.setData(data);
		return returnModel;
	}
	
	@RequestMapping(value="/buy/Guard")
	@ResponseBody
	public ReturnModel buyGuard(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime",
				"token", "gid", "count", "dstuid","price","realprice","inroom","forcebuy")) {
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
			return returnModel;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		Long nowtime = System.currentTimeMillis()/1000;
		Integer gid = Integer.parseInt(req.getParameter("gid"));
		Integer count = Integer.parseInt(req.getParameter("count"));
		Integer dstuid = Integer.parseInt(req.getParameter("dstuid"));
		Integer inroom = Integer.parseInt(req.getParameter("inroom"));
		Integer forcebuy = Integer.parseInt(req.getParameter("forcebuy"));
		Byte os = Byte.valueOf(req.getParameter("os"));
		if(uid==dstuid){
			returnModel.setCode(CodeContant.mall_buy_guard_error);
			returnModel.setMessage("不能给自己购买守护");
			return returnModel;
		}
		//TODO 确认骑士/王子相关id
		int qishiId = 45;
		int wangziId = 46;
		
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("商品不存在");
			return returnModel;
		}
		if(count <=0){
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		int price = giftConfigModel.getGprice(); //单价
		int realprice = price; //实际支付单价
		int pricetotal = giftConfigModel.getGprice()*count; //总价
		int realpricetotal = pricetotal; //实际支付总价格(实际需要支付)
		String gname = giftConfigModel.getGname();
		double gets = giftConfigModel.getCredit() * count;
		int wealths = giftConfigModel.getWealth() * count;
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return returnModel;
		}
		
		int isopen = 0;
		if (gid.intValue() == qishiId) { // 骑士守护
			// 查询是否拥有身份;拥有王子且有效则不可操作,无效则置为不可用并添加/修改骑士守护的服务时间; 如果有骑士守护并有效则续期并启用.
			UserGuardInfoModel guardInfoModel = ValueaddServiceUtil.getGuardInfo(uid, dstuid);
			Date date = new Date();
			Date startdate = new Date();
			if (guardInfoModel != null) {
				if(guardInfoModel.getGid().intValue() == wangziId && nowtime.intValue() <= guardInfoModel.getCushiontime().intValue()){
					ConfigGiftModel newGiftConfigModel = configService.getGiftInfoByGidNew(guardInfoModel.getGid().intValue());
					returnModel.setCode(CodeContant.mall_guard_level_error);
					returnModel.setMessage(giftConfigModel.getGname()+"不能覆盖"+newGiftConfigModel.getGname());
					return returnModel;
				}else{
					isopen=1;
					long endtime = guardInfoModel.getEndtime();
					long starttime = guardInfoModel.getStarttime();
					ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(guardInfoModel.getGid(), guardInfoModel.getLevel());
					double discount = privilegeModel.getRenewalDiscount();
					double discountprice = realprice*discount;
					realprice = (int)discountprice;
					double discountrealpricetotal = realpricetotal*discount;
					realpricetotal = (int)discountrealpricetotal;
					
					date = new Date(endtime*1000);
					startdate = new Date(starttime*1000);
				}
			}
			realprice = GiftPromotionConfigLib.getDisPrice(gid, realprice);
			realpricetotal = GiftPromotionConfigLib.getDisPrice(gid, realpricetotal);
			if (realpricetotal > sendUserAssetModel.getMoney()) {
				returnModel.setCode(CodeContant.MONEYLESS);
				returnModel.setMessage("金额不足");
				return returnModel;
			}
			Date newEndtime = DateUtils.getNMonthAfterDate(date, count);
			Date cushiontime = DateUtils.getNDaysAfterDate(newEndtime, 7);
			int exp = 20;
			mallService.buyGuard(gid, gname, uid, dstuid, count, wealths, gets, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), new Long(cushiontime.getTime()/1000).intValue(), price, pricetotal, realprice, realpricetotal, os,exp,inroom,isopen, returnModel);
			billcvgDao.insBillcvgList(uid, dstuid, gid, gname,realpricetotal, count, new Long(new Date().getTime()/1000).intValue(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 3);
			logger.debug("插入购买骑士守护记录=======");
		} else if (gid.intValue() == wangziId) {// 王子守护
			UserGuardInfoModel guardInfoModel = ValueaddServiceUtil.getGuardInfo(uid, dstuid);
			Date date = new Date();
			Date startdate = new Date();
			int exp = 30;
			if(guardInfoModel != null ){
				if(guardInfoModel.getGid().intValue() == qishiId  && nowtime.intValue() <= guardInfoModel.getCushiontime().intValue()){
					if(forcebuy==0){
						ConfigGiftModel newGiftConfigModel = configService.getGiftInfoByGidNew(guardInfoModel.getGid().intValue());
						returnModel.setCode(CodeContant.mall_force_buy_guard);
						returnModel.setMessage("提示，您已开通"+newGiftConfigModel.getGname()+"？确定后，"+giftConfigModel.getGname()+"将覆盖"+newGiftConfigModel.getGname()+"，有效期"+count+"个月？");
						return returnModel;
					}
					userGuardInfoService.updUserGuardInfo(exp, guardInfoModel.getStarttime(), guardInfoModel.getEndtime(), guardInfoModel.getCushiontime(), 1, uid, dstuid, guardInfoModel.getGid().intValue());
				}else{
					isopen = 1;
					long endtime = guardInfoModel.getEndtime();
					long starttime = guardInfoModel.getStarttime();
					ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(guardInfoModel.getGid(), guardInfoModel.getLevel());
					double discount = privilegeModel.getRenewalDiscount();
					double discountprice = realprice*discount;
					realprice = (int)discountprice;
					double discountrealpricetotal = realpricetotal*discount;
					realpricetotal = (int)discountrealpricetotal;
					date = new Date(endtime*1000);
					startdate = new Date(starttime*1000);
				}
			}
			realprice = GiftPromotionConfigLib.getDisPrice(gid, realprice);
			realpricetotal = GiftPromotionConfigLib.getDisPrice(gid, realpricetotal);
			if (realpricetotal > sendUserAssetModel.getMoney()) {
				returnModel.setCode(CodeContant.MONEYLESS);
				returnModel.setMessage("金额不足");
				return returnModel;
			}
			Date newEndtime = DateUtils.getNMonthAfterDate(date, count);
			Date cushiontime = DateUtils.getNDaysAfterDate(newEndtime, 7);
			mallService.buyGuard(gid, gname, uid, dstuid, count, wealths, gets, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), new Long(cushiontime.getTime()/1000).intValue(), price, pricetotal, realprice, realpricetotal, os, exp,inroom,isopen, returnModel);
			billcvgDao.insBillcvgList(uid, dstuid, gid, gname,realpricetotal, count, new Long(new Date().getTime()/1000).intValue(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 3);
			logger.debug("插入购买王子守护记录=======");
		} else {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		return returnModel;
	}
	
	@RequestMapping(value="/buy/VIP")
	@ResponseBody
	public ReturnModel buyVip(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime",
				"token", "gid", "count", "dstuid","price","realprice","forcebuy")) {
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
			return returnModel;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		Long nowtime = System.currentTimeMillis()/1000;
		Integer gid = Integer.parseInt(req.getParameter("gid"));
		Integer count = Integer.parseInt(req.getParameter("count"));
		Integer dstuid = Integer.parseInt(req.getParameter("dstuid"));
		//TODO 确认白金/钻石相关id
		int baijinId = 43;
		int zuanshiId = 44;
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("商品不存在");
			return returnModel;
		}
		int price = giftConfigModel.getGprice(); //单价
		int realprice = price; //实际支付单价
		int pricetotal = giftConfigModel.getGprice()*count; //总价
		int realpricetotal = pricetotal; //实际支付总价格(实际需要支付)
		Integer forcebuy = Integer.parseInt(req.getParameter("forcebuy"));
		String gname = giftConfigModel.getGname();
		int wealths = giftConfigModel.getWealth() * count;
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return returnModel;
		}
		if(count <=0){
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		
		if(gid.intValue() == baijinId){
			UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(dstuid);
			Date date = new Date();
			Date startdate = new Date();
			if(userVipInfoModel != null && nowtime.intValue() <= userVipInfoModel.getEndtime().intValue()){
				if(userVipInfoModel.getGid().intValue() == zuanshiId){ //当前的id不可覆盖
						ConfigGiftModel newGiftConfigModel = configService.getGiftInfoByGidNew(userVipInfoModel.getGid().intValue());
						returnModel.setCode(CodeContant.mall_guard_level_error);
						returnModel.setMessage(giftConfigModel.getGname()+"不能覆盖"+newGiftConfigModel.getGname());
						return returnModel;
				}else{
					long endtime = userVipInfoModel.getEndtime();
					long starttime = userVipInfoModel.getStarttime();
					ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(userVipInfoModel.getGid(),1);
					double discount = privilegeModel.getRenewalDiscount();
					double discountprice = realprice*discount;
					realprice = (int)discountprice;
					double discountrealpricetotal = realpricetotal*discount;
					realpricetotal = (int)discountrealpricetotal;
					date = new Date(endtime*1000);
					startdate = new Date(starttime*1000);
				}
			}
			realprice = GiftPromotionConfigLib.getDisPrice(gid, realprice);
			realpricetotal = GiftPromotionConfigLib.getDisPrice(gid, realpricetotal);
			if (realpricetotal > sendUserAssetModel.getMoney()) {
				returnModel.setCode(CodeContant.MONEYLESS);
				returnModel.setMessage("金额不足");
				return returnModel;
			}
			Date newEndtime = DateUtils.getNMonthAfterDate(date, count);
			mallService.buyVip(gid, gname, uid, dstuid, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), count, wealths, price, pricetotal, realprice, realpricetotal, returnModel);
			billcvgDao.insBillcvgList(uid, dstuid, gid, gname,realpricetotal, count, new Long(new Date().getTime()/1000).intValue(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 2);
			logger.debug("插入购买白金vip记录=======");
		}else if(gid.intValue() == zuanshiId){
			UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(dstuid);
			Date date = new Date();
			Date startdate = new Date();
			if(userVipInfoModel != null && nowtime.intValue() <= userVipInfoModel.getEndtime().intValue()){
				if(userVipInfoModel.getGid().intValue() == baijinId){
					if(uid != dstuid.intValue()){
						returnModel.setCode(CodeContant.mall_guard_level_error);
						returnModel.setMessage("亲，与赠送好友VIP不符，赠送失败！");
						return returnModel;
					}
					if(forcebuy==0){
						ConfigGiftModel newGiftConfigModel = configService.getGiftInfoByGidNew(userVipInfoModel.getGid().intValue());
						returnModel.setCode(CodeContant.mall_force_buy_guard);
						returnModel.setMessage("提示，您已开通"+newGiftConfigModel.getGname()+"？确定后，"+giftConfigModel.getGname()+"将覆盖"+newGiftConfigModel.getGname()+"，有效期"+count+"个月？");
						return returnModel;
					}
					userVipInfoService.updUserVipInfo(uid, userVipInfoModel.getStarttime(), userVipInfoModel.getEndtime(), 1, userVipInfoModel.getGid());
				}else{
					long endtime = userVipInfoModel.getEndtime();
					long starttime = userVipInfoModel.getStarttime();
					ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(userVipInfoModel.getGid(),1);
					double discount = privilegeModel.getRenewalDiscount();
					double discountprice = realprice*discount;
					realprice = (int)discountprice;
					double discountrealpricetotal = realpricetotal*discount;
					realpricetotal = (int)discountrealpricetotal;
					date = new Date(endtime*1000);
					startdate = new Date(starttime*1000);
				}
			}
			realprice = GiftPromotionConfigLib.getDisPrice(gid, realprice);
			realpricetotal = GiftPromotionConfigLib.getDisPrice(gid, realpricetotal);
			if (realpricetotal > sendUserAssetModel.getMoney()) {
				returnModel.setCode(CodeContant.MONEYLESS);
				returnModel.setMessage("金额不足");
				return returnModel;
			}
			Date newEndtime = DateUtils.getNMonthAfterDate(date, count);
			mallService.buyVip(gid, gname, uid, dstuid, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), count, wealths, price, pricetotal, realprice, realpricetotal, returnModel);
			billcvgDao.insBillcvgList(uid, dstuid, gid, gname,realpricetotal, count, new Long(new Date().getTime()/1000).intValue(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 2);
			logger.debug("插入购买钻石vip记录=======");
		}else{
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
		}
		return returnModel;
	}
	
	@RequestMapping(value="/buy/props")
	@ResponseBody
	public ReturnModel buyProps(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","token", "gid", "count", "dstuid","price","realprice")) {
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
			return returnModel;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		Long nowtime = System.currentTimeMillis()/1000;
		Integer gid = Integer.parseInt(req.getParameter("gid"));
		Integer count = Integer.parseInt(req.getParameter("count"));
		Integer dstuid = Integer.parseInt(req.getParameter("dstuid"));
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("商品不存在");
			return returnModel;
		}
		if(count <=0){
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		int price = giftConfigModel.getGprice(); //单价
		int realprice = price; //实际支付单价
		int pricetotal = giftConfigModel.getGprice()*count; //总价
		int realpricetotal = pricetotal; //实际支付总价格(实际需要支付)
		String gname = giftConfigModel.getGname();
		int wealths = giftConfigModel.getWealth() * count;
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return returnModel;
		}
		//TODO 读取商城道具的配置(暂时以ID写死)
		if(gid.intValue() == 60){
			UserMallItemModel userMallItemModel = userMallItemService.getItem(dstuid, 1, false); //TODO subtype可根据商城道具动态获取
			Date date = new Date();
			Date startdate = new Date();
			if(userMallItemModel !=null && nowtime.intValue() <= userMallItemModel.getEndtime()){
				long endtime = userMallItemModel.getEndtime();
				long starttime = userMallItemModel.getStarttime();
				double discountprice = realprice;
				realprice = (int)discountprice;
				date = new Date(endtime*1000);
				startdate = new Date(starttime*1000);
			}
			Date newEndtime = DateUtils.getNMonthAfterDate(date, count);
			realprice = GiftPromotionConfigLib.getDisPrice(gid, realprice);
			realpricetotal = GiftPromotionConfigLib.getDisPrice(gid, realpricetotal);
			if (realpricetotal > sendUserAssetModel.getMoney()) {
				returnModel.setCode(CodeContant.MONEYLESS);
				returnModel.setMessage("金额不足");
				return returnModel;
			}
			mallService.buyProps(gid, gname, uid, dstuid, 1, 1, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), count, wealths, price, pricetotal, realprice, realpricetotal, returnModel);
		}else{
			returnModel.setCode(CodeContant.MALLGIFTEXIST);
			returnModel.setMessage("商城道具不存在");
		}
		return returnModel;
	}
	
	@RequestMapping(value="/buy/car")
	@ResponseBody
	public ReturnModel buyCar(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","token", "gid", "count", "dstuid","price","realprice")) {
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
			return returnModel;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		Long nowtime = System.currentTimeMillis()/1000;
		Integer gid = Integer.parseInt(req.getParameter("gid"));
		Integer count = Integer.parseInt(req.getParameter("count"));
		Integer dstuid = Integer.parseInt(req.getParameter("dstuid"));
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("商品不存在");
			return returnModel;
		}
		if(count <=0){
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		int price = giftConfigModel.getGprice(); //单价
		int realprice = price; //实际支付单价
		int pricetotal = giftConfigModel.getGprice()*count; //总价
		int realpricetotal = pricetotal; //实际支付总价格(实际需要支付)
		String gname = giftConfigModel.getGname();
		int wealths = giftConfigModel.getWealth() * count;
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return returnModel;
		}
		if(giftConfigModel.getSubtype() == 5){
			UserCarInfoModel userCarInfo = userCarInfoService.getUserCarInfo(dstuid, gid, true);
			Date date = new Date();
			Date startdate = new Date();
			if(userCarInfo !=null && nowtime.intValue() <= userCarInfo.getEndtime().intValue()){
				long endtime = userCarInfo.getEndtime();
				long starttime = userCarInfo.getStarttime();
				double discountprice = realprice;
				realprice = (int)discountprice;
				date = new Date(endtime*1000);
				startdate = new Date(starttime*1000);
			}
			Date newEndtime = DateUtils.getNMonthAfterDate(date, count);
			realprice = GiftPromotionConfigLib.getDisPrice(gid, realprice);
			realpricetotal = GiftPromotionConfigLib.getDisPrice(gid, realpricetotal);
			if (realpricetotal > sendUserAssetModel.getMoney()) {
				returnModel.setCode(CodeContant.MONEYLESS);
				returnModel.setMessage("金额不足");
				return returnModel;
			}
			mallService.buyCar(gid, gname, uid, dstuid, 1, 1, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), count, wealths, price, pricetotal, realprice, realpricetotal, returnModel);
			billcvgDao.insBillcvgList(uid, dstuid, gid, gname,realpricetotal, count, new Long(new Date().getTime()/1000).intValue(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 1);
			logger.debug("插入购买座驾记录=======");
		}else{
			returnModel.setCode(CodeContant.MALLGIFTEXIST);
			returnModel.setMessage("商城道具不存在");
		}
		return returnModel;
	}
	
	@RequestMapping(value="/car/choose")
	@ResponseBody
	public ReturnModel chooseCar(HttpServletRequest req, HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","token","gid","type")) {
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
			return returnModel;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		Integer gid = Integer.parseInt(req.getParameter("gid"));
		Integer type = Integer.parseInt(req.getParameter("type"));
		int rsc = 0;
		if(type==1){ //启用
			rsc = userCarInfoService.updUserCarInfoStatus(uid, gid, type);
		}else{ //停用
			rsc = userCarInfoService.updUserCarInfoStatus(uid, gid, type);
		}
		if(rsc == 0){
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("操作失败");
		}
		return returnModel;
	}
	
	@RequestMapping(value="/props/list")
	@ResponseBody
	public ReturnModel getPropsList(HttpServletRequest req, HttpServletResponse resp){
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
		Map<String,Object> dateMap = new HashMap<String,Object>();
		int gid = 60;
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		dateMap.put("gid", giftConfigModel.getGid());
		dateMap.put("gname", giftConfigModel.getGname());
		dateMap.put("gimg", giftConfigModel.getIcon());
		dateMap.put("gprice", giftConfigModel.getGprice());
		dateMap.put("des", Constant.mall_props1_des);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(dateMap);
		Map<String, Object> _map = new HashMap<String, Object>();
		_map.put("list", list);
		returnModel.setData(_map);
		return returnModel;
	}
	
	@RequestMapping(value="/getMyProps")
	@ResponseBody
	public ReturnModel getMyProps(HttpServletRequest req, HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","token")) {
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
			return returnModel;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		mallService.getMyProps(uid, returnModel);
		return returnModel;
	}
	
	/**
	 * 商城坐骑列表
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value="/car/list")
	@ResponseBody
	public ReturnModel getCarList(HttpServletRequest req, HttpServletResponse resp){
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
		ConcurrentHashMap<Integer, ConfigGiftModel> configCarModels = configService.getConfigCarModels();
		List<Object> carList = new ArrayList<Object>();
		for(Map.Entry<Integer, ConfigGiftModel> entry : configCarModels.entrySet()){
			Map<String, Object> carMap = new HashMap<String, Object>();
			int gid = entry.getValue().getGid();
			int realprice = entry.getValue().getGprice();
			carMap.put("gid", gid);
			carMap.put("gimg", entry.getValue().getIcon());
			carMap.put("gname", entry.getValue().getGname());
			carMap.put("gprice", entry.getValue().getGprice());
			
			realprice = GiftPromotionConfigLib.getDisPrice(gid, realprice);
			carMap.put("realprice", realprice);
			carMap.put("sort", entry.getValue().getGsort());
			carList.add(carMap);
		}
		Map<String, Object> _map = new HashMap<String, Object>();
		_map.put("list", carList);
		returnModel.setData(_map);
		return returnModel;
	}
	
}
