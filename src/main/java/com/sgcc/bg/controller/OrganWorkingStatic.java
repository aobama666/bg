package com.sgcc.bg.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sgcc.bg.service.OrganWorkingStaticService;

@Controller
@RequestMapping(value="/organWorkingStatic")
public class OrganWorkingStatic {
	private Logger logger = Logger.getLogger(OrganWorkingStatic.class);
	
	@Autowired
	OrganWorkingStaticService organWorkingStaticService;
	
	@ResponseBody
	@RequestMapping(value="/index")
	public ModelAndView index(){
		ModelAndView modelAndView = new ModelAndView("bg/organWorkTime/organWorkingTime");
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value="/queryDept")
	public String queryDept(HttpServletRequest request){
		logger.info("[queryDept]: 组织工时统计查询列表 " );
		return organWorkingStaticService.queryDept(request);
	}
}
