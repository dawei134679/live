package com.tinypig.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tinypig.admin.dao.GiftInfoDao;
import com.tinypig.admin.dao.OperateDao;
import com.tinypig.admin.model.ConfigGiftModel;
import com.tinypig.admin.util.Constant;

@Service
public class OperateService {

	/**
	 * 获取 留存数据
	 * @param os
	 * @param channel
	 * @param stime
	 * @param etime
	 * @param pages
	 * @param size
	 * @return
	 */
	public Map<String, Object> getRemain(int os,String channel,Long stime,Long etime,int pages,int size,int loginUid,int platform,int category,int type){
		
//		if (loginUid == 3 || loginUid == 1) {
//			return OperateDao.getInstance().getRemainTest(stime, etime,pages,size);
//		}else {
//			return OperateDao.getInstance().getRemain(os, channel, stime, etime,pages,size);
//		}
		
		return OperateDao.getInstance().getRemain(os, channel, stime, etime,pages,size,platform,category,type);
	}
	
	public Map<String, Object> getSummary(int os,Long stime,Long etime,int pages,int size,int loginUid,String channelCode){
		
//		if (loginUid == 3 || loginUid == 1) {
//			return OperateDao.getInstance().getSummaryTest(stime,etime,pages,size);
//		}else {
//			return OperateDao.getInstance().getSummary(os, stime, etime,pages,size);
//		}
		return OperateDao.getInstance().getSummary(os, stime, etime,pages,size,channelCode);
	}
	
	public List<Map<String, Object>> getWages(int unionid,int uid,String dateYM,int type,int pages,int size){
		
		return OperateDao.getInstance().getWages(unionid, uid, dateYM, type, pages, size);
	}
	
	public int getWagesSize(int unionid,int uid,String dateYM,int type){
		return OperateDao.getInstance().getWagesSize(unionid, uid, dateYM, type);
	}

	
	public List<Map<String, Object>> getSendGiftList(int stime,int etime,int pages,int size){
		
		return OperateDao.getInstance().getSendGiftList(stime, etime, pages, size);
	}
	
	public int getSendGiftListSize(int stime,int etime){
		return OperateDao.getInstance().getSendGiftListSize(stime, etime);
	}
	
	public List<Map<String, Object>> getMallStatic(List<Integer> guardlist,List<Integer> viplist,List<Integer> propList,Long stime,Long etime,int pages,int size){
		return OperateDao.getInstance().getMallStatic(guardlist, viplist, propList, stime, etime, pages, size);
	}
	
	/**
	 * 
	 * @param gid =0 忽略
	 * @param uid =0 忽略
	 * @param type =0全部 =1VIP =2守护
	 * @param stime 购买时间端
	 * @param etime 购买时间端
	 * @param pages
	 * @param size
	 * @return
	 */
	public Map<String, Object> getMallSearch(Integer gid,Integer uid,Integer type,Long stime,Long etime,int pages,int size){
		
		return OperateDao.getInstance().getMallSearch(gid, uid, type, stime, etime, pages, size);
	}
	
	/**
	 * 获取 砸蛋数据统计
	 * @param hammer
	 * @param stime
	 * @param etime
	 * @param ipage
	 * @param irows
	 * @return
	 */
	public Map<String, Object> getEggList(int hammer, int stime, int etime, int ipage, int irows){
		
		return OperateDao.getInstance().getEggList(hammer, stime, etime, ipage, irows);
	}

	/**
	 * 查看具体日期 具体礼物的中奖列表
	 * @param reward_gift_type
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Map<String, Object> getUserListOfEgg(int reward_gift_type,Long starttime,Long endtime){
		return OperateDao.getInstance().getUserListOfEgg(reward_gift_type, starttime, endtime);
	}
	
	/**
	 * 获取礼物促销活动 查询最近30条数据
	 * @param id = 0 所有
	 * @return
	 */
	public Map<String, Object> getPromotList(int id){
		
		return OperateDao.getInstance().getPromotList(id);
	}
	
	public List<Map<String, Object>> getMallGift(){
		
		return OperateDao.getInstance().getMallGift();
	}
	
	public int promotAdd(int gid,String promotionName,int discount,int isvalid,Long starttime,Long endtime,int adminid){
		
		ConfigGiftModel giftInfo = GiftInfoDao.getInstance().getGiftInfo(gid);
		if (giftInfo != null) {
			int disPrice = (giftInfo.getGprice() * discount) / 100;
			return OperateDao.getInstance().promotAdd(gid, promotionName, discount, isvalid, starttime, endtime, adminid, disPrice);
		}
		return 0;
	}
	
