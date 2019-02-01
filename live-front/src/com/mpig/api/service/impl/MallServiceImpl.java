package com.mpig.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.mpig.api.controller.MallController;
import com.mpig.api.dao.IMallDao;
import com.mpig.api.dao.IUserGuardExpRecordDao;
import com.mpig.api.dao.IUserGuardInfoDao;
import com.mpig.api.dictionary.lib.GiftPromotionConfigLib;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.model.ConfigGiftModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.model.UserCarInfoModel;
import com.mpig.api.model.UserGuardInfoModel;
import com.mpig.api.model.UserMallItemModel;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.model.ValueaddPrivilegeModel;
import com.mpig.api.modelcomet.RunWayCMod;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IBillService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.IMallService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.IUserCarInfoService;
import com.mpig.api.service.IUserGuardInfoService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.service.IUserItemService.ItemSource;
import com.mpig.api.service.IUserMallItemService;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.IUserTransactionHisService;
import com.mpig.api.service.IUserVipInfoService;
import com.mpig.api.utils.ChatMessageUtil;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.ValueaddServiceUtil;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class MallServiceImpl implements IMallService{

	private static final Logger logger = Logger.getLogger(MallController.class);
	
	@Autowired
	private IMallDao mallDao;
	@Autowired
	private IUserGuardExpRecordDao userGuardExpRecordDao;
	@Autowired
	private IUserGuardInfoDao guardInfoDao;
	@Resource
	private IUserService userService;
	@Resource
	private IUserGuardInfoService guardInfoService;
	@Resource
	private IUserVipInfoService vipInfoService;
	@Resource
	private IBillService billService;
	@Resource
	private IUserMallItemService userMallItemService;
	@Resource
	private IConfigService configService;
	@Resource
	private IUserItemService userItemService;
	@Resource
	private IRoomService roomService;
	@Resource
	private IUserCarInfoService userCarInfoService;
	@Resource
	private IUserTransactionHisService userTransactionHisService;
	
	
	@Override
	public void buyGuard(Integer gid,String gname, Integer uid, Integer dstuid,
			Integer count, Integer wealths, Double gets, Integer starttime,
			Integer endtime, Integer cushiontime,Integer price,Integer pricetotal, Integer realprice,Integer realpricetotal, Byte os, Integer exp, Integer inroom, Integer isopen, ReturnModel returnModel) {
		Long lgNow = System.currentTimeMillis() / 1000;
		/**
		 * 扣费
		 */ 
		UserBaseInfoModel dstUserBaseinfo = userService.getUserbaseInfoByUid(dstuid, false);
		if (dstUserBaseinfo == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return;
		}
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return;
		}
		if (realpricetotal <= 0) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("非法请求");
			return;
		}
		if (wealths < 0) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("非法请求");
			return;
		}
		// 送礼者 扣费
		int res = userService.updUserAssetBySendUid(uid, realpricetotal, wealths);
		if (res == 0) {
			returnModel.setCode(CodeContant.MONEYDEDUCT);
			returnModel.setMessage("扣费失败");
			return;
		}
		// 收礼者资产更新
		double getsTotal = gets;
		if(lgNow>1483200000){
			Map<String, String> support = GiftPromotionConfigLib.getSupport();
			String unionId = support.get(uid.toString());
			if(StringUtils.isNotEmpty(unionId)){
				if(dstUserBaseinfo != null && unionId.equals(dstUserBaseinfo.getFamilyId().toString())){
					gets = new Double(0);
				}
			}
		}
		
		res = userService.updUserAssetByGetUid(dstuid, gets,getsTotal);
		if (res == 0) {
			returnModel.setCode(CodeContant.MONEYACCEPT);
			returnModel.setMessage("收礼失败");
			return;
		}
		/**
		 * 加入身份记录 更新缓存
		 */
		UserGuardInfoModel userGuardInfoModel = guardInfoService.getUserGuardInfoByUid(uid, dstuid, gid);
		int rsc = 0;
		if(userGuardInfoModel != null){
			rsc = guardInfoService.updUserGuardInfo(exp,starttime, endtime, cushiontime, 0,uid,dstuid,gid);
			//插入成长值记录
			userGuardExpRecordDao.insExpRecord(uid, dstuid,gid, exp, 3);
		}else{
			rsc = guardInfoService.addUserGuardInfo(dstuid, uid, gid, 1, 0, starttime, endtime, cushiontime, 0);
		}
		if(rsc == 0){
			returnModel.setCode(CodeContant.mall_buy_guard_error);
			returnModel.setMessage("购买失败!");
			return;
		}
		UserGuardInfoModel guardInfoModel = guardInfoService.getUserGuardInfo(uid, dstuid, true);
		if (getsTotal > 0) {
			// 添加主播与用户关系
			RelationRedisService.getInstance().addDstGetSrc(uid, dstuid, (double) gets);
			// 修改粉丝的排序score 送礼值排序
			Double db = RelationRedisService.getInstance().isFansScore(dstuid, uid);
			if (db != null) {
				RelationRedisService.getInstance().addFans(dstuid, uid, db + gets, "on");
			}
			// 添加用户与主播的关系
			RelationRedisService.getInstance().addSrcSendDst(uid, dstuid, (double) realpricetotal);
			// 关注主播排序:送礼值+登录次数
			Double dlFollows = RelationRedisService.getInstance().isFollows(uid, dstuid);
			if (dlFollows == null) {
				// RelationRedisService.getInstance().addFollows(srcUid,
				// dstUid, (double) (sends * 1000), "on");
			} else {
				RelationRedisService.getInstance().addFollows(uid, dstuid, realpricetotal * 1000 + dlFollows, "on");
			}
		}
		/**
		 * 加入消费订单
		 */
		UserBaseInfoModel userBaseinfo = userService.getUserbaseInfoByUid(uid, false);
		// 收礼者的资产
		UserAssetModel getUserAssetModel = userService.getUserAssetByUid(dstuid, false);
		res = billService.insertBill(gid, count, realprice, uid, sendUserAssetModel.getMoney() - realpricetotal,
				sendUserAssetModel.getWealth() + realpricetotal, sendUserAssetModel.getCredit(), dstuid,
				getUserAssetModel.getMoney(), getUserAssetModel.getWealth(), getUserAssetModel.getCredit(),
				lgNow, 1, gets, os, "", userBaseinfo.getNickname(),
				dstUserBaseinfo.getNickname(),dstUserBaseinfo.getFamilyId());

		if (res == 0) {
			logger.error("<sendGift>插入账单失败: gid="+gid+" count="+count+" price="+realprice+" srcUid="+uid+" surplus="+(sendUserAssetModel.getMoney() - realpricetotal)+" wealth="+
					(sendUserAssetModel.getWealth() + realpricetotal)+" credit="+sendUserAssetModel.getCredit()+" dstUid="+dstuid+" dstmoney="+
					getUserAssetModel.getMoney()+" dstWealth="+getUserAssetModel.getWealth()+" dstCredit="+getUserAssetModel.getCredit()+"  addtime="+
					lgNow+" type="+1+" dstGets="+gets+" os="+os+" bak="+" srcNickname="+ userBaseinfo.getNickname()+" dstNickname="+
					dstUserBaseinfo.getNickname());
		}
		roomService.updUserLevel(uid, dstuid, dstuid, 1, 1);
		/**
		 * 加入商城订单
		 */
		res = mallDao.addMallInfo(gid, gname, uid, userBaseinfo.getNickname(), dstuid, dstUserBaseinfo.getNickname(), count, price, realprice, pricetotal, realpricetotal, (int)gets.doubleValue(),guardInfoModel.getStarttime(),guardInfoModel.getEndtime(),1);
		
		userTransactionHisService.saveUserTransactionHis(13, uid, 0.00, (long)realpricetotal, System.currentTimeMillis(), "", 1);
		
		ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(gid, guardInfoModel.getLevel());
		if(inroom==1){
			Integer sort = 0;
			RelationRedisService.getInstance().userExitRoom(uid, dstuid);
			int userlevel = userBaseinfo.getUserLevel();
			userlevel = userlevel+1;
			if(privilegeModel!=null){
				sort = userlevel+privilegeModel.getAddRankScore(); 
			}
			RelationRedisService.getInstance().guardUserIntoRoom(uid, dstuid, Double.valueOf(sort));
			RelationRedisService.getInstance().allUserIntoRoom(uid, dstuid, Double.valueOf(sort));
		}
		
		//记录房间守护排序信息
		UserRedisService.getInstance().setRoomAllGuardSort(uid,dstuid,realpricetotal);
		
		//插入房间所有的守护信息
		List<UserGuardInfoModel> guardList = guardInfoService.selUserAllGardInfoByRoomId(dstuid);
		UserRedisService.getInstance().set(RedisContant.roomAllGuard+dstuid, JSONArray.toJSONString(guardList));
		UserRedisService.getInstance().setRank(String.valueOf(uid), String.valueOf(dstuid), realpricetotal,gets.intValue());
		/**
		 * 发送跑道信息
		 */
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		RunWayCMod msgBody = new RunWayCMod();
		map = new HashMap<String, Object>(); //感谢“用户昵称A”为“用户昵称B”开通了 3个月 XXX 守护，一路上有你，TA不再孤单
		map.put("name", "感谢 ");
		map.put("color", "");
		list.add(map);
		map = new HashMap<String, Object>(); 
		map.put("name", userBaseinfo.getNickname());
		map.put("color", "#fff08c");
		list.add(map);
		map = new HashMap<String, Object>(); 
		map.put("name", " 为 ");
		map.put("color", "");
		list.add(map);
		map = new HashMap<String, Object>(); 
		map.put("name", dstUserBaseinfo.getNickname());
		map.put("color", "#fff08c");
		list.add(map);
		map = new HashMap<String, Object>(); 
		map.put("name", " 开通了");
		map.put("color", "");
		list.add(map);
		map = new HashMap<String, Object>(); 
		map.put("name", count.toString());
		map.put("color", "#fff08c");
		list.add(map);
		map = new HashMap<String, Object>(); 
		map.put("name", "个月");
		map.put("color", "");
		list.add(map);
		map = new HashMap<String, Object>(); 
		map.put("name", gname);
		map.put("color", "#79ebd2");
		list.add(map);
		map = new HashMap<String, Object>(); 
		map.put("name", "，一路上有你，TA不再孤单！");
		map.put("color", "");
		list.add(map);
		map = new HashMap<String, Object>(); 
		map.put("list", list);
		msgBody.setData(map);
		msgBody.setAnchorUid(dstUserBaseinfo.getUid());
		msgBody.setAnchorName(dstUserBaseinfo.getNickname());
		logger.info("跑道的发送内容 : "+map);

		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
				"appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody));

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
				.field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(msgBody))
				.field("sign", signParams).asJsonAsync();
		
		/**
		 * 发送主播端推送
		 */
		List<Map<String, Object>> toUserList = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> toUserMap = new HashMap<String, Object>();
		//“用户昵称”为您开通了x月 “XXX守护”，获得 XXX 声援
		toUserMap = new HashMap<String, Object>();
		toUserMap.put("name", userBaseinfo.getNickname());
		toUserMap.put("color", "#fff08c");
		toUserList.add(toUserMap);
		
		toUserMap = new HashMap<String, Object>();
		toUserMap.put("name", "为您开通了");
		toUserMap.put("color", "");
		toUserList.add(toUserMap);
		
		toUserMap = new HashMap<String, Object>();
		toUserMap.put("name", count);
		toUserMap.put("color", "#fff08c");
		toUserList.add(toUserMap);
		
		toUserMap = new HashMap<String, Object>();
		toUserMap.put("name", "月");
		toUserMap.put("color", "");
		toUserList.add(toUserMap);
		
		toUserMap = new HashMap<String, Object>();
		toUserMap.put("name", gname);
		toUserMap.put("color", "#79ebd2");
		toUserList.add(toUserMap);
		
		toUserMap = new HashMap<String, Object>();
		toUserMap.put("name", "，获得 ");
		toUserMap.put("color", "");
		toUserList.add(toUserMap);
		
		toUserMap = new HashMap<String, Object>();
		toUserMap.put("name", gets);
		toUserMap.put("color", "#fff08c");
		toUserList.add(toUserMap);
		
		toUserMap = new HashMap<String, Object>();
		toUserMap.put("name", " 魅力");
		toUserMap.put("color", "#fff08c");
		toUserList.add(toUserMap);
		
		toUserMap = new HashMap<String, Object>();
		toUserMap.put("list", toUserList);
		List<Map<String, Object>> giftList = new ArrayList<Map<String, Object>>();
		if(isopen ==0){
			int shrqzhuId = 29;
			int giftCount = privilegeModel.getRqCount();
			userItemService.insertUserItem(uid, shrqzhuId, giftCount, ItemSource.Activity);
			HashMap<String, Object> giftMap = new HashMap<String, Object>();
			giftMap.put("gid", shrqzhuId);
			giftMap.put("num", giftCount);
			giftList.add(giftMap);
		}
		
		ChatMessageUtil.sendGiftUpdateAssetAndBag(dstuid,null, null, toUserMap,giftList, null, null, null);
		/**
		 *  发送聊天区信息
		 */
		UserAssetModel userAssetModel = userService.getUserAssetByUid(uid, false);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("surplus", userAssetModel.getMoney());
		dataMap.put("creditTotal", getUserAssetModel.getCreditTotal());
		dataMap.put("guardIcon", privilegeModel.getIconId());
		returnModel.setData(dataMap);
		ChatMessageUtil.openServiceNotice(dstuid, privilegeModel.getTid(),userBaseinfo.getUid(),userBaseinfo.getNickname(),new Long(getUserAssetModel.getCreditTotal()), map);
	}
	
	@Override
	public void buyVip(Integer gid,String gname, Integer uid, Integer dstuid,Integer starttime,
			Integer endtime, Integer count, Integer wealths, Integer price,Integer pricetotal, Integer realprice,Integer realpricetotal, ReturnModel returnModel){
		/**
		 * 扣费
		 */ 
		UserBaseInfoModel dstUserBaseinfo = userService.getUserbaseInfoByUid(dstuid, false);
		if(dstUserBaseinfo == null){
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return;
		}
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return;
		}
		if (realpricetotal <= 0) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("非法请求");
			return;
		}
		if (wealths < 0) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("非法请求");
			return;
		}
		// 送礼者 扣费
		int res = userService.updUserAssetBySendUid(uid, realpricetotal, wealths);
		if (res == 0) {
			returnModel.setCode(CodeContant.MONEYDEDUCT);
			returnModel.setMessage("扣费失败");
			return;
		}
		roomService.updUserLevel(uid, dstuid, dstuid, 1, 1);
		/**
		 * 加入身份记录 更新缓存
		 */
		UserVipInfoModel userVipInfoModel = vipInfoService.getUserVipInfoByUid(dstuid, gid);
		int rsc = 0;
		if(userVipInfoModel != null){
			rsc = vipInfoService.updUserVipInfo(dstuid,starttime, endtime, 0,gid);
		}else{
			rsc = vipInfoService.addUserVipInfo(dstuid, gid, starttime, endtime, 0);
		}
		if(rsc == 0){
			returnModel.setCode(CodeContant.mall_buy_vip_error);
			returnModel.setMessage("购买失败!");
			return;
		}
		UserVipInfoModel  vipInfoModel = vipInfoService.getUserVipInfo(dstuid, true);
		UserBaseInfoModel userBaseinfo = userService.getUserbaseInfoByUid(uid, false);
		UserRedisService.getInstance().setRank(String.valueOf(uid), String.valueOf(dstuid), realpricetotal,0);
		res = mallDao.addMallInfo(gid, gname, uid, userBaseinfo.getNickname(), dstuid, dstUserBaseinfo.getNickname(), count, price, realprice, pricetotal, realpricetotal, 0 ,vipInfoModel.getStarttime(),vipInfoModel.getEndtime(),2);
		
		userTransactionHisService.saveUserTransactionHis(14, uid, 0.00, (long)realpricetotal, System.currentTimeMillis(), "", 1);
	}
	
	@Override
	public void buyProps(Integer gid,String gname, Integer uid, Integer dstuid,Integer type, Integer subtype, Integer starttime,
			Integer endtime, Integer count, Integer wealths, Integer price,Integer pricetotal, Integer realprice,Integer realpricetotal, ReturnModel returnModel){
		UserBaseInfoModel dstUserBaseinfo = userService.getUserbaseInfoByUid(dstuid, false);
		if (dstUserBaseinfo == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return;
		}
		/**
		 * 扣费
		 */ 
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return;
		}
		if (realpricetotal <= 0) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("非法请求");
			return;
		}
		if (wealths < 0) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("非法请求");
			return;
		}
		// 送礼者 扣费
		int res = userService.updUserAssetBySendUid(uid, realpricetotal, wealths);
		if (res == 0) {
			returnModel.setCode(CodeContant.MONEYDEDUCT);
			returnModel.setMessage("扣费失败");
			return;
		}
		roomService.updUserLevel(uid, dstuid, dstuid, 1, 1);
		UserMallItemModel userMallItemModel = userMallItemService.getUserAllMallItemBySubtype(dstuid, subtype);
		int rsc = 0;
		if(userMallItemModel != null){
			rsc = userMallItemService.updUserMallItem(dstuid, subtype, starttime, endtime);
		}else{
			rsc = userMallItemService.addUserMallItem(dstuid, gid, type, subtype, count, starttime, endtime);
		}
		if(rsc == 0){
			returnModel.setCode(CodeContant.mall_buy_props_error);
			returnModel.setMessage("购买失败!");
			return;
		}
		UserRedisService.getInstance().setRank(String.valueOf(uid), String.valueOf(dstuid), realpricetotal,0);
		userMallItemModel = userMallItemService.getItem(dstuid, subtype, true);
		UserBaseInfoModel userBaseinfo = userService.getUserbaseInfoByUid(uid, false);
		res = mallDao.addMallInfo(gid, gname, uid, userBaseinfo.getNickname(), dstuid, dstUserBaseinfo.getNickname(), count, price, realprice, pricetotal, realpricetotal, 0 ,userMallItemModel.getStarttime(),userMallItemModel.getEndtime(),3);
	}
	
	@Override
	public void buyCar(Integer gid,String gname, Integer uid, Integer dstuid,Integer type, Integer subtype, Integer starttime,
			Integer endtime, Integer count, Integer wealths, Integer price,Integer pricetotal, Integer realprice,Integer realpricetotal, ReturnModel returnModel){
		UserBaseInfoModel dstUserBaseinfo = userService.getUserbaseInfoByUid(dstuid, false);
		if (dstUserBaseinfo == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return;
		}
		
		if (realpricetotal <= 0) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("非法请求");
			return;
		}
		if (wealths < 0) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("非法请求");
			return;
		}
		
		//扣费
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return;
		}
		// 送礼者 扣费
		int res = userService.updUserAssetBySendUid(uid, realpricetotal, wealths);
		if (res == 0) {
			returnModel.setCode(CodeContant.MONEYDEDUCT);
			returnModel.setMessage("扣费失败");
			return;
		}
		roomService.updUserLevel(uid, dstuid, dstuid, 1, 1);
		UserCarInfoModel userCarInfo = userCarInfoService.getUserCarInfo(dstuid, gid, true);
		int rsc = 0;
		if(userCarInfo != null){
			userCarInfoService.updUserCarInfoUnStatus(dstuid);
			rsc = userCarInfoService.updUserCarInfo(dstuid, gid, starttime, endtime, 1);
		}else{
			userCarInfoService.updUserCarInfoUnStatus(dstuid);
			rsc = userCarInfoService.addUserCarInfo(dstuid, gid, starttime, endtime, 1);
		}
		if(rsc == 0){
			returnModel.setCode(CodeContant.mall_buy_car_error);
			returnModel.setMessage("购买失败!");
			return;
		}
		UserRedisService.getInstance().setRank(String.valueOf(uid), String.valueOf(dstuid), realpricetotal,0);
		userCarInfo = userCarInfoService.getUserCarInfo(dstuid, gid, true);
		UserBaseInfoModel userBaseinfo = userService.getUserbaseInfoByUid(uid, false);
		res = mallDao.addMallInfo(gid, gname, uid, userBaseinfo.getNickname(), dstuid, dstUserBaseinfo.getNickname(), count, price, realprice, pricetotal, realpricetotal, 0 ,userCarInfo.getStarttime(),userCarInfo.getEndtime(),4);
		userTransactionHisService.saveUserTransactionHis(15, uid, 0.00, (long)realpricetotal, System.currentTimeMillis(), "", 1);
	}
	
	@Override
	public void getMyProps(int uid, ReturnModel returnModel){
		long nowtime = System.currentTimeMillis()/1000;
		Map<String,Object> getMyProps = new HashMap<String,Object>();
		List<UserGuardInfoModel> guardInfoModels = guardInfoDao.selUserAllGardInfo(uid);
		List<Object> userGuardInfoList = new ArrayList<Object>();
		for(UserGuardInfoModel guardInfoModel : guardInfoModels){
			Map<String, Object> userGuardInfos = new HashMap<String, Object>();
			int anchorId= guardInfoModel.getRoomid();
			UserBaseInfoModel userBaseinfo = userService.getUserbaseInfoByUid(anchorId, false);
			userGuardInfos.put("anchorName", userBaseinfo.getNickname());
			userGuardInfos.put("anchorLevel", userBaseinfo.getAnchorLevel());
			userGuardInfos.put("gid", guardInfoModel.getGid());
			userGuardInfos.put("exp", guardInfoModel.getExp());
			userGuardInfos.put("level", guardInfoModel.getLevel());
			userGuardInfos.put("headimage", userBaseinfo.getHeadimage());
			userGuardInfos.put("uid", userBaseinfo.getUid());
			userGuardInfos.put("status", userBaseinfo.getLiveStatus());
			Long days = DateUtils.getDaysBetweenTime(nowtime, new Long(guardInfoModel.getEndtime()));
			userGuardInfos.put("surplusDays", days);
			userGuardInfoList.add(userGuardInfos);
		}
		getMyProps.put("guardInfos", userGuardInfoList);
		UserVipInfoModel vipInfoModel = ValueaddServiceUtil.getVipInfo(uid);
		Map<String, Object> userVipInfos = new HashMap<String, Object>();
		List<Object> userVipInfoList = new ArrayList<Object>();
		if(vipInfoModel != null){
			userVipInfos.put("gid", vipInfoModel.getGid());
			Long days = DateUtils.getDaysBetweenTime(nowtime, new Long(vipInfoModel.getEndtime()));
			userVipInfos.put("surplusDays", days);
			userVipInfoList.add(userVipInfos);
		}
		getMyProps.put("vipInfo", userVipInfoList);
		UserMallItemModel mallItemModel = ValueaddServiceUtil.getMengzhuCard(uid);
		List<Object> userMallItemList = new ArrayList<Object>();
		if(mallItemModel != null){
			Map<String, Object> userMallItems = new HashMap<String, Object>();
			userMallItems.put("gid", mallItemModel.getGid());
			ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(mallItemModel.getGid());
			userMallItems.put("gname", giftConfigModel.getGname());
			userMallItems.put("gimg", giftConfigModel.getIcon());
			Long days = DateUtils.getDaysBetweenTime(nowtime, new Long(mallItemModel.getEndtime()));
			userMallItems.put("surplusDays", days);
			userMallItemList.add(userMallItems);
		}
		getMyProps.put("userMallItem", userMallItemList);
		
		List<UserCarInfoModel> userValidCars = userCarInfoService.getUserValidCars(uid);
		List<Object> userValidCarsList = new ArrayList<Object>();
		for(UserCarInfoModel userCarInfoModel : userValidCars){
			ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(userCarInfoModel.getGid());
			Map<String, Object> userCarInfo = new HashMap<String, Object>();
			userCarInfo.put("gid", userCarInfoModel.getGid());
			userCarInfo.put("gname", giftConfigModel.getGname());
			userCarInfo.put("gimg", giftConfigModel.getIcon());
			userCarInfo.put("carStatus", userCarInfoModel.getStatus());
			Long days = DateUtils.getDaysBetweenTime(nowtime, new Long(userCarInfoModel.getEndtime()));
			userCarInfo.put("surplusDays", days);
			userValidCarsList.add(userCarInfo);
		}
		getMyProps.put("cars", userValidCarsList);
		returnModel.setData(getMyProps);
	}
}
