package com.tinypig.admin.util;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang.RandomStringUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author 张元林
 * @version 创建时间：2016年3月26日 16:10:32
 * @类说明
 */
public class FileUtil {

	/**
	 * 判断文件名是否已经存在，如果存在则在后面加(n)的形式返回新的文件名，否则返回原始文件名 例如：已经存在文件名 log4j.htm 则返回log4j(1).htm
	 * 
	 * @param fileName
	 *            文件名
	 * @param dir
	 *            判断的文件路径
	 * @return 判断后的文件名
	 */
	public static String checkFileName(String fileName, String dir) {
		boolean isDirectory = new File(dir + fileName).isDirectory();
		if (FileUtil.isFileExist(fileName, dir)) {
			int index = fileName.lastIndexOf(".");
			StringBuffer newFileName = new StringBuffer();
			String name = isDirectory ? fileName : fileName.substring(0, index);
			String extendName = isDirectory ? "" : fileName.substring(index);
			int nameNum = 1;
			while (true) {
				newFileName.append(name).append("(").append(nameNum).append(")");
				if (!isDirectory)
					newFileName.append(extendName);
				if (FileUtil.isFileExist(newFileName.toString(), dir)) {
					nameNum++;
					newFileName = new StringBuffer();
					continue;
				}
				return newFileName.toString();
			}
		}
		return fileName;
	}

