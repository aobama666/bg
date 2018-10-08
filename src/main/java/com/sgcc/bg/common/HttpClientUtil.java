package com.sgcc.bg.common;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

	private static final String CONTENT_TYPE_KEY = "Content-Type";
	private static final String CONTENT_TYPE_VALUE = "text/xml;charset=UTF-8";

	private static final String USER_AGENT_KEY = "User-Agent";
	private static final String USER_AGENT_VALUE = "Apache-HttpClient/4.1.1";

	private static final String ACCEPT_KEY = "Accept";
	private static final String ACCEPT_VALUE = "*/*";

	private static final String ACCEPT_LANGUAGE_KEY = "Accept-Language";
	private static final String ACCEPT_LANGUAGE_VALUE = "zh-cn";

	//private static final String ACCEPT_ENCODING_KEY = "Accept-Encoding";
	//private static final String ACCEPT_ENCODING_VALUE = "gzip, deflate";

	//private static final String PROXYHOSTNAME = "10.4.45.138";
	//private static final int PROXYPORT = 20002;

	// 默认编码
	//private static final String DEFAULT_ENCODING = "UTF-8";
	
	
	
	/**
	 * 获取http post对象
	 * @param serviceWsdl
	 * @param requestXml
	 * @return
	 */
	private static HttpPost getHttpPost(String serviceWsdl, String requestXml){
		try {
			HttpPost httpPost = new HttpPost(serviceWsdl);
			httpPost.setHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);
			httpPost.setHeader(ACCEPT_LANGUAGE_KEY, ACCEPT_LANGUAGE_VALUE);
			httpPost.setHeader(ACCEPT_KEY, ACCEPT_VALUE);
			httpPost.setHeader(USER_AGENT_KEY, USER_AGENT_VALUE);
			//httpPost.setHeader(ACCEPT_ENCODING_KEY, ACCEPT_ENCODING_VALUE);
			byte[] bytes = requestXml.getBytes();
			ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes, 0, bytes.length);
			httpPost.setEntity(byteArrayEntity);
			return httpPost;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取httpClient对象
	 * @return
	 */
	private static DefaultHttpClient getProxyHttpClient(){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// 设置请求链接超时
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		// 设置请求读取超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
		// 设置socket链接时间
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_KEEPALIVE, 10000);
		//设置网络代理(不设置)
		//HttpHost proxy = new HttpHost(PROXYHOSTNAME, PROXYPORT);
		httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,null);
		return httpClient;
	}
	
	/**
	 * 接收接口返回的数据流转为字符串
	 * @param httpResponse
	 * @return
	 */
	private static String parseHttpResponse(HttpResponse httpResponse){
		try {
			String uncompressResult = EntityUtils.toString(httpResponse.getEntity());
			uncompressResult = uncompressResult.replace("lt;", "<");
			uncompressResult = uncompressResult.replace("gt;", ">");
			return uncompressResult;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @Description	截取字符串中body标签内容
	 * @param responseXML
	 * @return 响应结果字符串
	 */
	@SuppressWarnings("unused")
	private static String removeSoapTag(String responseXML){
		if(null == responseXML || "".equals(responseXML)){
			return responseXML;
		}
		String beginTag = "<body>";
		String endTag = "</body>";
		return StringUtils.substringBetween(responseXML, beginTag, endTag);
	}
	
	/**
	 * 调用接口
	 * @param serviceWsdl 接口地址
	 * @param requestXml 要发送的xml字符串
	 * @return
	 */
	public static String sendPostRequest(String serviceWsdl, String requestXml){
		
		HttpPost httpPost = getHttpPost(serviceWsdl, requestXml);
		HttpClient httpClient = getProxyHttpClient();
		HttpResponse httpResponse  = null;
		try {
			httpResponse = httpClient.execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				return parseHttpResponse(httpResponse);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
