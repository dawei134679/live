package com.mpig.api.dfs;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageServer;
import org.springframework.util.Assert;

/**
 * 分布式文件系统接口测试实例
 * @ClassName:     TestFileManager.java
 * @Description:   TODO 
 * 
 * @author         jackzhang
 * @version        V1.0  
 * @Date           May 27, 2016 1:32:50 PM
 */
public class TestFileManager {

  public String[] upload() throws Exception {
    File content = new File("/Users/jackzhang/sql.sql");
    
    FileInputStream fis = new FileInputStream(content);
      byte[] file_buff = null;
      if (fis != null) {
      	int len = fis.available();
      	file_buff = new byte[len];
      	fis.read(file_buff);
      }
    
    FastDFSFile file = new FastDFSFile("sql", file_buff, "sql");
    
    String[] redults = FileManager.upload(file);
    System.out.println("fileAbsolutePath>>>"+redults[2]);
    fis.close();
    return redults;
  }
  
  public void getFile(String groupName,String fileName) throws Exception {
    FileInfo file = FileManager.getFile(groupName, fileName);
    Assert.notNull(file);
    String sourceIpAddr = file.getSourceIpAddr();
    long size = file.getFileSize();
    System.out.println("ip:" + sourceIpAddr + ",size:" + size);
  }
  
  public boolean existFile(String groupName,String fileName) throws Exception {
	   FileInfo file = FileManager.getFile(groupName, fileName);
	   return file!= null;
  }
  
  public void getStorageServer() throws Exception {
    StorageServer[] ss = FileManager.getStoreStorages("group1");
    Assert.notNull(ss);
    
    for (int k = 0; k < ss.length; k++){
      System.err.println(k + 1 + ". " + ss[k].getInetSocketAddress().getAddress().getHostAddress() + ":" + ss[k].getInetSocketAddress().getPort());
      }
  }
  
  public void getFetchStorages() throws Exception {
    ServerInfo[] servers = FileManager.getFetchStorages("group1", "M00/00/00/wKgBm1N1-CiANRLmAABygPyzdlw073.jpg");
    Assert.notNull(servers);
    
    for (int k = 0; k < servers.length; k++) {
    		System.err.println(k + 1 + ". " + servers[k].getIpAddr() + ":" + servers[k].getPort());
    	}
  }
  
  
  public static void main(String[] args){
	  TestFileManager testFileManager = new TestFileManager();
	  try {
		String[] upload = testFileManager.upload();
		System.err.println(upload);
		boolean existFile = testFileManager.existFile(upload[0],upload[1]);
		System.err.println(existFile?"ok":"no exist");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
}