	/**
	 * 多文件压缩
	 * 
	 * <pre>
	 *    Example : 
	 *    ZipOutputStream zosm = new ZipOutputStream(new FileOutputStream(&quot;c:/b.zip&quot;));
	 *    zipFiles(zosm, new File(&quot;c:/com&quot;), &quot;&quot;);
	 *    zosm.close();
	 * </pre>
	 * 
	 * @param zosm
	 * @param file
	 * @param basePath
	 * @throws IOException
	 */
	public static void compressionFiles(ZipOutputStream zosm, File file, String basePath) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			try {
				zosm.putNextEntry(new ZipEntry(basePath + "/"));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			basePath = basePath + (basePath.length() == 0 ? "" : "/") + file.getName();
			for (File f : files)
				compressionFiles(zosm, f, basePath);
		}
		else {
			FileInputStream fism = null;
			BufferedInputStream bism = null;
			try {
				byte[] bytes = new byte[1024];
				fism = new FileInputStream(file);
				bism = new BufferedInputStream(fism, 1024);
				basePath = basePath + (basePath.length() == 0 ? "" : "/") + file.getName();
				zosm.putNextEntry(new ZipEntry(basePath));
				int count;
				while ((count = bism.read(bytes, 0, 1024)) != -1)
					zosm.write(bytes, 0, count);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				if (bism != null)
					try {
						bism.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				if (fism != null)
					try {
						fism.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}

	/**
	 * 创建缩略图
	 * 
	 * @param file
	 *            上传的文件流
	 * @param height
	 *            最小的尺寸
	 * @throws IOException
	 */
	public static void createMiniPic(File file, float width, float height) throws IOException {
		Image src = javax.imageio.ImageIO.read(file); // 构造Image对象
		int old_w = src.getWidth(null); // 得到源图宽
		int old_h = src.getHeight(null);
		int new_w = 0;
		int new_h = 0; // 得到源图长
		float tempdouble;
		if (old_w >= old_h)
			tempdouble = old_w / width;
		else tempdouble = old_h / height;

		if (old_w >= width || old_h >= height) { // 如果文件小于锁略图的尺寸则复制即可
			new_w = Math.round(old_w / tempdouble);
			new_h = Math.round(old_h / tempdouble);// 计算新图长宽
			while (new_w > width && new_h > height) {
				if (new_w > width) {
					tempdouble = new_w / width;
					new_w = Math.round(new_w / tempdouble);
					new_h = Math.round(new_h / tempdouble);
				}
				if (new_h > height) {
					tempdouble = new_h / height;
					new_w = Math.round(new_w / tempdouble);
					new_h = Math.round(new_h / tempdouble);
				}
			}
			BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src, 0, 0, new_w, new_h, null); // 绘制缩小后的图
			FileOutputStream newimage = new FileOutputStream(file); // 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(tag);
			param.setQuality((float) (100 / 100.0), true);// 设置图片质量,100最大,默认70
			encoder.encode(tag, param);
			encoder.encode(tag); // 将JPEG编码
			newimage.close();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static boolean delFile(String filePathAndName) {
		File myDelFile = new java.io.File(filePathAndName);
		if (!myDelFile.exists())
			return true;
		return myDelFile.delete();
	}

	/**
	 * 删除指定文件路径下面的所有文件和文件夹
	 * 
	 * @param file
	 */
	public static boolean delFiles(File file) {
		boolean flag = false;
		try {
			if (file.exists()) {
				if (file.isDirectory()) {
					String[] contents = file.list();
					for (String content : contents) {
						File file2X = new File(file.getAbsolutePath() + "/" + content);
						if (file2X.exists()) {
							if (file2X.isFile())
								flag = file2X.delete();
							else if (file2X.isDirectory())
								delFiles(file2X);
						}
						else throw new RuntimeException("File not exist!");
					}
				}
				flag = file.delete();
			}
		}
		catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
    /** 
     * 删除目录（文件夹）以及目录下的文件 
     * @param   sPath 被删除目录的文件路径 
     * @return  目录删除成功返回true，否则返回false 
     */  
    public static boolean deleteDirectory(String sPath) {  
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)) {  
            sPath = sPath + File.separator;  
        }  
        File dirFile = new File(sPath);  
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()) {  
            return false;  
        }  
        boolean flag = true;  
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            //删除子文件  
            if (files[i].isFile()) {  
                flag = deleteFile(files[i].getAbsolutePath());  
                if (!flag) break;  
            } //删除子目录  
            else {  
                flag = deleteDirectory(files[i].getAbsolutePath());  
                if (!flag) break;  
            }  
        }  
        if (!flag) return false;  
        //删除当前目录  
        if (dirFile.delete()) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
    
    /** 
     *  根据路径删除指定的目录或文件，无论存在与否 
     *@param sPath  要删除的目录或文件 
     *@return 删除成功返回 true，否则返回 false。 
     */  
    public static boolean DeleteFolder(String sPath) {  
        boolean flag = false;  
        File file = new File(sPath);  
        // 判断目录或文件是否存在  
        if (!file.exists()) {  // 不存在返回 false  
            return flag;  
        } else {  
            // 判断是否为文件  
            if (file.isFile()) {  // 为文件时调用删除文件方法  
                return deleteFile(sPath);  
            } else {  // 为目录时调用删除目录方法  
                return deleteDirectory(sPath);  
            }  
        }  
    }  
    
    /** 
     * 删除单个文件 
     * @param   sPath    被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */  
    public static boolean deleteFile(String sPath) {  
        boolean flag = false;  
        File file = new File(sPath);  
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {  
            file.delete();  
            flag = true;  
        }  
        return flag;  
    }  

	/**
	 * 文件转化为字节数组
	 */
	public static byte[] getBytesFromFile(File f) {
		if (f == null)
			return null;
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();
		}
		catch (IOException e) {
		}
		return null;
	}


	// 获得随机文件名,保证在同一个文件夹下不同名
	public static String getRandomName(String fileName, String dir) {
		String[] split = fileName.split("\\.");// 将文件名已.的形式拆分
		String extendFile = "." + split[split.length - 1].toLowerCase(); // 获文件的有效后缀
		String randomname = RandomStringUtils.random(10, true, true).toLowerCase();
		String ret = randomname + extendFile;
		while (isFileExist(ret, dir)) {
			randomname = RandomStringUtils.random(10, true, true).toLowerCase();
			ret = randomname + extendFile;
		}
		return ret;
	}

	// 判断文件是否存在
	public static boolean isFileExist(String fileName, String dir) {
		File files = new File(dir + fileName);
		return files.exists() ? true : false;
	}

	/**
	 * 判断文件类型是否是合法的,就是判断allowTypes中是否包含contentType
	 * 
	 * @param contentType
	 *            文件类型
	 * @param allowTypes
	 *            文件类型列表
	 * @return 是否合法
	 */
	public static boolean isValid(String contentType, String[] allowTypes) {
		if (null == contentType || "".equals(contentType))
			return false;
		for (String type : allowTypes)
			if (contentType.equals(type))
				return true;
		return false;
	}

	// 获取本地目录下文件信息（包括子目录）文件信息为：文件名 文件修改时间_文件大小
	public static void listLocalFile(String basepath, String path, boolean containsub, Map<String, String> localfilelist) throws IOException {
		if (!path.endsWith("/"))
			path = path + "/";
		File dirFile = new File(path);
		if (!dirFile.exists() || !dirFile.isDirectory())
			FileUtil.mkDirectory(path);
		File[] files = dirFile.listFiles();
		for (File file : files)
			if (file.isFile())
				localfilelist.put(file.getAbsolutePath().replaceAll("\\\\", "/").replaceAll("//", "/").replaceFirst(basepath, ""), file.lastModified() + "_" + file.length());
			else if (file.isDirectory() && containsub)
				listLocalFile(basepath, file.getAbsolutePath(), containsub, localfilelist);
	}


	/**
	 * 根据路径创建一系列的目录
	 * 
	 * @param path
	 */
	public static boolean mkDirectory(String path) {
		File file = null;
		try {
			file = new File(path);
			if (!file.exists())
				return file.mkdirs();
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
		finally {
			file = null;
		}
		return false;
	}
	
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
