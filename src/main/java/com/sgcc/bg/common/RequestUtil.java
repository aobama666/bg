package com.sgcc.bg.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestUtil {
	
	/**
	 * 将前端反馈的值映射到map中 其具体类型自行判断
	 * @param request
	 * @param str
	 * @return
	 */
	public static  Map<String,Object> doRequestParameter(HttpServletRequest request,String...str){
		Map<String,Object>map=new HashMap<String,Object>();
		if(str!=null)
			for(String st:str){
				if(request.getParameter(st)==null) throw new RuntimeException("the "+st+" value is  null"); 
				map.put(st,request.getParameter(st));
			};
		return map;
	}

}
