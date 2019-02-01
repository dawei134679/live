package com.mpig.api.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.mpig.api.controller.UploadController;

/**
 * HttpKit
 */
public class HttpKit {
	
	private static final Logger logger = Logger.getLogger(UploadController.class);

	private HttpKit() {
	}

	/**
	 * https 域名校验
	 */
	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	/**
	 * https 证书管理
	 */
	private static class TrustAnyTrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
	}

	private static final String GET = "GET";
	private static final String POST = "POST";
	private static String CHARSET = "UTF-8";

	private static final SSLSocketFactory sslSocketFactory = initSSLSocketFactory();
	private static final TrustAnyHostnameVerifier trustAnyHostnameVerifier = new HttpKit.TrustAnyHostnameVerifier();

	private static SSLSocketFactory initSSLSocketFactory() {
		try {
			TrustManager[] tm = { new HttpKit.TrustAnyTrustManager() };
			SSLContext sslContext = SSLContext.getInstance("TLS"); // ("TLS", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			return sslContext.getSocketFactory();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setCharSet(String charSet) {
		if (org.apache.commons.lang.StringUtils.isBlank(charSet)) {
			throw new IllegalArgumentException("charSet can not be blank.");
		}
		HttpKit.CHARSET = charSet;
	}

	private static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers)
			throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		URL _url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
		if (conn instanceof HttpsURLConnection) {
			((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
			((HttpsURLConnection) conn).setHostnameVerifier(trustAnyHostnameVerifier);
		}

		conn.setRequestMethod(method);
		conn.setDoOutput(true);
		conn.setDoInput(true);

		conn.setConnectTimeout(19000);
		conn.setReadTimeout(19000);

		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");

		if (headers != null && !headers.isEmpty()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}

		return conn;
	}

	/**
	 * Send GET request
	 */
	public static String get(String url, Map<String, String> queryParas, Map<String, String> headers) {
		HttpURLConnection conn = null;
		try {
			conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), GET, headers);
			conn.connect();
			return readResponseString(conn);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	public static String get(String url, Map<String, String> queryParas) {
		return get(url, queryParas, null);
	}

	public static String get(String url) {
		return get(url, null, null);
	}

	/**
	 * Send POST request
	 */
	public static String post(String url, Map<String, String> queryParas, String data, Map<String, String> headers) {
		HttpURLConnection conn = null;
		try {
			conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), POST, headers);
			conn.connect();

			if (data != null) {
				OutputStream out = conn.getOutputStream();
				out.write(data.getBytes(CHARSET));
				out.flush();
				out.close();
			}

			return readResponseString(conn);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	public static String post(String url, Map<String, String> queryParas, String data) {
		return post(url, queryParas, data, null);
	}

	public static String post(String url, String data, Map<String, String> headers) {
		return post(url, null, data, headers);
	}

	public static String post(String url, String data) {
		return post(url, null, data, null);
	}

	private static String readResponseString(HttpURLConnection conn) {
		BufferedReader reader = null;
		try {
			StringBuilder ret;
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));
			String line = reader.readLine();
			if (line != null) {
				ret = new StringBuilder();
				ret.append(line);
			} else {
				return "";
			}

			while ((line = reader.readLine()) != null) {
				ret.append('\n').append(line);
			}
			return ret.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * Build queryString of the url
	 */
	private static String buildUrlWithQueryString(String url, Map<String, String> queryParas) {
		if (queryParas == null || queryParas.isEmpty()) {
			return url;
		}

		StringBuilder sb = new StringBuilder(url);
		boolean isFirst;
		if (url.indexOf('?') == -1) {
			isFirst = true;
			sb.append('?');
		} else {
			isFirst = false;
		}

		for (Entry<String, String> entry : queryParas.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append('&');
			}

			String key = entry.getKey();
			String value = entry.getValue();
			if (org.apache.commons.lang.StringUtils.isNotBlank(value)) {
				try {
					value = URLEncoder.encode(value, CHARSET);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
			sb.append(key).append('=').append(value);
		}
		return sb.toString();
	}

	public static String readData(HttpServletRequest request) {
		BufferedReader br = null;
		try {
			StringBuilder ret;
			br = request.getReader();

			String line = br.readLine();
			if (line != null) {
				ret = new StringBuilder();
				ret.append(line);
			} else {
				return "";
			}

			while ((line = br.readLine()) != null) {
				ret.append('\n').append(line);
			}

			return ret.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	private static HttpURLConnection getHttpConnection2(String url, String method, Map<String, String> headers)
			throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		URL _url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
		if (conn instanceof HttpsURLConnection) {
			((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
			((HttpsURLConnection) conn).setHostnameVerifier(trustAnyHostnameVerifier);
		}

		conn.setRequestMethod(method);
		conn.setDoOutput(true);
		conn.setDoInput(true);

		conn.setConnectTimeout(19000);
		conn.setReadTimeout(19000);

		conn.setRequestProperty("Charset", CHARSET); // 设置编码
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");

		if (headers != null && !headers.isEmpty()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}

		return conn;
	}

	public static String post(String url, Map<String, String> headers, Map<String, String> queryParas, File file) {
		HttpURLConnection conn = null;
		try {
			String towHyphens = "--"; // 定义连接字符串
			String boundary = UUID.randomUUID().toString(); // 定义分界线字符串
			String end = "\r\n"; // 定义结束换行字符串

			conn = getHttpConnection2(buildUrlWithQueryString(url, queryParas), POST, headers);
			conn.setRequestProperty("Content-Type", "multipart/form-data" + ";boundary=" + boundary);
			conn.connect();

			if (file != null) {
				OutputStream out = conn.getOutputStream();
				DataOutputStream dataOutputStream = new DataOutputStream(out);

				String fileName = URLEncoder.encode(file.getName(), "UTF-8");
				dataOutputStream.writeBytes(towHyphens + boundary + end);
				dataOutputStream.writeBytes(
						"Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + end);
				dataOutputStream.writeBytes("Content-Type: application/octet-stream; charset=" + CHARSET + end);
				dataOutputStream.writeBytes(end);

				FileInputStream fileInputStream = new FileInputStream(file);
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];
				int length;
				while ((length = fileInputStream.read(buffer)) != -1) {
					dataOutputStream.write(buffer, 0, length);
				}
				dataOutputStream.writeBytes(end);
				fileInputStream.close();

				dataOutputStream.writeBytes(towHyphens + boundary + towHyphens + end);
				dataOutputStream.flush();

				dataOutputStream.close();
				out.flush();
				out.close();
			}

			String result = readResponseString(conn);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

	}
}
