package com.sgcc.bg.common;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Http 简单封装
 * 
 * @author xuyw
 * @email xyw10000@163.com
 * @date 2012-06-14
 */
public class HttpUtil {
	// 连接超时时间
	private static final int CONNECTION_TIMEOUT = 3000;
	//读取超时时间
	private static final int READ_TIMEOUT = 5000;
	// 参数编码
	private static final String ENCODE_CHARSET = "utf-8";

	/**
	 * 调用对方接口方法
	 * @param path 对方或第三方提供的路径
	 * @param data 向对方或第三方发送的数据，大多数情况下给对方发送JSON数据让对方解析
	 */
	public static String  interfaceUtil(String path,String data) {
		StringBuilder resultData = new StringBuilder();
		try {
			URL url = new URL(path);
			//打开和url之间的连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			PrintWriter out = null;
			//请求方式
//          conn.setRequestMethod("POST");
//           //设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			//设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
			//最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
			//post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
			conn.setDoOutput(true);
			conn.setDoInput(true);
			//获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			//发送请求参数即数据
			out.print(data);
			//缓冲数据
			out.flush();
			//获取URLConnection对象对应的输入流
			InputStream is = conn.getInputStream();
			//构造一个字符流缓存
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str = "";
			while ((str = br.readLine()) != null) {
				resultData.append(str);
			}
			//关闭流
			is.close();
			//断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
			//固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
			conn.disconnect();
			System.out.println("完整结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultData.toString();
	}


	public   static  void  main(String[] args){
//		interfaceUtil("http://api.map.baidu.com/telematics/v3/weather?location=嘉兴&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ", "");
//		String reqURL="http://10.85.61.208:28233/zhywSi/WorkTimeService/getWorkTime";
//      interfaceUtil("http://192.168.10.89:8080/eoffice-restful/resources/sys/oadata", "usercode=10012");
       String   dd=  interfaceUtil("http://10.85.61.208:28233/zhywSi/WorkTimeService/getWorkTime",
                    "beginDate=2019-01-01&endDate=2019-01-31&key=f91f33c46f6f49a7be2d7012fef9ac57");

       System.out.println(dd);

	}
}