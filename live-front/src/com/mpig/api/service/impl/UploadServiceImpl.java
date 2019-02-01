package com.mpig.api.service.impl;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mpig.api.service.IUploadService;

@Service
public class UploadServiceImpl implements IUploadService {	

	private static final Logger logger = Logger.getLogger(UploadServiceImpl.class);
	
	@Override
	public Boolean disposeImage(String srcImgPath, String outImgPath, int width,
			int height) {
		BufferedImage src = null;
		try {
			FileInputStream in = new FileInputStream(srcImgPath);  
			src = javax.imageio.ImageIO.read(in);  
		} catch (Exception e) {
			logger.info("<disposeImage->Exception>"+e.getMessage());
			return false;
		}
		// 得到图片  
        int old_w = src.getWidth();  
        // 得到源图宽  
        int old_h = src.getHeight();  
        // 得到源图长  
        BufferedImage newImg = null;  
        // 判断输入图片的类型  
        switch (src.getType()) {  
        case 13:  
             newImg = new BufferedImage(width, height,BufferedImage.TYPE_4BYTE_ABGR);  
            break;  
        default:  
            newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
            break;  
        }  
        Graphics2D g = newImg.createGraphics();  
        // 从原图上取颜色绘制新图  
        g.drawImage(src, 0, 0, old_w, old_h, null);  
        g.dispose();  
        // 根据图片尺寸压缩比得到新图的尺寸  
        newImg.getGraphics().drawImage(  
                src.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0,  
                null);  
        // 调用方法输出图片文件  
        return OutImage(outImgPath, newImg);
	}
	
	/**
	 * 将图片文件输出到指定的路径，并可设定压缩质量
	 * @param outImgPath
	 * @param newImg
	 */
	private Boolean OutImage(String outImgPath, BufferedImage newImg) {  
        // 判断输出的文件夹路径是否存在，不存在则创建  
		Boolean bl = false;
        File file = new File(outImgPath);  
        if (!file.getParentFile().exists()) {  
            file.getParentFile().mkdirs();  
        }// 输出到文件流  
        try {  
            ImageIO.write(newImg, outImgPath.substring(outImgPath  
                    .lastIndexOf(".") + 1), new File(outImgPath)); 
            bl = true;
        } catch (FileNotFoundException e) {
        	logger.info("<OutImage->FileNotFoundException>"+e.getMessage());  
        } catch (IOException e) { 
        	logger.info("<OutImage->IOException>"+e.getMessage());  
        } 

        return bl;
    }  
}
