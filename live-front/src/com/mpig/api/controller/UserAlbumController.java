package com.mpig.api.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserAlbumModel;
import com.mpig.api.service.IUserAlbumService;
import com.mpig.api.utils.BaseContant;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.ParamHandleUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

@Controller
@Scope("prototype")
@RequestMapping("/user/photo")
public class UserAlbumController extends BaseController {

	private static final Logger logger = Logger.getLogger(UploadController.class);
	@Resource
	private IUserAlbumService userAlbumService;

	private UploadManager uploadManager = new UploadManager();

	@RequestMapping("/upload")
	@ResponseBody
	public ReturnModel upload(Integer pid, HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}

		UserAlbumModel userAlbumModel = null;
		String fileName = RandomStringUtils.random(11, true, true) + System.currentTimeMillis();
		if (pid != null) {
			userAlbumModel = userAlbumService.getUserAlbumById(pid);
			if (userAlbumModel != null) {
				fileName = userAlbumModel.getFileName();
			} else {
				returnModel.setCode(CodeContant.coveredPhotoError);
				returnModel.setMessage("相片覆盖失败");
				return returnModel;
			}
		} else {
			int count = userAlbumService.selPhotoCountByUser(uid);
			if (count == 6) {
				returnModel.setCode(CodeContant.albumPhotoIsMaxError);
				returnModel.setMessage("相片已达到上限");
				return returnModel;
			}
		}
		Auth auth = Auth.create(BaseContant.accessKey, BaseContant.secretKey);
		String uploadToken = auth.uploadToken(Constant.qn_photo_bucket, fileName);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
			MultipartFile mFile = multipartRequest.getFile("photo");
			byte[] file_buff = null;
			InputStream fis = mFile.getInputStream();
			if (fis != null) {
				int len = fis.available();
				file_buff = new byte[len];
				fis.read(file_buff);
			}
			Response response = uploadManager.put(file_buff, fileName, uploadToken);
			if (response.statusCode == 200) {
				Long lgLong = System.currentTimeMillis() / 1000;
				String image = Constant.qn_photo_bucket_domain  + "/" + fileName + "?v=" + lgLong;
				String imageThumb = Constant.qn_photo_bucket_domain  + "/" + fileName + "?imageMogr2/thumbnail/200x200&v="
						+ lgLong;
				if (pid != null) {
					userAlbumService.updPhoto(pid, fileName, image, imageThumb);
					userAlbumModel = userAlbumService.getUserAlbumById(pid);
				} else {
					userAlbumService.addPhoto(uid, fileName, image, imageThumb);
					userAlbumModel = userAlbumService.getUserAlbumLast(uid);
				}
				userAlbumService.getUserAlbumDate(uid, true);
				dataMap.put("album", userAlbumModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			returnModel.setCode(CodeContant.uploadPhotoError);
			returnModel.setMessage("上传失败");
		}
		returnModel.setData(dataMap);
		return returnModel;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnModel delPhoto(Integer pid, HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "pid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		UserAlbumModel userAlbumModel = userAlbumService.getUserAlbumById(pid);
		if (userAlbumModel != null) {
			Auth auth = Auth.create(BaseContant.accessKey, BaseContant.secretKey);
			BucketManager bucketManager = new BucketManager(auth);
			try {
				int rsc = userAlbumService.delPhoto(pid, uid);
				if (rsc <= 0) {
					returnModel.setCode(CodeContant.delPhotoError);
					returnModel.setMessage("删除失败");
				} else {
					bucketManager.delete(Constant.qn_photo_bucket, userAlbumModel.getFileName());
				}
				userAlbumService.getUserAlbumDate(uid, true);
			} catch (QiniuException e) {
				returnModel.setCode(CodeContant.delPhotoError);
				returnModel.setMessage("删除失败");
				logger.error(e);
			}
		}
		return returnModel;
	}

	@RequestMapping("/report")
	@ResponseBody
	public ReturnModel report(Integer pid, String reportReason, HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "pid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int dstuid = authService.decryptToken(token, returnModel);
		if (dstuid <= 0) {
			return returnModel;
		}
		try {
			// 获取当前相片的信息
			UserAlbumModel albumModel = userAlbumService.getUserAlbumById(pid);
			if (albumModel != null) {
				String fileName = albumModel.getFileName();
				Integer uid = albumModel.getUid();
				// 查询当前相片是否被举报过
				int rid = userAlbumService.getReportAlbumByPid(pid);
				if (rid > 0) {
					// 查询当前用户是否举报过
					int rsc = userAlbumService.getReportAlbummUserByRid(rid, dstuid);
					if (rsc == 0) {
						// 增加举报信息的举报次数
						userAlbumService.updReportAlbum(pid);
						// 增加用户的举报信息记录
						userAlbumService.addReportAlbumUser(rid, pid, reportReason, dstuid);
					} else {
						returnModel.setCode(CodeContant.albumIsHavent);
						returnModel.setMessage("请勿重复举报！");
					}
				} else {
					Auth auth = Auth.create(BaseContant.accessKey, BaseContant.secretKey);
					BucketManager bucketManager = new BucketManager(auth);
					String newFileName = RandomStringUtils.random(11, true, true) + System.currentTimeMillis();
					bucketManager.copy(Constant.qn_photo_bucket, fileName, Constant.qn_photo_bucket, newFileName);
					Long lgLong = System.currentTimeMillis() / 1000;
					String copyImageUrl = Constant.qn_photo_bucket_domain + "/" + newFileName + "?v=" + lgLong;
					rid = userAlbumService.addReportAlbum(uid, pid, newFileName, copyImageUrl);
					userAlbumService.addReportAlbumUser(rid, pid, reportReason, dstuid);
				}
			} else {
				returnModel.setCode(CodeContant.reportAlbumInfoIsHave);
				returnModel.setMessage("您举报的相片不存在！");
			}
		} catch (Exception e) {
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("服务器暂时休克啦~");
			logger.error(e);
		}
		return returnModel;
	}
}