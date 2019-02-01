package com.tinypig.newadmin.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.admin.dao.GiftInfoDao;
import com.tinypig.admin.model.ConfigGiftModel;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.web.dao.ActivityGiftDao;
import com.tinypig.newadmin.web.entity.ActivityGift;
import com.tinypig.newadmin.web.service.ActivityGiftService;
import com.tinypig.newadmin.web.vo.ActivityGiftVo;

@Service
public class ActivityGiftServiceImpl implements ActivityGiftService {

	@Autowired
	private ActivityGiftDao actGiftDao;

	@Override
	public List<ActivityGiftVo> selectList(ActivityGiftVo gift) {
		return actGiftDao.selectList(gift);
	}

	@Override
	public Integer selectCount(ActivityGiftVo gift) {
		return actGiftDao.selectCount(gift);
	}

	@Override
	public ActivityGiftVo selectByPrimaryKey(Integer id) {
		return actGiftDao.selectByPrimaryKey(id);
	}

	@Override
	public Boolean updateByObject(ActivityGift gift) {
		if (gift != null) {
			int result = actGiftDao.updateByPrimaryKeySelective(gift);
			if (result == 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean insertByObject(ActivityGift gift) {
		if (gift != null) {
			int result = actGiftDao.insertSelective(gift);
			if (result == 1) {
				if (gift.getAtype() == 1) {
					// 周星
					ConfigGiftModel giftInfo = GiftInfoDao.getInstance().getGiftInfo(gift.getGid());
					if (giftInfo != null && giftInfo.getGprice() > 0) {

						String formatDate = DateUtil.formatDate(new Date(gift.getStime()*1000), "yyyyMMdd");
						RedisOperat.getInstance().zadd(RedisContant.host, RedisContant.port6379,
								RedisContant.zxTimes + formatDate, giftInfo.getGprice(), gift.getGid().toString());
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean deleteByPrimaryKey(Integer id) {
		ActivityGiftVo selectByPrimaryKey = actGiftDao.selectByPrimaryKey(id);
		if (selectByPrimaryKey != null ) {
			int result = actGiftDao.deleteByPrimaryKey(id);
			if (result == 1) {
				if (selectByPrimaryKey.getAtype() == 1) {
					// 周星
					String formatDate = DateUtil.formatDate(new Date(selectByPrimaryKey.getStime()*1000), "yyyyMMdd");
					RedisOperat.getInstance().zrem(RedisContant.host, RedisContant.port6379, RedisContant.zxTimes + formatDate, selectByPrimaryKey.getGid().toString());
				}
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getGiftList() {
		return actGiftDao.getGiftList();
	}
	
	@Override
	public ArrayList<HashMap<String, Object>> getAllGiftList() {
		return actGiftDao.getAllGiftList();
	}
}
