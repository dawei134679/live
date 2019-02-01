package com.mpig.api.service;

public interface IUploadService {

	/**
	 * 处理图片
	 * @param srcImgPath 原图片地址
	 * @param outImgPath 新图片地址
	 * @param width	新宽度
	 * @param height 新高度
	 */
	public Boolean disposeImage(String srcImgPath,String outImgPath, int width, int height);
}
