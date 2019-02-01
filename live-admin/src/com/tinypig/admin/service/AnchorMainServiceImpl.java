package com.tinypig.admin.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.dao.UserDao;
import com.tinypig.admin.model.UserBaseInfoModel;
import com.tinypig.admin.util.DateUtil;

@Service
public class AnchorMainServiceImpl {
	
	public int editAnchoCover(int uid,String livimage,String pcimg1,String pcimg2,int adminid){
		
		return UserDao.getInstance().editCover(uid, livimage, pcimg1, pcimg2, adminid);
	}

	public Map<String, Object> getAnchorCover(int unionid,int anchoruid,int page,int size){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		int total = 0;
		
		if (anchoruid <= 0) {
			// 无指定主播UID 从工会列表中读取
			mapResult = UnionDao.getIns().getAnchorCoverInUnionList(unionid, 0, page, size);
			
		}else {
			mapResult.put("total", total);
			if (unionid > 0) {
				// 工会主播
				mapResult = UnionDao.getIns().getAnchorCoverInUnionList(unionid, anchoruid, page, size);
			}else {
				
				UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(anchoruid, false);
				if (userBaseInfo != null) {
					if (userBaseInfo.getFamilyId() > 0) {
						mapResult = UnionDao.getIns().getAnchorCoverInUnionList(userBaseInfo.getFamilyId(), anchoruid, page, size);
					}else {
						// 自由主播
						List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
						
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("nickname", userBaseInfo.getNickname());
						map.put("anchorLevel", userBaseInfo.getAnchorLevel());
						map.put("headimage", userBaseInfo.getHeadimage());
						map.put("livimage", userBaseInfo.getLivimage());
						map.put("pcimg1", userBaseInfo.getPcimg1());
						map.put("pcimg2", userBaseInfo.getPcimg2());
						map.put("recommend", userBaseInfo.getRecommend());

						map.put("unionid", 0);
						map.put("anchorid", anchoruid);
						map.put("unionname", "自由人");
						list.add(map);
						
						mapResult.put("rows",list);
						mapResult.put("total", 1);
					}
				}
			}
		}
		
		return mapResult;
	}
	public Map<String, Object> getUserCoverList(int unionid,int anchoruid,int status,int page,int size){
		Map<String, Object> mapResult = new HashMap<String, Object>();
		mapResult = UnionDao.getIns().getUserCoverList(unionid, anchoruid,status, page, size);
		return mapResult;
	}
	
	public Map<String, Object> getUserCoverList(int anchoruid,int status,int page,int size){
		Map<String, Object> mapResult = new HashMap<String, Object>();
		mapResult = UnionDao.getIns().getUserCoverList(anchoruid,status, page, size);
		return mapResult;
	}
	
	public Map<String, Object> getGrant(int uid,int operateUid,int page,int size,String startDate,String endDate,String gStatus,String gsid){
		return UserDao.getInstance().getGrant(uid,operateUid, page, size,startDate,endDate,gStatus,gsid);
	}
	
	public List<Map<String, Object>> getAllGrant(int uid,int operateUid,String startDate,String endDate,String gStatus,String gsid){
		return UserDao.getInstance().getAllGrant(uid,operateUid,startDate,endDate,gStatus,gsid);
	}
	
	public int addGrant(int uid,int zhutou,int credit,String descrip,int adminid){
		
		return UserDao.getInstance().addGrant(uid, zhutou, credit, descrip, adminid);
	}
	
	public String getNickName(int uid){
		UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(uid, false);
		if (userBaseInfo != null) {
			return userBaseInfo.getNickname();
		}else {
			return "undefined";
		}
	}
	
	/**
	 * 获取从后台添加经验值的列表
	 * @param uid
	 * @param stime
	 * @param etime
	 * @param page
	 * @param size
	 * @return
	 */
	public Map<String,Object> getUsedExpList(int uid,Long stime,Long etime,int page,int size){
		return UserDao.getInstance().getUsedExpList(uid, stime, etime, page, size);
	}
	
	/**
	 * 后台添加用户经验值
	 * @param uid
	 * @param exp
	 * @param remarks
	 * @param adminid
	 * @return
	 */
	public int addUserExp(int uid,Long exp,String remarks,int adminid){
		return UserDao.getInstance().addUserExp(uid, exp, remarks, adminid);
	}
	
	public int updUserExpStatus(int id,int status){
		return UserDao.getInstance().updUserExpStatus(id, status);
	}
	
