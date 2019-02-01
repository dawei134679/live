package com.tinypig.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.dao.UserDao;
import com.tinypig.admin.model.UnionModel;
import com.tinypig.admin.model.UserBaseInfoModel;

@Service
public class UnionServiceImpl {

	/**
	 * 统计扶持号 扶持细节
	 * @return
	 */
	public List<Map<String, Object>> getSupportList(){
		return UnionDao.getIns().getSupportList();
	}
	
	/**
	 * 获取扶持账号列表
	 * @param isvalid =9全部 =1有效 =0无效
	 * @param unionid =0全部
	 * @return
	 */
	public Map<String, Object> getSupport(int isvalid,int unionid, int page,int size){
		
		return UnionDao.getIns().getSupport(isvalid, unionid, page, size);
	}
	
	/**
	 * 修改 扶持号 扶持状态
	 * @param unionid
	 * @param uid
	 * @param isvalid
	 * @param adminid
	 * @return
	 */
	public int editSupport(Integer unionid, int uid,int amount,int isvalid,int adminid){
		

		UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(uid, false);
		if (userBaseInfo == null) {
			return 0;
		}
		
		UnionModel unionById = UnionDao.getIns().getUnionById(unionid);
		if (unionById == null) {
			return 0;
		}
		
		Map<String, Object> supportInfo = UnionDao.getIns().getSupportByUid(uid, unionid,9);
		if (supportInfo == null) {
			// 不存在
			return 0;
		}else if (isvalid == Integer.valueOf(supportInfo.get("isvalid").toString())) {
			// 状态没改变
			return 0;
		}
		
		Map<String, Object> checkSupportByUid = UnionDao.getIns().checkSupportByUid(uid, unionid);
		if (checkSupportByUid == null) {
			return UnionDao.getIns().editSupport(unionid, uid,amount, isvalid, adminid);
		}else {
			// 已经存在扶持其他公会
			return 0;
		}
	}
	
	public Map<String, Object> getSupportConsume(Integer uid,Integer unionid,int stime,int etime,Integer page,Integer size){
		
		return UnionDao.getIns().getSupportConsume(uid, unionid, stime, etime, page, size);
	}
	
	/**
	 * 新增扶持账号
	 * @param unionid
	 * @param uid
	 * @param isvalid
	 * @param remarks
	 * @param admin_id
	 * @return
	 */
	public int addSupport(int unionid,int uid,int isvalid,String remarks,int admin_id){
		
		UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(uid, false);
		if (userBaseInfo == null) {
			return 0;
		}
		
		UnionModel unionById = UnionDao.getIns().getUnionById(unionid);
		if (unionById == null) {
			return 0;
		}

		Map<String, Object> supportInfo = UnionDao.getIns().getSupportByUid(uid, unionid,9);
		if (supportInfo == null) {
			// 不存在
			if (isvalid == 1) {
				// 判断该扶持号 有没有扶持其他公会
				Map<String, Object> checkSupportByUid = UnionDao.getIns().checkSupportByUid(uid, unionid);
				if (checkSupportByUid != null) {
					return 0;
				}
			}
			return UnionDao.getIns().addSupport(unionid, uid, isvalid, remarks, admin_id);
		}else {
			// 已存在
		}
		return 0;
	}
}
