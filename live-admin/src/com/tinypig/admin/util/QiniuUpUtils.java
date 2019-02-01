package com.tinypig.admin.util;

import java.io.File;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.qiniu.common.BannerConfig;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class QiniuUpUtils {
	// 密钥配置
	static Auth auth = Auth.create(BannerConfig.ACCESS_KEY, BannerConfig.SECRET_KEY);

	// 简单上传，使用默认策略，只需要设置上传的空间名就可以了
	public static String getUpToken(String bucketname) {
		return auth.uploadToken(bucketname);
	}

	/**
	 * delete : 根据文件名删除文件
	 * @param key 文件名
	 * @return 200：成功
	 *         612：没有文件
	 *         631：没有bucket
	 */
	public static void delete(String key, String bucketname) {
		// 实例化一个BucketManager对象
		BucketManager bucketManager = new BucketManager(auth);
		try {
			// 调用delete方法移动文件
			bucketManager.delete(bucketname, key);
		} catch (QiniuException e) {
			// 捕获异常信息
			Response r = e.response;
			System.out.println(r.toString());
		}
	}

	/**
	 * download ：根据域名和文件名获取文件下载地址(共有)
	 * @param domain 域名
	 * @param key 文件名
	 * @return 下载URL
	 */
	public static String download(String key, String domail) {
		return "http://" + domail + "/" + key;
	}

	/**
	 * upload : 上传文件
	 * @param localFile 本地文件地址
	 * @param key 文件名
	 * @return 200：成功
	 *         612：没有文件
	 *         631：没有bucket
	 */
	public static int upload(String localFile, String key, String bucketname) throws Exception {
		// 创建上传对象
		UploadManager uploadManager = new UploadManager();
		try {
			// 调用put方法上传
			Response res = uploadManager.put(localFile, key, getUpToken(bucketname));
			// 打印返回的信息
			// System.out.println(res.bodyString());
			return 200;
		} catch (QiniuException e) {
			Response r = e.response;
			// 请求失败时打印的异常的信息
			System.out.println(r.toString());
			try {
				// 响应的文本信息
				System.out.println(r.bodyString());
			} catch (QiniuException e1) {
				// ignore
				e1.printStackTrace();
			}
			return 500;
		}

	}

	public static String uploadfile(MultipartFile file, String bucketname, String domail) {
		String url = "";
		try {
			String movefilepath = PathUtil.getRealPath("/resources/photo/");
			// 文件类型
			String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			// 随机生成文件名
			String filename = RandomStringUtils.random(16, true, true) + System.currentTimeMillis();
			File fileDir = new File(movefilepath);
			if (!fileDir.exists())
				fileDir.mkdirs();
			String oldpic = movefilepath + filename + "." + fileType;
			File tmpFile = new File(oldpic);
			// 文件保存至临时目录
			file.transferTo(tmpFile);
			String key = filename + "." + fileType;
			QiniuUpUtils.upload(oldpic, key, bucketname);
			url = QiniuUpUtils.download(key, domail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

}
