package com.sgcc.bg.yszx.controller;

 

 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.yszx.service.IdeaInfoService;
 
 

@Controller
@RequestMapping(value = "yszx")
public class YSZXController {
	private static Logger log = LoggerFactory.getLogger(YSZXController.class);
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	DataDictionaryService dict;
	@Autowired
	private IdeaInfoService ideaInfoService;
	/**
	 * 返回列表展示页面
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String initPage(HttpServletRequest request){
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
	public ModelAndView addPage(){
		Map<String, String> map = new HashMap<>();
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		map.put("hrcode", currentUser.getHrCode());
		map.put("deptName", currentUser.getDeptName());
		map.put("deptCode", currentUser.getDeptCode());
		ModelAndView model = new ModelAndView("yszx/yszx_idea_add",map);
		return model;
		 
	}
	/**
	 * 返回修改页面
	 * @return
	 */
	@RequestMapping("/updatePage")
	public ModelAndView projectUpdate(String id, HttpServletRequest request) {
		Map<String, Object> proInfo = ideaInfoService.selectForId(id);
		ModelAndView model = new ModelAndView("yszx/yszx_idea_update", proInfo);
		return model;
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
