package com.mpig.api.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.springframework.util.Assert;

public class GzipUtil {

	/**
	 * 压缩字符串
	 * 
	 * @param str
	 * @param charest
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] compress(String str, String charset) throws IOException, UnsupportedEncodingException {
		Assert.notNull(str, " null compress error ");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		try {
			gzip.write(str.getBytes(charset));
			gzip.close();
			return out.toByteArray();
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			gzip.close();
			out.close();
		}
	}

	/** 
     * 数据压缩 
     *  
     * @param data 
     * @return 
     * @throws Exception 
     */  
    public static byte[] compress(byte[] data) throws Exception {  
        ByteArrayInputStream bais = new ByteArrayInputStream(data);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        compress(bais, baos);  
        byte[] output = baos.toByteArray();  
        baos.flush();  
        baos.close();  
        bais.close();  
        return output;  
    }  
  
    public static final int BUFFER = 1024; 
    
    /** 
     * 数据压缩 
     *  
     * @param is 
     * @param os 
     * @throws Exception 
     */  
    public static void compress(InputStream is, OutputStream os)  
            throws Exception {  
        GZIPOutputStream gos = new GZIPOutputStream(os);  
        int count;  
        byte data[] = new byte[BUFFER];  
        while ((count = is.read(data, 0, BUFFER)) != -1) {  
            gos.write(data, 0, count);  
        }  
  
        gos.finish();  
        gos.flush();  
        gos.close();  
    } 
    
	/**
	 * 解压缩字符串
	 * 
	 * @param str
	 * @param charest
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static byte[] uncompress(InputStream in) throws IOException, UnsupportedEncodingException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPInputStream gunzip = new GZIPInputStream(in);
		try {
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			return out.toByteArray();
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			out.close();
			gunzip.close();
			in.close();
		}
	}
}
