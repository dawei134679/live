package com.mpig.api.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpig.api.dao.IBillcvgListDao;
import com.mpig.api.dao.IUserGuardExpRecordDao;
import com.mpig.api.model.ConfigGiftModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserCarInfoModel;
import com.mpig.api.model.UserGuardInfoModel;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.service.IAdminService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.IUserCarInfoService;
import com.mpig.api.service.IUserGuardInfoService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.service.IUserItemService.ItemSource;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.IUserVipInfoService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.ValueaddServiceUtil;

@Service
public class AdminServiceImpl implements IAdminService{
	
	private static final Logger logger = Logger.getLogger(IAdminService.class);

	@Autowired
	private IUserGuardExpRecordDao userGuardExpRecordDao;
	@Resource
	private IConfigService configService;
	@Resource
	private IUserService userService;
	@Resource
	private IUserVipInfoService vipInfoService;
	@Resource
	private IUserGuardInfoService guardInfoService;
	@Resource
	private IUserCarInfoService userCarInfoService;
	@Resource
	private IRoomService roomService;
	@Resource
	private IUserItemService userItemService;
	@Autowired
	private IBillcvgListDao billcvgDao;
	
	@Override
	public ReturnModel addVip(Integer dstuid,Integer gid,Integer count, ReturnModel returnModel){
		int baijinId = 43;
		int zuanshiId = 44;
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("商品不存在");
			return returnModel;
		}
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(dstuid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return returnModel;
		}
		
