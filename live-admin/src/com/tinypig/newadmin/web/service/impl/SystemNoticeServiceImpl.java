package com.tinypig.newadmin.web.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.admin.util.DateUtil;
import com.tinypig.newadmin.web.dao.SystemNoticeDao;
import com.tinypig.newadmin.web.entity.SystemNotice;
import com.tinypig.newadmin.web.service.SystemNoticeService;

@Service
public class SystemNoticeServiceImpl implements SystemNoticeService{
	
	@Autowired
	private SystemNoticeDao systemNoticeDao;

	@Override
	public List<SystemNotice> selectList() {
		return systemNoticeDao.selectList();
	}
	
	@Override
	public Integer selectCount() {
		return systemNoticeDao.selectCount();
	}
	
	@Override
	public SystemNotice selectByPrimaryKey(Byte id) {
		return systemNoticeDao.selectByPrimaryKey(id);
	}
	
	@Override
	public Boolean insertByObject(SystemNotice notice) {
		if(notice!=null){
			notice.setId(null);
			notice.setUtime(DateUtil.getTimeStamp(new Date()));
			Integer result = this.systemNoticeDao.insertSelective(notice);
			if(result == 1){
				return true;
			}	
		}
		return false;
	}

	@Override
	public Boolean updateByObject(SystemNotice notice) {
		if(notice != null){
			notice.setUtime(DateUtil.getTimeStamp(new Date()));
			int result = this.systemNoticeDao.updateByPrimaryKeySelective(notice);
			if(result == 1){
				return true;
			}
		}
		return false;
		
	}

	@Override
	public Boolean deleteByPrimaryKey(Byte id) {
		int result = this.systemNoticeDao.deleteByPrimaryKey(id);
		if(result == 1){
			return true;
		}
		else
			return false;
	}

}
