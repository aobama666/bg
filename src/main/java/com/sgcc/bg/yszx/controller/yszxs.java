package com.sgcc.bg.yszx.controller;

 

 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
 

@Controller
@RequestMapping(value = "ideaInfo")
public class yszxs {
	private static Logger log = LoggerFactory.getLogger(yszxs.class);
	
	/**
	 * 返回列表展示页面
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String initPage(){
		return "yszx/yszx_idea_info";
	}
	
	/**
	 * 返回详情展示页面
	 * @return
	 */
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public String detailsPage(){
		return "yszx/yszx_idea_details";
	}
	/**
	 * 返回新增页面
	 * @return
	 */
	@RequestMapping(value = "/addPage", method = RequestMethod.GET)
	public String addPage(){
		return "yszx/yszx_idea_add";
	}
	/**
	 * 返回预定状态页面
	 * @return
	 */
	@RequestMapping(value = "/statePage", method = RequestMethod.GET)
	public String statePage(){
		return "yszx/yszx_idea_state";
	}

}
