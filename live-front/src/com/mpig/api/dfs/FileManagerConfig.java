package com.mpig.api.dfs;

import java.io.Serializable;

/**
 * 分布式文件系统元数据
 * @ClassName:     FileManagerConfig.java
 * @author         jackzhang
 * @version        V1.0  
 * @Date           May 27, 2016 12:08:38 PM
 */
public interface FileManagerConfig extends Serializable {

  public static final String FILE_DEFAULT_WIDTH 	= "120";
  public static final String FILE_DEFAULT_HEIGHT 	= "120";
  public static final String FILE_DEFAULT_AUTHOR 	= "Diandi";
  
  public static final String PROTOCOL = "http://";
  public static final String SEPARATOR = "/";
  
  public static final String TRACKER_NGNIX_PORT 	= "80";
  
  public static final String CLIENT_CONFIG_FILE   = "fdfs_client.conf";
  
}