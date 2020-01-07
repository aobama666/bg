package com.sgcc.bg.planCount.contrpller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.*;
import com.sgcc.bg.planCount.service.PlanBaseService;
import com.sgcc.bg.planCount.service.PlanExecutionService;
import com.sgcc.bg.planCount.service.PlanInputService;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "planBase")
public class PlanBaseController {
	private static Logger Logger = LoggerFactory.getLogger(PlanBaseController.class);
	@Autowired
	private WebUtils webUtils;
	@Autowired
	UserUtils userUtils;
	@Autowired
	private PlanBaseService planBaseService;
	/**
	 * 计划统计--专项类型
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForItem")
	public String selectForItem(String year){
		year=Rtext.toStringTrim(year, ""); //专项类别
		Map<String, Object> fundsSourceMap = new HashMap<String, Object>();
		fundsSourceMap.put("year",year);
		Map<String, Object>  map= new  HashMap<String, Object>();
		map.put("specalType","0");
		List<Map<String, Object>>	list=planBaseService.selectForCategoryInfo(map);
		if(list.isEmpty()){
			map.put("itmeList", "");
			map.put("success", "false");
			map.put("msg", "查询失败");
		}else {
			List<Map<String, Object>> fundsSourceList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> maps : list) {
				String id = String.valueOf(maps.get("SPECIAL_CODE"));
				String text = String.valueOf(maps.get("SPECIAL_NAME"));
				Map<String, Object> fundsSourceMaps = new HashMap<String, Object>();
				fundsSourceMaps.put("id", id);
				fundsSourceMaps.put("text", text);
				fundsSourceList.add(fundsSourceMaps);
			}
			map.put("itmeList", fundsSourceList);
			map.put("success", "ture");
			map.put("msg", "查询成功");
		}
		String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	/**
	 * 计划统计--根据专项类别获取资金来源,所谓联动
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForFundsSource")
	public String selectForFundsSource(String specalType,String epriCodes){
		specalType=Rtext.toStringTrim(specalType, ""); //专项类别
		epriCodes=Rtext.toStringTrim(epriCodes, ""); //基建类
		Map<String, Object> fundsSourceMap = new HashMap<String, Object>();
		fundsSourceMap.put("specalType",specalType);
		List<String>  arryFundsSource=new ArrayList<String>();
		if(!StringUtils.isEmpty(epriCodes)){
			//拆分id
			String[] arry = epriCodes.split(",");
			for (String specialCode : arry) {
				arryFundsSource.add(specialCode);
			}
			fundsSourceMap.put("epriCodes",arryFundsSource);
		}
		Map<String, Object>  map= new  HashMap<String, Object>();
		List<Map<String,Object>>	list = planBaseService.selectForFundsSourceInfo(fundsSourceMap);
		if(list.isEmpty()){
			map.put("fundsSourceList", "");
			map.put("success", "false");
			map.put("msg", "查询失败");
		}else {
			List<Map<String, Object>> fundsSourceList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> maps : list) {
				String id = String.valueOf(maps.get("FUNDS_SOURCE_CODE"));
				String text = String.valueOf(maps.get("FUNDS_SOURCE_NAME"));
				Map<String, Object> fundsSourceMaps = new HashMap<String, Object>();
				fundsSourceMaps.put("id", id);
				fundsSourceMaps.put("text", text);
				fundsSourceList.add(fundsSourceMaps);
			}
			map.put("fundsSourceList", fundsSourceList);
			map.put("success", "ture");
			map.put("msg", "查询成功");
		}
		String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	/**
	 * 计划投入情况-近三年发展投入趋势
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForYearAndDevelopListInfo")
	public String selectForYearInfo(String specalType){
		specalType=Rtext.toStringTrim(specalType, ""); //专项类别
		Map<String, Object>   baseMap = new HashMap<String, Object>();
		baseMap.put("specialType",specalType);
		List<Map<String,Object>>	yearAndDevelopList =planBaseService.selectForYearInfo(baseMap);
		Map<String, Object>  map= new  HashMap<String, Object>();
		map.put("yearAndDevelopList", yearAndDevelopList);
		map.put("success", "ture");
		map.put("msg", "查询成功");
		String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	/**
	 * 计划投入情况-近三年发展投入趋势
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForItemInfo")
	public String selectForItemInfo(String specalType,String year){
		specalType=Rtext.toStringTrim(specalType, ""); //专项类别
		year=Rtext.toStringTrim(year, ""); //专项类别
		Map<String, Object>   baseMap = new HashMap<String, Object>();
		baseMap.put("specialType",specalType);
		baseMap.put("year",year);
		List<String>  arrySpecialType=new ArrayList<String>();
		if(!StringUtils.isEmpty(specalType)){
			String[] arry = specalType.split(",");
			for (String specialType : arry) {
				arrySpecialType.add(specialType);
			}
			baseMap.put("specialTypes",arrySpecialType);
		}
		List<Map<String,Object>>	itemList =planBaseService.selectForItemInfo(baseMap);
		Map<String, Object>  map= new  HashMap<String, Object>();
		map.put("itemList", itemList);
		map.put("success", "ture");
		map.put("msg", "查询成功");
		String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
}
