package com.sgcc.bg.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.sgcc.bg.common.ConfigUtils;

@Controller
@RequestMapping(value="config")
public class ConfigController {
	
	@Autowired
	private ConfigUtils configUtils;
	
	/**
	 * 根据tb_config表 刷新 configMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/resetConfigMap")
	public String resetConfigMap(){
		configUtils.initConfigMap();
		Map<String, String> resultMap=new HashMap<String, String>();
		resultMap.put("retcode", "success");
		return JSONObject.toJSONString(resultMap);
	}
	
	/**
	 * 查询管理用户登录信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/userLoginManager")
	public ModelAndView userLoginManager(){
		ModelAndView model = new ModelAndView("adminManager/userLoginManager");
		return model;
	}
	
	/**
	 * 查询管理用户登录后操作记录
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/userLoginManagerInfo")
	public ModelAndView userLoginManagerInfo(){
		ModelAndView model = new ModelAndView("adminManager/userLoginManagerInfo");
		return model;
	}
	
	/**
	 * 查询用户登录日志列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getUserLoginList")
	public String getUserLoginList(HttpServletRequest request){
		return configUtils.getUserLoginList(request);
	}
	
	/**
	 * 查询用户登录后操作日志
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getUserLoginLog")
	public String getUserLoginLog(HttpServletRequest request){
		return configUtils.getUserLoginLog(request);
	}
}
