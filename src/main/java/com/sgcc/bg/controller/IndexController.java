package com.sgcc.bg.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sgcc.bg.service.SearchWorkTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sgcc.bg.common.RedisBeanWarp;
import com.sgcc.bg.common.WebUtils;

@Controller
@RequestMapping(value="/index")
public class IndexController {
	
	@Autowired
	private WebUtils webUtils;
	@Autowired
	SearchWorkTaskService searchWorkTaskService;

	private static Logger log =  LoggerFactory.getLogger(IndexController.class);
	/**
	 * 跳转未登录提示页
	 * @return
	 */
	@RequestMapping("/nologin")
	public String nologin(){
		 return "nologin";
	}
	
	/**
	 * 登陆后成功后跳转首页
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index(){
		 log.info("登录成功");
		 ModelAndView model = new ModelAndView("success");
		 return model;
	}
	
	/**
	 * 用户登录页面
	 * @return
	 */
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request){	
		String redirect = request.getParameter("redirect");
		log.info("登录页面，跳转页为： "+redirect);
		redirect = webUtils.redirectclean(redirect);
		Map<String,String> context = new HashMap<String,String>();
		context.put("redirect", redirect);
		ModelAndView model = new ModelAndView("login",context);
		return model;
	}
	
	/**
	 * 用户登录系统
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping("/loginsystem")
	public String loginsystem(HttpServletRequest requst,HttpServletResponse response) throws IOException{
		log.info("登录系统 ");
		RedisBeanWarp rbw = webUtils.login(requst,response);
		log.info("rbw ："+rbw);
		//加判断 跳转不同的页面
		String returnData = "";
		if(rbw.getValue() != null&&rbw.getValue().toString().contains("登录失败")){
			returnData = rbw.getValue().toString();
		}else if(rbw.getValue() != null){
			returnData = "1";
		}
		return returnData;
	}
	/**
	 * 用户退出系统
	 * @return
	 */
	@RequestMapping("/loginout")
	public ModelAndView loginout(HttpServletRequest requst,HttpServletResponse response){
		log.info("退出系统 ");
		webUtils.logOut(requst, response);
		ModelAndView model = new ModelAndView("login");
		return model;
	}

	/**
	 * 用户名自动补全
	 */
	@ResponseBody
	@RequestMapping("/autocompleteName")
	public String autocompleteName(HttpServletRequest request){

		return searchWorkTaskService.getUser(request);
	}
}
