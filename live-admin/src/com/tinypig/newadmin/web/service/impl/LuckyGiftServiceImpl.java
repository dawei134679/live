package com.tinypig.newadmin.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.newadmin.web.dao.GiftLuckyProbabilitysDao;
import com.tinypig.newadmin.web.entity.GiftLuckyProbabilitys;
import com.tinypig.newadmin.web.service.LuckyGiftService;

@Service
public class LuckyGiftServiceImpl implements LuckyGiftService{
	
	@Autowired
	private GiftLuckyProbabilitysDao luckyGiftDao;

	@Override
	public List<GiftLuckyProbabilitys> selectList() {
		return luckyGiftDao.selectList();
	}
	
	@Override
	public Integer selectCount() {
		return luckyGiftDao.selectCount();
	}

	@Override
	public GiftLuckyProbabilitys selectByPrimaryKey(Integer id) {
		return luckyGiftDao.selectByPrimaryKey(id);
	}

	@Override
	public Boolean updateByObject(GiftLuckyProbabilitys gift) {
		if(gift != null){
			int result = luckyGiftDao.updateByPrimaryKeySelective(gift);
			if(result == 1){
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean insertByObject(GiftLuckyProbabilitys gift) {
		if(gift != null){
			int result = luckyGiftDao.insertSelective(gift);
			if(result == 1){
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean deleteByPrimaryKey(Integer id) {
		int result = luckyGiftDao.deleteByPrimaryKey(id);
		if(result == 1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Boolean checkMultiples(Integer multiples) {
		List<GiftLuckyProbabilitys> list = luckyGiftDao.checkMultiples(multiples);
		if(list.size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	

}
