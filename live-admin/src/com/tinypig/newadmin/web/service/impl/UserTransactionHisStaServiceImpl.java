package com.tinypig.newadmin.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tinypig.admin.model.UserBaseInfoModel;
import com.tinypig.admin.redis.service.UserRedisService;
import com.tinypig.admin.util.JsonUtil;
import com.tinypig.newadmin.web.dao.UserTransactionHisStaMapper;
import com.tinypig.newadmin.web.entity.UserTransactionHisSta;
import com.tinypig.newadmin.web.entity.UserTransactionHisStaParam;
import com.tinypig.newadmin.web.service.UserTransactionHisStaService;

@Service
@Transactional
public class UserTransactionHisStaServiceImpl implements UserTransactionHisStaService {

	@Autowired
	private UserTransactionHisStaMapper userTransactionHisStaDao;
	
	@Override
	public Map<String, Object> getUserTransactionHisListPage(UserTransactionHisStaParam param) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<UserTransactionHisSta>  list = userTransactionHisStaDao.getUserTransactionHisListPage(param);
		List<String> uidList = new ArrayList<String>();
		for (UserTransactionHisSta userTransactionHisSta : list) {
			uidList.add(String.valueOf(userTransactionHisSta.getUid()));
		}
		List<String> userList = UserRedisService.getInstance().getUserBaseInfo(uidList.toArray(new String[0]));
		if(userList!=null&&userList.size()>0) {
			Map<Integer, UserBaseInfoModel> userMap = new HashMap<Integer,UserBaseInfoModel>();
			for (String str : userList) {
				if(StringUtils.isNotBlank(str)) {
					UserBaseInfoModel userBaseInfoModel = JsonUtil.toBean(str, UserBaseInfoModel.class);
					userMap.put(userBaseInfoModel.getUid(), userBaseInfoModel);
				}
			}
			
			for (UserTransactionHisSta userTransactionHisSta : list) {
				UserBaseInfoModel bean = userMap.get(userTransactionHisSta.getUid());
				if(bean!=null) {
					userTransactionHisSta.setNickname(bean.getNickname());
				}
			}
		}
		resultMap.put("rows", list);
		resultMap.put("total", userTransactionHisStaDao.getserTransactionHisTotalCount(param));
		return resultMap;
	}


	@Override
	public List<UserTransactionHisSta> getUserTransactionHisList(UserTransactionHisStaParam param) {
		
		List<UserTransactionHisSta> list = userTransactionHisStaDao.getUserTransactionHisList(param);
		
		List<String> uidList = new ArrayList<String>();
		for (UserTransactionHisSta userTransactionHisSta : list) {
			uidList.add(String.valueOf(userTransactionHisSta.getUid()));
		}
		List<String> userList = UserRedisService.getInstance().getUserBaseInfo(uidList.toArray(new String[0]));
		if(userList!=null&&userList.size()>0) {
			Map<Integer, UserBaseInfoModel> userMap = new HashMap<Integer,UserBaseInfoModel>();
			for (String str : userList) {
				if(StringUtils.isNotBlank(str)) {
					UserBaseInfoModel userBaseInfoModel = JsonUtil.toBean(str, UserBaseInfoModel.class);
					userMap.put(userBaseInfoModel.getUid(), userBaseInfoModel);
				}
			}
			for (UserTransactionHisSta userTransactionHisSta : list) {
				UserBaseInfoModel bean = userMap.get(userTransactionHisSta.getUid());
				if(bean!=null) {
					userTransactionHisSta.setNickname(bean.getNickname());
				}
			}
		}
		return list;
	}


	@Override
	public Map<String, Object> getUserTransactionHisTotal(UserTransactionHisStaParam param) {
		return userTransactionHisStaDao.getUserTransactionHisTotal(param);
	}
}
