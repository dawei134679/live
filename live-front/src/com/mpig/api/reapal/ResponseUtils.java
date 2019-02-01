package com.mpig.api.reapal;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpServletResponse帮助类
 */
public final class ResponseUtils {
	public static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);

	/**
	 * 发送文本。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderText(HttpServletResponse response, String text) {
		render(response, "text/plain;charset=UTF-8", text);
	}

	/**
	 * 发送xml。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderXml(HttpServletResponse response, String text) {
		render(response, "text/xml;charset=UTF-8", text);
	}

	/**
	 * 发送javascript。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的js
	 */
	public static void renderJs(HttpServletResponse response, String js) {
		renderHtml(response, String.format("<script type=\"text/javascript\">%s</script>", js));
	}

	/**
	 * 发送html。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderHtml(HttpServletResponse response, String text) {
		render(response, "text/html;charset=UTF-8", text);
	}

	/**
	 * 发送内容。使用UTF-8编码。
	 * 
	 * @param response
	 * @param contentType
	 * @param text
	 */
	public static void render(HttpServletResponse response, String contentType, String text) {
		response.setContentType(contentType);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	public static void download(HttpServletRequest request, HttpServletResponse response, File srcFile)
			throws IOException {
		String fileName = srcFile.getName();
		download(request, response, fileName, srcFile);
	}

	/**
	 * 下载文件
	 * 
	 * @param response
	 * @param srcFile
	 * @throws IOException
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, String fileName, File srcFile)
			throws IOException {
		setDownloadHeader(request, response, fileName);
		ServletOutputStream outStream = response.getOutputStream();
		java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(outStream);
		java.io.FileInputStream stream = new java.io.FileInputStream(srcFile);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}
		bos.close();
		outStream.close();
		stream.close();
	}

	public static void download(HttpServletRequest request, HttpServletResponse response, byte[] export2ByteArray,
			String fileName) throws IOException {
		setDownloadHeader(request, response, fileName);
		ServletOutputStream outStream = response.getOutputStream();
		java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(outStream);
		bos.write(export2ByteArray);
		bos.close();
		outStream.close();
	}

	private static void setDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName)
			throws UnsupportedEncodingException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("multipart/form-data");
		String agent = request.getHeader("User-Agent");
		boolean isMSIE = (agent != null && (agent.indexOf("MSIE") != -1 || agent.indexOf("like ") != -1));
		if (isMSIE) {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
	}
}
