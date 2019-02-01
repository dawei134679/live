package com.mpig.api.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mpig.api.model.AuthenticationModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserXiaozhuAuthModel;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.ISearchService;
import com.mpig.api.service.IUploadService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.HttpKit;
import com.mpig.api.utils.JsonUtil;
import com.mpig.api.utils.MD5Encrypt;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RandomUtil;

/**
 * 上传文件
 * 
 * @author fang
 * 
 */
@Controller
@Scope("prototype")
@RequestMapping("/upload")
public class UploadController extends BaseController {

	private static final Logger logger = Logger.getLogger(UploadController.class);

	@Resource
	private IUploadService uploadService;
	@Resource
	private ISearchService searchService;
	@Resource
	private IUserService userService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "authenpic")
	@ResponseBody
	public ReturnModel uploadAuthenPic(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "type")) {
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
		String type = req.getParameter("type");
		AuthenticationModel authenticationModel = userService.getAuthentication(uid);
		if (authenticationModel != null) {
			if (authenticationModel.getAuditStatus() == 1) {
				returnModel.setCode(CodeContant.authenIng);
				returnModel.setMessage("审核中，不能上传图片");
				return returnModel;
			} else if (authenticationModel.getAuditStatus() == 3) {
				returnModel.setCode(CodeContant.authened);
				returnModel.setMessage("审核通过，不能上传图片");
				return returnModel;
			}
		}
		String fileName = ""; 
		if ("positiveImage".equalsIgnoreCase(type)) {
			fileName = uid + "_p";// 正面
		} else if ("negativeImage".equalsIgnoreCase(type)) {
			fileName = uid + "_n";// 反面
		} else {
			fileName = uid + "_h";// 手持
		}
		// 判断是否已经存在图片了
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
		MultipartFile mFile = multipartRequest.getFile(type);
		try {
			String uploadFileName = RandomUtil.getTime3Random() + ".png";

			Map<String, String> headers = new HashMap<String, String>();
			String timestamp = new Date().getTime() + "";
			headers.put("timestamp", timestamp);
			headers.put("auth", MD5Encrypt.encrypt((URLEncoder.encode(uploadFileName, "UTF-8") + timestamp)));

			String tempFolder = System.getProperty("java.io.tmpdir");
			File tempFile = new File(tempFolder + File.separator + uploadFileName);
			mFile.transferTo(tempFile);

			String json = HttpKit.post(Constant.file_upload_server_url, headers, null, tempFile);
			logger.info("返回结果："+json);
			Map<String,String> result = JsonUtil.toBean(json, Map.class);
			if (!result.get("state").equals("ok")) {
				 returnModel.setCode(CodeContant.CONSYSTEMERR);
				 returnModel.setMessage("上传文件失败");
				 return returnModel;
			} 
			Map<String, String> map = new HashMap<String, String>();
			map.put("filename", result.get("url"));
			returnModel.setData(map);
			UserRedisService.getInstance().setIdentity(fileName, result.get("url"));
			return returnModel;
		} catch (Exception e) {
			returnModel.setCode(CodeContant.authenImageErr);
			returnModel.setMessage("exception=" + e.getMessage());
			logger.error("uploadAuthenPic->excepton:" + e.toString(),e);
		}
		return returnModel;
	}

	@RequestMapping(value = "/xzAuthpic")
	@ResponseBody
	public ReturnModel uploadXzAuthenPic(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "type")) {
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
		String type = req.getParameter("type");

		UserXiaozhuAuthModel xiaozhuAuthModel = userService.getXiaozhuAuth(uid);
		if (xiaozhuAuthModel != null) {
			if (xiaozhuAuthModel.getStatus() == 1) {
				returnModel.setCode(CodeContant.authenIng);
				returnModel.setMessage("审核中，不能上传图片");
				return returnModel;
			} else if (xiaozhuAuthModel.getStatus() == 3) {
				returnModel.setCode(CodeContant.authened);
				returnModel.setMessage("审核通过，不能上传图片");
				return returnModel;
			}
		}

		String fileName = "";
		if ("1".equalsIgnoreCase(type)) {
			fileName = uid + "_1";
		} else if ("2".equalsIgnoreCase(type)) {
			fileName = uid + "_2";
		} else if ("3".equalsIgnoreCase(type)) {
			fileName = uid + "_3";
		} else {
			fileName = uid + "_4";
		}
		// 判断是否已经存在图片了
		String pic = UserRedisService.getInstance().getXzAuthpic(fileName);

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
		MultipartFile mFile = multipartRequest.getFile(type);

		try {
			String uploadFileName = RandomUtil.getTime3Random() + ".png";

			Map<String, String> headers = new HashMap<String, String>();
			String timestamp = new Date().getTime() + "";
			headers.put("timestamp", timestamp);
			headers.put("auth", MD5Encrypt.encrypt((URLEncoder.encode(uploadFileName, "UTF-8") + timestamp)));

			String tempFolder = System.getProperty("java.io.tmpdir");
			File tempFile = new File(tempFolder + File.separator + uploadFileName);
			mFile.transferTo(tempFile);

			String json = HttpKit.post(Constant.file_upload_server_url, headers, null, tempFile);
			logger.info("返回结果："+json);
			Map<String,String> result = JsonUtil.toBean(json, Map.class);
			if (!result.get("state").equals("ok")) {
				 returnModel.setCode(CodeContant.CONSYSTEMERR);
				 returnModel.setMessage("上传文件失败");
				 return returnModel;
			} 
			Map<String, String> map = new HashMap<String, String>();
			map.put("filename", result.get("url"));
			returnModel.setData(map);
			UserRedisService.getInstance().setXzAuthPic(fileName, result.get("url"));
		} catch (Exception e) {
			returnModel.setCode(CodeContant.authenImageErr);
			returnModel.setMessage("exception=" + e.getMessage());
			logger.error("uploadXzAuthenPic->excepton:" + e.toString());
		}
		return returnModel;
	}

	/**
	 * 图片上传公共接口
	 * @param file
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
	public ReturnModel upload(@RequestParam("file") CommonsMultipartFile file, String type) {
		try {
			String fileName = RandomUtil.getTime3Random() + ".png";

			Map<String, String> headers = new HashMap<String, String>();
			String timestamp = new Date().getTime() + "";
			headers.put("timestamp", timestamp);
			headers.put("auth", MD5Encrypt.encrypt((URLEncoder.encode(fileName, "UTF-8") + timestamp)));

			String tempFolder = System.getProperty("java.io.tmpdir");
			File tempFile = new File(tempFolder + File.separator + fileName);
			file.transferTo(tempFile);

			String json = HttpKit.post(Constant.file_upload_server_url, headers, null, tempFile);
			
			Map<String,String> result = JsonUtil.toBean(json, Map.class);
			if (!result.get("state").equals("ok")) {
				 returnModel.setCode(CodeContant.CONSYSTEMERR);
				 returnModel.setMessage("上传文件失败");
				 return returnModel;
			} 
			Map<String, String> map = new HashMap<String, String>();
			map.put("filename", result.get("url"));
			returnModel.setData(map);
			return returnModel;
		} catch (Exception e) {
			logger.error("上传失败：", e);
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage(e.getMessage());
			return returnModel;
		}
	}
}
