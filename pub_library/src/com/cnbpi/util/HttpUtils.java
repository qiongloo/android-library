package com.cnbpi.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cnbpi.app.DefaultConfig;
import com.cnbpi.exception.ExceptionManagers;

public class HttpUtils {

	public static final String UTF_8 = "UTF-8";
	public static final String DESC = "descend";
	public static final String ASC = "ascend";

	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 1;

	private static String appCookie;
	private static String appUserAgent;

	private int objId;
	private String objKey = "";
	private int objType;

	public static final String m_aTC_URL = "http://115.236.59.58:8077/";
	
	public int getObjId() {
		return objId;
	}

	public void setObjId(int objId) {
		this.objId = objId;
	}

	public String getObjKey() {
		return objKey;
	}

	public void setObjKey(String objKey) {
		this.objKey = objKey;
	}

	public int getObjType() {
		return objType;
	}

	public void setObjType(int objType) {
		this.objType = objType;
	}

	/**
	 * 解析url获得objId
	 * 
	 * @param path
	 * @param url_type
	 * @return
	 */
	private final static String parseObjId(String path, String url_type) {
		String objId = "";
		int p = 0;
		String str = "";
		String[] tmp = null;
		p = path.indexOf(url_type) + url_type.length();
		str = path.substring(p);
		if (str.contains(File.separator)) {
			tmp = str.split(File.separator);
			objId = tmp[0];
		} else {
			objId = str;
		}
		return objId;
	}

