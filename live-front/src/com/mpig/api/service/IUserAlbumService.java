package com.mpig.api.service;

import java.util.List;

import com.mpig.api.model.UserAlbumModel;

public interface IUserAlbumService {

	/**
	 * 添加相片
	 * @param uid
	 * @param photoUrl 大图链接
	 * @param photoThumbUrl 小图链接
	 * @return
	 */
	public int addPhoto(Integer uid, String fileName, String photoUrl, String photoThumbUrl);
	
	/**
	 * 覆盖相片
	 * @param id
	 * @param fileName
	 * @param photoUrl
	 * @param photoThumbUrl
	 * @return
	 */
	public int updPhoto(Integer id, String fileName, String photoUrl, String photoThumbUrl);
	
	/**
	 * 删除相片
	 * @param id 相片id
	 * @return
	 */
	public int delPhoto(Integer id, Integer uid);
	
	/**
	 * 获取用户的相册列表
	 * @param uid
	 * @return
	 */
	public List<UserAlbumModel> getUserAlbum(Integer uid);
	/**
	 * 获取用户的相册列表
	 * @param uid
	 * @return
	 */
	public List<UserAlbumModel> getUserAlbumDate(Integer uid, boolean directReadMysql);
	
	/**
	 * 获取用户的某张相片
	 * @param uid
	 * @return
	 */
	public UserAlbumModel getUserAlbumById(Integer id);
	
	/**
	 * 根据用户查询相片数量
	 * @param uid
	 * @return
	 */
	public int selPhotoCountByUser(Integer uid);
	
	/**
	 * 查询用户最新上传的相片
	 * @param uid
	 * @return
	 */
	public UserAlbumModel getUserAlbumLast(Integer uid);
	
	
	/**
	 * 根据相片id获取举报记录
	 * @param pid
	 * @return
	 */
	public int getReportAlbumByPid(Integer pid);
	
	/**
	 * 根据举报信息查看当前举报人是否举报过
	 * @param rid
	 * @param dstuid
	 * @return
	 */
	public int getReportAlbummUserByRid(Integer rid, Integer dstuid);
	
	/**
	 * 添加相片举报的用户详情信息
	 * @param rid
	 * @param pid
	 * @param reportReason
	 * @param dstuid
	 * @return
	 */
	public int addReportAlbumUser(Integer rid, Integer pid, String reportReason, Integer dstuid);
	
	/**
	 * 添加举报相册信息
	 * @param reportUid
	 * @param reportPid
	 * @param copyFilename
	 * @param copyUrl
	 * @return
	 */
	public int addReportAlbum(Integer reportUid, Integer reportPid,String copyFilename,String copyUrl);
	
	/**
	 * 修改举报次数
	 * @param reportPid
	 * @return
	 */
	public int updReportAlbum(Integer reportPid);
}
