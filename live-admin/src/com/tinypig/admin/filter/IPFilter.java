package com.tinypig.admin.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.tinypig.admin.util.StringUtil;

public class IPFilter implements Filter {

	protected FilterConfig filterConfig;
	protected String ip;
	public static HashMap<String,Boolean> whiteIps = new HashMap<String, Boolean>();
	
	private static Logger logger = Logger.getLogger(IPFilter.class);

	public IPFilter() {
		super();
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String remoteIP = getRemoteAddrIp((HttpServletRequest)request);
		
		if (whiteIps.containsKey(remoteIP) || whiteIps.containsKey("*")) {
			chain.doFilter(request, response);
		} else {
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.println("<b>your ip address not allowed</b>");
		}
	}

	public static String getRemoteAddrIp(HttpServletRequest request) {  
	    String ipFromNginx = getHeader(request, "X-Real-IP");  
	    return StringUtil.isEmpty(ipFromNginx) ? request.getRemoteAddr() : ipFromNginx;  
	}  
	  
	  
	private static String getHeader(HttpServletRequest request, String headName) {  
	    String value = request.getHeader(headName);  
	    return !StringUtil.isEmpty(value) && !"unknown".equalsIgnoreCase(value) ? value : "";  
	} 
	
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
//		String rootPrefix = this.getClass().getResource("/").getPath();
//		rootPrefix = rootPrefix.substring(0,rootPrefix.lastIndexOf("classes"));
//		readAllWhiteIpList(rootPrefix+"whiteIpList.properties");
	}
	
	public static void readAllWhiteIpList(String filePath) {
		BufferedReader bufferedReader = null;
		try {
			whiteIps.clear();
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
//				BufferedReader bufferedReader = new BufferedReader(read);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					whiteIps.put(lineTxt, true);
				}
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					logger.error("关闭流异常",e);
				}
			}
		}
	}


}