	/**
	 * 解析url获得objKey
	 * 
	 * @param path
	 * @param url_type
	 * @return
	 */
	private final static String parseObjKey(String path, String url_type) {
		try {
			path = URLDecoder.decode(path,"URF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String objKey = "";
		int p = 0;
		String str = "";
		String[] tmp = null;
		p = path.indexOf(url_type) + url_type.length();
		str = path.substring(p);
		if (str.contains("?")) {
			tmp = str.split("?");
			objKey = tmp[0];
		} else {
			objKey = str;
		}
		return objKey;
	}

	/**
	 * 对URL进行格式处理
	 * 
	 * @param path
	 * @return
	 */
	private final static String formatURL(String path) {
		if (path.startsWith("http://") || path.startsWith("https://"))
			return path;
		return "http://" + URLEncoder.encode(path);
	}

	private static String getCookie(Context appContext) {
		if (appCookie == null || appCookie == "") {
			PreferencesUtil.getString(appContext,
					DefaultConfig.PREFERENCES_HTTP, "cookie");

		}
		return appCookie;
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context appContext) {
		PackageInfo info = null;
		try {
			info = appContext.getPackageManager().getPackageInfo(
					appContext.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	private static String getUserAgent(Context appContext) {
		if (appUserAgent == null || appUserAgent == "") {
			StringBuilder ua = new StringBuilder("cnbpi.com");
			ua.append('/' + getPackageInfo(appContext).versionName + '_'
					+ getPackageInfo(appContext).versionCode);// App版本
			ua.append("/Android");// 手机系统平台
			ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
			ua.append("/" + android.os.Build.MODEL); // 手机型号
			appUserAgent = ua.toString();
		}
		return appUserAgent;
	}

	/**
	 * 创建 Httpclient
	 * 
	 * @return
	 */
	private static HttpClient getHttpClient() {
		HttpClient httpClient = new HttpClient();
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		httpClient.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
		// 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}

	private static GetMethod getHttpGet(String url, String cookie,
			String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		// httpGet.setRequestHeader("Host", URLs.HOST);
		httpGet.setRequestHeader("Connection", "Keep-Alive");
		httpGet.setRequestHeader("Cookie", cookie);
		httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}

	private static PostMethod getHttpPost(String url, String cookie,
			String userAgent) {
		PostMethod httpPost = new PostMethod(url);
		// 设置 请求超时时间
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		// httpPost.setRequestHeader("Host", URLs.HOST);
		httpPost.setRequestHeader("Connection", "Keep-Alive");
		httpPost.setRequestHeader("Cookie", cookie);
		httpPost.setRequestHeader("User-Agent", userAgent);
		// httpPost.setRequestHeader("Content-Type", "multipart/form-data");
		return httpPost;
	}

	/**
	 * 组装url
	 * 
	 * @param p_url
	 * @param params
	 * @return
	 */
	public static String http_makeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0)
			url.append('?');

		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			// 不做URLEncoder处理
			// url.append(URLEncoder.encode(String.valueOf(params.get(name)),
			// UTF_8));
		}

		return url.toString().replace("?&", "?");
	}

	/**
	 * get请求URL
	 * 
	 * @param url
	 * @throws ExceptionManagers
	 */
	public static InputStream http_get_is(Context appContext, String url)
			throws ExceptionManagers {
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;
		GetMethod httpGet = null;

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, cookie, userAgent);
				int statusCode = httpClient.executeMethod(httpGet);
				System.out.println("statusCode==> " +statusCode);
				if (statusCode != HttpStatus.SC_OK) {
					throw ExceptionManagers.http(statusCode);
				}
				responseBody = httpGet.getResponseBodyAsString();
				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw ExceptionManagers.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw ExceptionManagers.network(e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		/*
		 * responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		 * if(responseBody.contains("result") &&
		 * responseBody.contains("errorCode") &&
		 * appContext.containsProperty("user.uid")){ try { Result res =
		 * Result.parse(new ByteArrayInputStream(responseBody.getBytes()));
		 * if(res.getErrorCode() == 0){ appContext.Logout();
		 * appContext.getUnLoginHandler().sendEmptyMessage(1); } } catch
		 * (Exception e) { e.printStackTrace(); } }
		 */

		return new ByteArrayInputStream(responseBody.getBytes());

	}

	/**
	 * 公用post方法_有文件有参数值
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws ExceptionManagers
	 */
	public static InputStream http_post(Context appContext, String url,
			Map<String, Object> params, Map<String, File> files)
			throws ExceptionManagers {
		System.out.println("post_url==> " + url);
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;
		PostMethod httpPost = null;

		// post表单参数处理
		int length = (params == null ? 0 : params.size())
				+ (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null)
			for (String name : params.keySet()) {
				parts[i++] = new StringPart(name, String.valueOf(params
						.get(name)), UTF_8);
				System.out.println("post_key==> " + name + "    value==>"
						+ String.valueOf(params.get(name)));
			}
		if (files != null)
			for (String file : files.keySet()) {
				try {
					parts[i++] = new FilePart(file, files.get(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				System.out.println("post_key_file==> " + file);
			}

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, cookie, userAgent);
				httpPost.setRequestEntity(new MultipartRequestEntity(parts,
						httpPost.getParams()));
				int statusCode = httpClient.executeMethod(httpPost);
				System.out.println("statusCode==> " +statusCode);
				if (statusCode != HttpStatus.SC_OK) {
					throw ExceptionManagers.http(statusCode);
				} else if (statusCode == HttpStatus.SC_OK) {
					Cookie[] cookies = httpClient.getState().getCookies();
					String tmpcookies = "";
					for (Cookie ck : cookies) {
						tmpcookies += ck.toString() + ";";
					}
					// 保存cookie
					if (appContext != null && tmpcookies != "") {
						PreferencesUtil.putString(appContext,
								DefaultConfig.PREFERENCES_HTTP, "cookie",
								tmpcookies);
						appCookie = tmpcookies;
					}
				}
				responseBody = httpPost.getResponseBodyAsString();
				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw ExceptionManagers.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw ExceptionManagers.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		/*
		 * responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		 * if(responseBody.contains("result") &&
		 * responseBody.contains("errorCode") &&
		 * appContext.containsProperty("user.uid")){ try { Result res =
		 * Result.parse(new ByteArrayInputStream(responseBody.getBytes()));
		 * if(res.getErrorCode() == 0){ appContext.Logout();
		 * appContext.getUnLoginHandler().sendEmptyMessage(1); } } catch
		 * (Exception e) { e.printStackTrace(); } }
		 */
		return new ByteArrayInputStream(responseBody.getBytes());
	}

	/**
	 * 公用post方法 _ 纯参数值
	 * 
	 * @param url
	 * @param params
	 * @throws ExceptionManagers
	 */
	public static InputStream http_post_is(Context appContext, String url,
			Map<String, Object> params) throws ExceptionManagers {
		System.out.println("post_url==> " + url);
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;
		PostMethod httpPost = null;

		// post表单参数处理
		int length = params == null ? 0 : params.size();
		NameValuePair[] data = new NameValuePair[length];

		int i = 0;
		if (params != null) {
			for (String name : params.keySet()) {
				data[i++] = new NameValuePair(name, String.valueOf(params
						.get(name)));
				System.out.println("post_key==> " + name + "    value==>"
						+ String.valueOf(params.get(name)));
			}
		}

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				url=getHttpUrl(url);
				httpPost = getHttpPost(url, cookie, userAgent);
				httpPost.setRequestBody(data);
				int statusCode = httpClient.executeMethod(httpPost);
				System.out.println("statusCode==> " +statusCode);
				if (statusCode != HttpStatus.SC_OK) {
					throw ExceptionManagers.http(statusCode);
				} else if (statusCode == HttpStatus.SC_OK) {
					Cookie[] cookies = httpClient.getState().getCookies();
					String tmpcookies = "";
					for (Cookie ck : cookies) {
						tmpcookies += ck.toString() + ";";
					}
					// 保存cookie
					if (appContext != null && tmpcookies != "") {
						PreferencesUtil.putString(appContext,
								DefaultConfig.PREFERENCES_HTTP, "cookie",
								tmpcookies);
						appCookie = tmpcookies;
					}
				}
				responseBody = httpPost.getResponseBodyAsString();
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw ExceptionManagers.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw ExceptionManagers.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return new ByteArrayInputStream(responseBody.getBytes());
	}
	/**
	 * F返回http的url的地址
	 */
private static String getHttpUrl(String url){
	String strurl=m_aTC_URL+url;
	return strurl;
}
	/**
	 * 字节流转化为字符
	 * 
	 * @param inputStream
	 * @return
	 * @throws ExceptionManagers
	 */
	public static String convertToString(InputStream is)
			throws ExceptionManagers {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int i = -1;
		try {
			while ((i = is.read()) != -1) {
				bos.write(i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw ExceptionManagers.io(e);
		}
		return bos.toString();
	}

	
	
	/**
	 * post请求直接返回str
	 * @param appContext
	 * @param url
	 * @param params
	 * @return
	 * @throws ExceptionManagers
	 */
	public static String http_post_str(Context appContext, String url,
			Map<String, Object> params) throws ExceptionManagers {
			return convertToString(http_post_is(appContext, url, params));
	}
	
	
	/**
	 * post请求 重复数据 直接返回strs
	 * @param appContext
	 * @param url
	 * @param params
	 * @return
	 * @throws ExceptionManagers
	 */
	public static String http_post_strs(Context appContext, String url,
			NameValuePair[] data) throws ExceptionManagers {
			return convertToString(http_post_is(appContext, url,data));
	}
	/**
	 * get请求直接返回str
	 * @param appContext
	 * @param url
	 * @param params
	 * @return
	 * @throws ExceptionManagers
	 */
	public static String http_get_str(Context appContext, String url,
			Map<String, Object> params) throws ExceptionManagers {
			return convertToString(http_get_is(appContext, url));
	}
	
	/**
	 * 获取网络图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getNetBitmap(String url) throws ExceptionManagers {
		// System.out.println("image_url==> "+url);
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		Bitmap bitmap = null;
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, null, null);
				int statusCode = httpClient.executeMethod(httpGet);
				System.out.println("statusCode=" + statusCode);
				if (statusCode != HttpStatus.SC_OK) {
					throw ExceptionManagers.http(statusCode);
				}
				InputStream inStream = httpGet.getResponseBodyAsStream();
				bitmap = BitmapFactory.decodeStream(inStream);
				inStream.close();
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw ExceptionManagers.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw ExceptionManagers.network(e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return bitmap;
	}
	/**
	 * json解析成Map<String,String>形式
	 * 
	 * @param result
	 * @return
	 * @throws ExceptionManagers
	 */
	/*
	 * @SuppressWarnings("unchecked") public static Map<String, String>
	 * parseToMap(String result) throws ExceptionManagers {
	 * 
	 * Map<String, String> userData = null; ObjectMapper mapper = new
	 * ObjectMapper();
	 * 
	 * try {
	 * 
	 * userData = mapper.readValue(result, Map.class);
	 * 
	 * } catch (JsonParseException e) { e.printStackTrace(); throw
	 * ExceptionManagers.json(e); } catch (JsonMappingException e) {
	 * e.printStackTrace(); throw ExceptionManagers.json(e); } catch
	 * (IOException e) { e.printStackTrace(); throw ExceptionManagers.io(e); }
	 * 
	 * return userData;
	 * 
	 * }
	 */

	/**
	 * 公用post方法 _纯参数值_相同数值
	 * 
	 * @param url
	 * @param params
	 * @throws ExceptionManagers
	 */
	public static InputStream http_post_is(Context appContext, String url,
			NameValuePair[] data) throws ExceptionManagers {
		System.out.println("post_url==> " + url);
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;
		PostMethod httpPost = null;

		// post表单参数处理
		 data = null==data ?new NameValuePair[0]:data;
		 for(int i = 0;i<data.length;i++){
				System.out.println("post_key==> " + data[i].getName() + "    value==>"+ data[i].getValue());
		 }
		 
		 
		 
		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				url=getHttpUrl(url);
				httpPost = getHttpPost(url, cookie, userAgent);
				httpPost.setRequestBody(data);
				int statusCode = httpClient.executeMethod(httpPost);
				System.out.println("statusCode==> " +statusCode);
				if (statusCode != HttpStatus.SC_OK) {
					throw ExceptionManagers.http(statusCode);
				} else if (statusCode == HttpStatus.SC_OK) {
					Cookie[] cookies = httpClient.getState().getCookies();
					String tmpcookies = "";
					for (Cookie ck : cookies) {
						tmpcookies += ck.toString() + ";";
					}
					// 保存cookie
					if (appContext != null && tmpcookies != "") {
						PreferencesUtil.putString(appContext,
								DefaultConfig.PREFERENCES_HTTP, "cookie",
								tmpcookies);
						appCookie = tmpcookies;
					}
				}
				responseBody = httpPost.getResponseBodyAsString();
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw ExceptionManagers.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw ExceptionManagers.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return new ByteArrayInputStream(responseBody.getBytes());
	}
	
	
	
	
	
	
	
	
	
	
	

}
