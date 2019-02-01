package com.mpig.api.utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class FileUtils {

	//读文件，返回字符串
	public static String ReadFileToString(String filePath){
		File file = new File(filePath);
		BufferedReader reader = null;
		String laststr = "";
		try {
			reader = new BufferedReader((new InputStreamReader(new FileInputStream(file),"UTF-8")));
			String tempString = null;
			//一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				laststr = laststr+tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					
				}
			}
			if (file != null) {
				file = null;
			}
		}
		return laststr;
	}
	/** 
     * 从网络Url中下载文件 
     * @param urlStr 
     * @param fileName 
     * @param savePath 
     * @throws IOException 
     */  
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{  
        URL url = new URL(urlStr);    
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
                //设置超时间为3秒  
        conn.setConnectTimeout(3*1000);  
        //防止屏蔽程序抓取而返回403错误  
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
  
        //得到输入流  
        InputStream inputStream = conn.getInputStream();    
        //获取自己数组  
        byte[] getData = readInputStream(inputStream);      
  
        //文件保存位置  
        File saveDir = new File(savePath);  
        if(!saveDir.exists()){  
            saveDir.mkdir();  
        }  
        File file = new File(saveDir+File.separator+fileName);      
        FileOutputStream fos = new FileOutputStream(file);       
        fos.write(getData);   
        if(fos!=null){  
            fos.close();    
        }  
        if(inputStream!=null){  
            inputStream.close();  
        }  
  
  
        System.out.println("info:"+url+" download success");   
  
    }
    
    /**  
     * 对图片裁剪，并把裁剪新图片保存  
     * @param srcPath 读取源图片路径 
     * @param toPath    写入图片路径 
     * @param x 剪切起始点x坐标 
     * @param y 剪切起始点y坐标 
     * @param width 剪切宽度 
     * @param height     剪切高度 
     * @param readImageFormat  读取图片格式 
     * @param writeImageFormat 写入图片格式 
     * @throws IOException 
     */  
    public static void cropImage(String srcPath,String toPath,  
            int x,int y,int width,int height,  
            String readImageFormat,String writeImageFormat) throws IOException{     
        FileInputStream fis = null ;  
        ImageInputStream iis =null ;  
        try{     
            //读取图片文件   
            fis = new FileInputStream(srcPath);   
            Iterator it = ImageIO.getImageReadersByFormatName(readImageFormat);   
            ImageReader reader = (ImageReader) it.next();   
            //获取图片流    
            iis = ImageIO.createImageInputStream(fis);    
            reader.setInput(iis,true) ;  
            ImageReadParam param = reader.getDefaultReadParam();   
            //定义一个矩形   
            Rectangle rect = new Rectangle(x, y, width, height);   
            //提供一个 BufferedImage，将其用作解码像素数据的目标。    
            param.setSourceRegion(rect);  
            BufferedImage bi = reader.read(0,param);                  
            //保存新图片    
            ImageIO.write(bi, writeImageFormat, new File(toPath));       
        }finally{  
            if(fis!=null)  
                fis.close();         
            if(iis!=null)  
               iis.close();   
        }   
    }
    /** 
     * 从输入流中获取字节数组 
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    }  
}