	/**
	 * 艺管获取主播兑换 待审核的列表
	 * @param uid
	 * @param times
	 * @param page
	 * @param size
	 * @return
	 */
	public Map<String, Object> getAnchorCashOfArttube(int unionid,Integer uid,Integer times,int page,int size){
		Long stime = 0L ;
		Long etime = 0L ;
		
		Calendar now = Calendar.getInstance(); 

		if (times == 5) {
			// 上月的21号 ~上月底
			etime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1);
			now.add(Calendar.MONTH, -1);
			stime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 21);
			
		}else if (times == 15) {
			// 本月 1号~10号
			etime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 11);
			stime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1);
		}else if (times == 25) {
			// 本月 11号 ~ 20号
			etime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 21);
			stime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 11);
		}
		
		return UserDao.getInstance().getAnchorCashOfArttube(unionid,uid, stime, etime, page, size);
	}
	
	public int verifyByArttube(int type,int id,int uid,int adminid){
		return UserDao.getInstance().verifyByArttube(type, id, uid, adminid);
	}
	
	/**
	 * 艺管获取主播兑换 待审核的列表
	 * @param uid
	 * @param times
	 * @param page
	 * @param size
	 * @return
	 */
	public Map<String, Object> getAnchorCashOfOperate(int uid,int unionid,Integer times,int page,int size){
		Long stime = 0L ;
		Long etime = 0L ;
		
		Calendar now = Calendar.getInstance(); 

		if (times == 5) {
			// 上月的21号 ~上月底
			etime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1);
			now.add(Calendar.MONTH, -1);
			stime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 21);
			
		}else if (times == 15) {
			// 本月 1号~10号
			etime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 11);
			stime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1);
		}else if (times == 25) {
			// 本月 11号 ~ 20号
			etime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 21);
			stime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 11);
		}else {
			return null;
		}
		
		return UserDao.getInstance().getAnchorCashOfOperate(uid,unionid,times,stime, etime, page, size);
	}
	
	public int verifyByOperate(int type,int times,int uid,int adminid){
		
		Long stime = 0L ;
		Long etime = 0L ;
		
		Calendar now = Calendar.getInstance(); 

		if (times == 5) {
			// 上月的21号 ~上月底
			etime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1);
			now.add(Calendar.MONTH, -1);
			stime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 21);
			
		}else if (times == 15) {
			// 本月 1号~10号
			etime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 11);
			stime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1);
		}else if (times == 25) {
			// 本月 11号 ~ 20号
			etime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 21);
			stime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 11);
		}else {
			return 0;
		}
		return UserDao.getInstance().verifyByOperate(type, stime,etime, uid, adminid);
	}
	
	/**
	 * 艺管获取主播兑换 待审核的列表
	 * @param uid
	 * @param times
	 * @param page
	 * @param size
	 * @return
	 */
	public Map<String, Object> getCashCreditList(int uid,int status,int page,int size,
			long starttm,long endtm){
//		Long stime = 0L ;
//		Long etime = 0L ;
//		
//		Calendar now = Calendar.getInstance();
//		
//		if (times == 5) {
//			// 上月的21号 ~上月底
//			etime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1);
//			now.add(Calendar.MONTH, -1);
//			stime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 21);
//			
//		}else if (times == 15) {
//			// 本月 1号~10号
//			etime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 11);
//			stime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1);
//		}else if (times == 25) {
//			// 本月 11号 ~ 20号
//			etime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 21);
//			stime = DateUtil.getYMD(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 11);
//		}else {
//			return null;
//		}
		
		return UserDao.getInstance().getCashCreditList(starttm, endtm,uid,status, page, size);
	}
	
	/**
	 * 审核通过，修改主播封面信息
	 * @param uId	主播编号
	 * @param picCover	封面地址
	 * @param picCover1	4:3封面地址
	 * @param picCover2	16:9封面地址
	 * @return
	 */
	public int updCover(int id,int uid,String picCover,String picCover1,String picCover2){
		UserDao.getInstance().updUserCover(id,uid,picCover, picCover1, picCover2);
		return UserDao.getInstance().updUserCoverStatus(id);
	}
	
	/**
	 * 审核驳回，修改主播封面信息
	 * @param uId	主播编号
	 * @param picCover	封面地址
	 * @param picCover1	4:3封面地址
	 * @param picCover2	16:9封面地址
	 * @return
	 */
	public int updCover(int id,String cause){
		return UserDao.getInstance().updUserCover(id,cause);
	}
}