		if(gid.intValue() == baijinId){
			UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(dstuid);
			Date date = new Date();
			Date startdate = new Date();
			if(userVipInfoModel != null){
				if(userVipInfoModel.getGid().intValue() == zuanshiId){ //当前的id不可覆盖
						ConfigGiftModel newGiftConfigModel = configService.getGiftInfoByGidNew(userVipInfoModel.getGid().intValue());
						returnModel.setCode(CodeContant.mall_guard_level_error);
						returnModel.setMessage(giftConfigModel.getGname()+"不能覆盖"+newGiftConfigModel.getGname());
						return returnModel;
				}else{
					long endtime = userVipInfoModel.getEndtime();
					long starttime = userVipInfoModel.getStarttime();
					date = new Date(endtime*1000);
					startdate = new Date(starttime*1000);
				}
			}
			Date newEndtime = DateUtils.getNDaysAfterDate(date, count);
			UserVipInfoModel userVipInfo = vipInfoService.getUserVipInfoByUid(dstuid, gid);
			int rsc = 0;
			if(userVipInfo != null){
				rsc = vipInfoService.updUserVipInfo(dstuid,new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0,gid);
			}else{
				rsc = vipInfoService.addUserVipInfo(dstuid, gid, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0);
			}
			//插入购买vip记录
			billcvgDao.insBillcvgList(dstuid, 0, giftConfigModel.getGid(), giftConfigModel.getGname(),0, count, new Long(new Date().getTime()/1000).intValue(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 2);
			logger.debug("插入购买白金vip记录------");
			if(rsc == 0){
				returnModel.setCode(CodeContant.mall_buy_vip_error);
				returnModel.setMessage("购买失败!");
				return returnModel;
			}
			vipInfoService.getUserVipInfo(dstuid, true);
		}else if(gid.intValue() == zuanshiId){
			UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(dstuid);
			Date date = new Date();
			Date startdate = new Date();
			if(userVipInfoModel != null){
				if(userVipInfoModel.getGid().intValue() == baijinId){
					vipInfoService.updUserVipInfo(dstuid, userVipInfoModel.getStarttime(), userVipInfoModel.getEndtime(), 1, userVipInfoModel.getGid());
				}else{
					long endtime = userVipInfoModel.getEndtime();
					long starttime = userVipInfoModel.getStarttime();
					date = new Date(endtime*1000);
					startdate = new Date(starttime*1000);
				}
			}
			Date newEndtime = DateUtils.getNDaysAfterDate(date, count);
			UserVipInfoModel userVipInfo = vipInfoService.getUserVipInfoByUid(dstuid, gid);
			int rsc = 0;
			if(userVipInfo != null){
				rsc = vipInfoService.updUserVipInfo(dstuid,new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0,gid);
			}else{
				rsc = vipInfoService.addUserVipInfo(dstuid, gid, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0);
			}
			//插入购买vip记录
			billcvgDao.insBillcvgList(dstuid, 0, giftConfigModel.getGid(), giftConfigModel.getGname(),0, count, new Long(new Date().getTime()/1000).intValue(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 2);
			logger.debug("插入购买钻石vip记录------");
			if(rsc == 0){
				returnModel.setCode(CodeContant.mall_buy_vip_error);
				returnModel.setMessage("购买失败!");
				return returnModel;
			}
			vipInfoService.getUserVipInfo(dstuid, true);
		}else{
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		return returnModel;
	}
	
	@Override
	public ReturnModel addGuard(Integer srcuid, Integer dstuid, Integer gid, Integer count, ReturnModel returnModel) {
		// TODO 确认骑士/王子相关id
		int qishiId = 45;
		int wangziId = 46;

		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("商品不存在");
			return returnModel;
		}
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(srcuid,false);
		UserAssetModel dstUserAssetModel = userService.getUserAssetByUid(dstuid,false);
		if (sendUserAssetModel == null || dstUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return returnModel;
		}

		if (gid.intValue() == qishiId) { // 骑士守护
			// 查询是否拥有身份;拥有王子且有效则不可操作,无效则置为不可用并添加/修改骑士守护的服务时间; 如果有骑士守护并有效则续期并启用.
			UserGuardInfoModel guardInfoModel = ValueaddServiceUtil.getGuardInfo(srcuid, dstuid);
			Date date = new Date();
			Date startdate = new Date();
			if (guardInfoModel != null) {
				if (guardInfoModel.getGid().intValue() == wangziId) {
					ConfigGiftModel newGiftConfigModel = configService.getGiftInfoByGidNew(guardInfoModel.getGid().intValue());
					returnModel.setCode(CodeContant.mall_guard_level_error);
					returnModel.setMessage(giftConfigModel.getGname() + "不能覆盖" + newGiftConfigModel.getGname());
					return returnModel;
				} else {
					long endtime = guardInfoModel.getEndtime();
					long starttime = guardInfoModel.getStarttime();
					date = new Date(endtime * 1000);
					startdate = new Date(starttime * 1000);
				}
			}
			Date newEndtime = DateUtils.getNDaysAfterDate(date, count);
			Date cushiontime = DateUtils.getNDaysAfterDate(newEndtime, 7);
			int exp = 20;

			UserGuardInfoModel userGuardInfoModel = guardInfoService.getUserGuardInfoByUid(srcuid, dstuid, gid);
			int rsc = 0;
			if(userGuardInfoModel != null){
				rsc = guardInfoService.updUserGuardInfo(exp, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), new Long(cushiontime.getTime()/1000).intValue(), 0, srcuid, dstuid, gid);
				//插入成长值记录
				userGuardExpRecordDao.insExpRecord(srcuid, dstuid,gid, exp, 3);
			}else{
				rsc = guardInfoService.addUserGuardInfo(dstuid, srcuid, gid, 1, 0, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), new Long(cushiontime.getTime()/1000).intValue(), 0);
			}
			//插入购买守护记录
			billcvgDao.insBillcvgList(srcuid, dstuid, giftConfigModel.getGid(), giftConfigModel.getGname(),0, count, new Long(new Date().getTime()/1000).intValue(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 3);
			logger.debug("插入购买骑士守护记录------");
			if(rsc == 0){
				returnModel.setCode(CodeContant.mall_buy_guard_error);
				returnModel.setMessage("购买失败!");
				return returnModel;
			}
			guardInfoService.getUserGuardInfo(srcuid, dstuid, true);
		} else if (gid.intValue() == wangziId) {// 王子守护
			UserGuardInfoModel guardInfoModel = ValueaddServiceUtil.getGuardInfo(srcuid, dstuid);
			Date date = new Date();
			Date startdate = new Date();
			int exp = 30;
			if (guardInfoModel != null) {
				if (guardInfoModel.getGid().intValue() == qishiId) {
					guardInfoService.updUserGuardInfo(exp,
							guardInfoModel.getStarttime(),
							guardInfoModel.getEndtime(),
							guardInfoModel.getCushiontime(), 1, srcuid, dstuid,
							guardInfoModel.getGid().intValue());
				} else {
					long endtime = guardInfoModel.getEndtime();
					long starttime = guardInfoModel.getStarttime();
					date = new Date(endtime * 1000);
					startdate = new Date(starttime * 1000);
				}
			}
			Date newEndtime = DateUtils.getNDaysAfterDate(date, count);
			Date cushiontime = DateUtils.getNDaysAfterDate(newEndtime, 7);

			UserGuardInfoModel userGuardInfoModel = guardInfoService.getUserGuardInfoByUid(srcuid, dstuid, gid);
			int rsc = 0;
			if(userGuardInfoModel != null){
				rsc = guardInfoService.updUserGuardInfo(exp, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), new Long(cushiontime.getTime()/1000).intValue(), 0, srcuid, dstuid, gid);
				//插入成长值记录
				userGuardExpRecordDao.insExpRecord(srcuid, dstuid,gid, exp, 3);
			}else{
				rsc = guardInfoService.addUserGuardInfo(dstuid, srcuid, gid, 1, 0, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), new Long(cushiontime.getTime()/1000).intValue(), 0);
			}
			//插入购买守护记录
			billcvgDao.insBillcvgList(srcuid, dstuid, giftConfigModel.getGid(), giftConfigModel.getGname(),0, count, new Long(new Date().getTime()/1000).intValue(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 3);
			logger.debug("插入购买王子守护记录------");
			if(rsc == 0){
				returnModel.setCode(CodeContant.mall_buy_guard_error);
				returnModel.setMessage("购买失败!");
				return returnModel;
			}
			guardInfoService.getUserGuardInfo(srcuid, dstuid, true);
		} else {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		return returnModel;
	}
	