	public int promotEdit(int gid,String promotionName,int discount,int isvalid,Long starttime,Long endtime,int id,int adminid){
		
		ConfigGiftModel giftInfo = GiftInfoDao.getInstance().getGiftInfo(gid);
		if (giftInfo != null) {
			int disPrice = (giftInfo.getGprice() * discount) / 100;
			return OperateDao.getInstance().promotEdit(gid, promotionName, discount, isvalid, starttime, endtime, id, disPrice,adminid);
		}
		return 0;
	}
	
	public Map<String, Object> getWithdraw(int isSecc,Long starttime,Long endtime, int uid,int pages,int size){
		return OperateDao.getInstance().getWithdraw(isSecc, starttime, endtime, uid, pages, size);
	}
	
	public Map<String, Object> getReportalbum(int status,Long starttime,Long endtime,int pages,int size){
		return OperateDao.getInstance().getReportalbum(status, starttime, endtime, pages, size);
	}
	
	/**
	 * 审核相册举报
	 * @param id
	 * @param status
	 * @param adminid
	 * @return
	 */
	public int verifyAlbum(int id,int status,int adminid){
		return OperateDao.getInstance().verifyAlbum(id, status, adminid);
	}
	
	/**
	 * 提现 审核
	 * @param id
	 * @return
	 */
	public Map<String, Object> verifyWithdraw(int id,String billno,int adminid,String type){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if ("1".equals(type)) {
			// 审核通过
			try {
				HttpResponse<JsonNode> res = Unirest.get(Constant.business_server_url + "/admin/verifyWithdraw?withdraw=withdrwa&id="+id+"&billno="+billno+"&adminid="+adminid).asJson();
				org.json.JSONObject object = res.getBody().getObject();
				
				if(Integer.parseInt(object.get("code").toString()) == 200){
					map.put("success", 200);
					OperateDao.getInstance().AddOperationLog("pay_withdraw", String.valueOf(id), "审核提现", 2, "{isSecc:0}", "{isSecc:1}", adminid);
				}else{
					map.put("errorMsg", object.get("message").toString());
				}
			} catch (UnirestException e) {
				
				map.put("errorMsg", "UnirestExcep:"+e.getMessage());
				e.printStackTrace();
			}
		}else {
			// 
			int verifyWithdraw = OperateDao.getInstance().verifyWithdraw(id, billno, adminid);
			if (verifyWithdraw != 1) {
				map.put("errorMsg", "审核失败");
			}else {
				map.put("success", 200);
			}
		}
		return map;
	}
	
	/**
	 * 获取动态广场 推荐主播列表数据
	 * @param pages
	 * @param size
	 * @return
	 */
	public Map<String, Object> getRecommend(int pages,int size){
		return OperateDao.getInstance().getRecommend(pages, size);
	}

	/**
	 * 新增广场主播推荐
	 * @param uid
	 * @param sort
	 * @return
	 */
	public int addRecommend(int uid,int sort,String adminname,int adminid){
		return OperateDao.getInstance().addRecommend(uid, sort, adminid, adminname);
	}
	
	/**
	 * 删除 动态广场 推荐主播
	 * @param id
	 * @param adminid
	 * @return
	 */
	public int delRecommend(int id,int adminid){
		return OperateDao.getInstance().delRecommend(id, adminid);
	}
	
	public Map<String, Object> getChannelList(int status,int platform,String channelName,int page,int size){
		
		return OperateDao.getInstance().getChannelList(status,platform,channelName, page, size);
	}
	
	public int addChannel(String channelCode,String channelName,int isvalid,int adminid,int loginport,int platform){
		
		return OperateDao.getInstance().addChannel(channelCode, channelName, isvalid, adminid,loginport,platform);
	}
	
	public int editChannel(String id,int isvalid,String channelCode,String channelName,int platform,int loginport){
		return OperateDao.getInstance().editChannel(id,isvalid,channelCode,channelName,platform,loginport);
	}
	
	public List<Map<String, Object>> getChannelForSelect(){
		return OperateDao.getInstance().getChannelForSelect();
	}
	
	
	/**
	 * 添加操作日志
	 * @param dbname
	 * @param db_id
	 * @param operation_note
	 * @param action
	 * @param previous_version
	 * @param current_version
	 * @param admin_id
	 */
	public void AddOperationLog(String dbname,String db_id,String operation_note,int action,String previous_version,String current_version,int admin_id){
		OperateDao.getInstance().AddOperationLog(dbname, db_id, operation_note, action, previous_version, current_version, admin_id);
	}
	
	public List getRemainList(int os,String channel,Long stime,Long etime,int platform,int category,int type){
		return OperateDao.getInstance().getRemainList(os, channel, stime, etime, platform,category,type);
	}
	
	public Map<String, Object> ltv(int os,String channel,Long stime,Long etime,int pages,int size,int loginUid,int platform,int category,int type){
		
		return OperateDao.getInstance().ltv(os, channel, stime, etime,pages,size,platform,category,type);
	}
}
