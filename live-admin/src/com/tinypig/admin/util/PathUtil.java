package com.tinypig.admin.util;

import java.io.File;
import java.io.FileNotFoundException;

import javax.servlet.ServletContext;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.util.WebUtils;

public class PathUtil {

	public static String getRealPath(ServletContext context) {
		return context.getRealPath(File.separator);
	}

	public static String getRealPath(ServletContext context, String path) {
		return getRealPath(context).concat(path);
	}

	public static String getRealPath(ServletContext context, String path, String fileName) {
		return getRealPath(context).concat(path).concat(fileName);
	}

	/*
	 * 获取path的绝对路径
	 */
	public static String getRealPath(String path) throws FileNotFoundException {
		String pathurl = WebUtils.getRealPath(ContextLoader.getCurrentWebApplicationContext().getServletContext(), path);
		pathurl = pathurl.replaceAll("\\\\", "/").replaceAll("//", "/");
		if (!pathurl.endsWith("/"))
			pathurl = pathurl + "/";
		return pathurl;
	}
}
