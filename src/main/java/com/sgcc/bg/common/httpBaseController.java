package com.sgcc.bg.common;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class httpBaseController {
	 public Logger log = LoggerFactory.getLogger(httpBaseController.class);
	/**
	 * 解析request请求
	 * 
	 * @param req
	 * @return
	 */
	@SuppressWarnings("null")
	/**
	 * 解析request请求
	 *
	 * @param req
	 * @return
	 */
	protected RequestInfo parseFromRequest(HttpServletRequest req,HttpServletResponse response){
		RequestInfo requestInfo=new RequestInfo();
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
		return requestInfo;
	}


	 

}
