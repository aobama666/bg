package com.sgcc.bg.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class IDUtils {
	public static void main(String[] args) {
		System.out.println("ID---->"+getUUID());
	}
	
	/**
	 * 获得一个去掉横杠'-'并全部转为大写的UUID字符串
	 * @return
	 */
	public static String getUUID(){
		String id=UUID.randomUUID().toString();
		id=id.replace("-", "");
		id=id.toUpperCase();
		return id;
	}
	
	/**
	 * 根据传入的标识字符串生成指定的时间戳字符串
	 * 如传入'aaa' 则返回 'aaa20170506121345632'
	 * @param beginStr
	 * @return
	 */
	public synchronized static String getTimeStampId(String beginStr){
		if(beginStr==null||"".equals(beginStr)){
			return "";
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String timeStamp=sdf.format(new Date());
		try {
			Thread.sleep(3);
			String id=beginStr+timeStamp;
			return id;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "";
		}
	}

}
