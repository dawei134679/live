package com.mpig.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dao.IUserAlbumDao;
import com.mpig.api.model.UserAlbumModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IUserAlbumService;
import com.mpig.api.utils.RedisContant;

@Service
public class UserAlbumServiceImpl implements IUserAlbumService{

	@Autowired
	private IUserAlbumDao userAlbumDao;
	@Override
	public int addPhoto(Integer uid, String fileName, String photoUrl, String photoThumbUrl) {
		return userAlbumDao.addPhoto(uid, fileName, photoUrl, photoThumbUrl);
	}
	
	@Override
	public int updPhoto(Integer id, String fileName, String photoUrl,
			String photoThumbUrl) {
		return userAlbumDao.updPhoto(id, fileName, photoUrl, photoThumbUrl);
	}

	@Override
	public int delPhoto(Integer id, Integer uid) {
		return userAlbumDao.delPhoto(id, uid);
	}

	@Override
	public List<UserAlbumModel> getUserAlbum(Integer uid) {
		return userAlbumDao.getUserAlbum(uid);
	}

	@Override
	public List<UserAlbumModel> getUserAlbumDate(Integer uid, boolean directReadMysql) {
		List<UserAlbumModel>  albumModels = new ArrayList<UserAlbumModel>();
		if (!directReadMysql) {
			// 读缓存
			String userbaseinfo = UserRedisService.getInstance().get(RedisContant.keyUserAlbum+uid);
			if (org.apache.commons.lang.StringUtils.isNotEmpty(userbaseinfo)) {
				albumModels = (List<UserAlbumModel>) JSONObject.parseArray(userbaseinfo, UserAlbumModel.class);
			}
		}
		if(albumModels.size() ==0){
			albumModels = userAlbumDao.getUserAlbum(uid);
			UserRedisService.getInstance().set(RedisContant.keyUserAlbum+uid, JSONArray.toJSONString(albumModels));
		}
		
		return albumModels;
	}

	@Override
	public UserAlbumModel getUserAlbumById(Integer id) {
		return userAlbumDao.getUserAlbumById(id);
	}

	@Override
	public int selPhotoCountByUser(Integer uid) {
		return userAlbumDao.selPhotoCountByUser(uid);
	}

	@Override
	public UserAlbumModel getUserAlbumLast(Integer uid) {
		return userAlbumDao.getUserAlbumLast(uid);
	}

	@Override
	public int getReportAlbumByPid(Integer pid) {
		return userAlbumDao.getReportAlbumByPid(pid);
	}

	@Override
	public int getReportAlbummUserByRid(Integer rid, Integer dstuid) {
		return userAlbumDao.getReportAlbummUserByRid(rid, dstuid);
	}

	@Override
	public int addReportAlbumUser(Integer rid, Integer pid,
			String reportReason, Integer dstuid) {
		return userAlbumDao.addReportAlbumUser(rid, pid, reportReason, dstuid);
	}

	@Override
	public int addReportAlbum(Integer reportUid, Integer reportPid,
			String copyFilename, String copyUrl) {
		return userAlbumDao.addReportAlbum(reportUid, reportPid, copyFilename, copyUrl);
	}

	@Override
	public int updReportAlbum(Integer reportPid) {
		return userAlbumDao.updReportAlbum(reportPid);
	}

}
