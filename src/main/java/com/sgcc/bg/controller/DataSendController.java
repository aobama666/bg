package com.sgcc.bg.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.service.DataSendService;

@Controller
@RequestMapping(value="/dateSend")
public class DataSendController {
	@Autowired
	DataSendService dataSendService;
	
	/*
	 * 推送页面
	 * */
	@ResponseBody
	@RequestMapping(value="/index")
	public ModelAndView index(){
		ModelAndView modelAndView = new ModelAndView("bg/dataSend/dataSend");
		return modelAndView;
	}
	/*
	 * 推送页面查询
	 * */
	@ResponseBody
	@RequestMapping(value="/queryList")
	public String queryList(HttpServletRequest request){
		return dataSendService.queryList(request);
	}
	/*
	 * 推送页面导出
	 * */
	@ResponseBody
	@RequestMapping(value="/queryListExport")
	public String queryListExport(HttpServletRequest request,HttpServletResponse response){
		return dataSendService.queryListExport(request,response);
	}
}
