package com.tinypig.newadmin.web.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tinypig.admin.util.DateUtil;
import com.tinypig.newadmin.web.dao.GameDao;
import com.tinypig.newadmin.web.service.IGameService;

@Service
public class GameServiceImpl implements IGameService {
	
	@Override
	public Map<String, Object> getBetWinList(int uid, String stime, String etime,int page,int size) {
		Long istime = DateUtil.dateToLong(stime, "yyyy-MM-dd", "", 0);
		Long ietime = DateUtil.dateToLong(etime, "yyyy-MM-dd", "", 0);
		
		System.out.println("stime:"+istime+" etime:"+ietime);
		
		Map<String, Object> getbetwinlistOfTime = GameDao.getInstance().getbetwinlistOfTime(istime, ietime, uid,page,size);
		
		return getbetwinlistOfTime;
	}

	@Override
	public int getBetWins(int uid, String stime, String etime) {

		Long istime = DateUtil.dateToLong(stime, "yyyy-MM-dd", "", 0);
		Long ietime = DateUtil.dateToLong(etime, "yyyy-MM-dd", "", 0);
		
		Integer getbetwinlistsOfTime = GameDao.getInstance().getbetwinlistsOfTime(istime, ietime, uid);
		
		return getbetwinlistsOfTime;
	}

	@Override
	public List<Map<String, Object>> getNidBetWinlist(Integer nid, String stime, String etime) {
		Long istime = DateUtil.dateToLong(stime, "yyyy-MM-dd", "", 0);
		Long ietime = DateUtil.dateToLong(etime, "yyyy-MM-dd", "", 0);
		
		List<Map<String, Object>> getNidBetWinlist = GameDao.getInstance().getNidBetWinlist(istime, ietime, nid);
		
		return getNidBetWinlist;
	}

	@Override
	public int getNidBetWins(Integer nid, String stime, String etime) {
		
		Long istime = DateUtil.dateToLong(stime, "yyyy-MM-dd", "", 0);
		Long ietime = DateUtil.dateToLong(etime, "yyyy-MM-dd", "", 0);
		
		Integer getNidBetWins = GameDao.getInstance().getNidBetWins(istime, ietime, nid);
		
		return getNidBetWins;
	}

}
