package com.mpig.api.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

public class CompressionFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String uri = request.getRequestURI();
		String ext = FilenameUtils.getExtension(uri);

		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", -1);
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			if (isGZipEncoding(request)) {
				String gzippPatternExclude = ",.htm,.html,.jsp,.js,.ajax,.css,";
				if (StringUtils.indexOf(gzippPatternExclude, ",." + ext + ",") == -1) {
					BufferedResponse gzipResponse = new BufferedResponse(response);
					chain.doFilter(request, gzipResponse);
					byte[] srcData = gzipResponse.getResponseData();
					byte[] outData = null;
					if (srcData.length > 512) {
						byte[] gzipData = GzipUtil.compress(srcData);
						response.addHeader("Content-Encoding", "gzip");
						response.setContentLength(gzipData.length);
						outData = gzipData;
					} else {
						outData = srcData;
					}
					ServletOutputStream output = response.getOutputStream();
					output.write(outData);
					output.flush();
				} else {
					chain.doFilter(request, response);
				}
				return;
			}

			chain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	@Override
	public void destroy() {
	}

	/**
	 * 判断浏览器是否支持GZIP
	 * 
	 * @param request
	 * @return
	 */
	private boolean isGZipEncoding(HttpServletRequest request) {
		boolean flag = false;
		String encoding = request.getHeader("Accept-Encoding");
		if (encoding != null && encoding.indexOf("gzip") != -1) {
			flag = true;
		}
		return flag;
	}
}