	@Override
	public ReturnModel addCar(Integer dstuid,Integer gid,Integer count, ReturnModel returnModel){
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("商品不存在");
			return returnModel;
		}
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(dstuid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return returnModel;
		}
		if(giftConfigModel.getSubtype() == 5){
			UserCarInfoModel userCarInfo = userCarInfoService.getUserCarInfo(dstuid, gid, true);
			Date date = new Date();
			Date startdate = new Date();
			if(userCarInfo !=null){
				long endtime = userCarInfo.getEndtime();
				long starttime = userCarInfo.getStarttime();
				date = new Date(endtime*1000);
				startdate = new Date(starttime*1000);
			}
			Date newEndtime = DateUtils.getNDaysAfterDate(date, count);
			int rsc = 0;
			if(userCarInfo != null){
				userCarInfoService.updUserCarInfoUnStatus(dstuid);
				rsc = userCarInfoService.updUserCarInfo(dstuid, gid, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 1);
			}else{
				userCarInfoService.updUserCarInfoUnStatus(dstuid);
				rsc = userCarInfoService.addUserCarInfo(dstuid, gid, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 1);
			}
			if(rsc == 0){
				returnModel.setCode(CodeContant.mall_buy_car_error);
				returnModel.setMessage("购买失败!");
				return returnModel;
			}
			//插入购买座驾记录
			billcvgDao.insBillcvgList(dstuid, 0, giftConfigModel.getGid(), giftConfigModel.getGname(),0, count, new Long(new Date().getTime()/1000).intValue(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 1);
			logger.debug("插入购买座驾守护记录------");
			userCarInfo = userCarInfoService.getUserCarInfo(dstuid, gid, true);
		}else{
			returnModel.setCode(CodeContant.MALLGIFTEXIST);
			returnModel.setMessage("商城道具不存在");
			return returnModel;
		}
		return returnModel;
	}
	
	@Override
	public ReturnModel addExp(Integer dstuid,Integer exp, ReturnModel returnModel){
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(dstuid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return returnModel;
		}
		if(exp != null){
			UserServiceImpl.getInstance().addUserExpByTask(dstuid, exp);
		}
		return returnModel;
	}
	
	@Override
	public ReturnModel addGift(Integer dstuid, Integer gid, Integer count, ReturnModel returnModel){
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("商品不存在");
			return returnModel;
		}
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(dstuid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return returnModel;
		}
		userItemService.insertUserItem(dstuid, gid, count, ItemSource.systemAdd);
		return returnModel;
	}
	
	@Override
	public ReturnModel modifyMoney(int uid, int zhutou, int credit,int type,String desc,ReturnModel returnModel) {
//		if (uid < 10000000 || uid > 100000000) {
//			returnModel.setCode(CodeContant.UIDERROR);
//			returnModel.setMessage("用户UID错误");
//			return returnModel;
//		}
//		
//		if (zhutou <=-100000 || credit <=-100000) {
//			returnModel.setCode(CodeContant.MONEYVALIDERROR);
//			returnModel.setMessage("添加金币或声援值错误");
//			return returnModel;
//		}else if (zhutou > 100000 || credit > 100000) {
//			returnModel.setCode(CodeContant.MONEYVALIDERROR);
//			returnModel.setMessage("添加金币或声援值错误");
//			return returnModel;
//		}
		if(StringUtils.isBlank(desc)) {
			returnModel.setCode(CodeContant.DESCISNULL);
			returnModel.setMessage("备注不能为空");
			return returnModel;
		}
		
		return userService.modifyMoney(uid,zhutou,credit,type,desc);
	}
}
