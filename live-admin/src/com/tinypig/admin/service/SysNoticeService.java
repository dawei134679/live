package com.tinypig.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tinypig.admin.dao.SysNoticeDao;

@Service
public class SysNoticeService {

	/**
	 * 获取系统私信公告 
	 * @return
	 */
	public List<Map<String, Object>> sysNoticeList(){
		return SysNoticeDao.getInstance().getSysNoticeList();
	}
	
	public int addSysNoctice(String content,String url,Long utime,Long addtime,int adminid){
		
		return SysNoticeDao.getInstance().addSysNoctice(content, url, utime, addtime, adminid);
	}
	
	public List<Map<String, Object>> getValidSysNotice(Long stime){
		return SysNoticeDao.getInstance().getValidSysNotice(stime);
	}
	
	public Map<String, Object> getRoomChat(){
		
		return SysNoticeDao.getInstance().getRoomChat();
	}
	
	public int addRoomChat(String content,Long starttime,Long endtime,int isvalid,Long addtime,int adminid){
		
		return SysNoticeDao.getInstance().addRoomChat(content, starttime, endtime, isvalid, addtime, adminid);
	}
	
	public int editRoomChat(String content,Long starttime,Long endtime,int isvalid,int id,int adminid){
		return SysNoticeDao.getInstance().editRoomChat(content, starttime, endtime, isvalid, id, adminid);
	}
	
	public int removeRoomChat(int id,int adminid){
		return SysNoticeDao.getInstance().removeRoomChat(id, adminid);
	}
}
