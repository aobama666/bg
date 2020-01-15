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
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userCode = currentUser.getHrCode();
		Map<String, Object>  AccessMap = new HashMap<>();
		AccessMap.put("userCode",userCode);
		List<Map<String, Object>>	accessInfoList=planBaseService.selectForMainrtainAccessInfo(AccessMap);
		Object   accessType=accessInfoList.get(0).get("ACCESS_TYPE");
		List<Map<String, Object>> specialList=null;
		if(accessType.equals("BUSINESS_MANAGEMENT")) {
			Object deptCode = accessInfoList.get(0).get("DEPT_CODE");
			AccessMap.put("deptCode", deptCode);
			AccessMap.put("year", year);
			specialList = planBaseService.selectForUserAccessInfo(AccessMap);
		}else{
			map.put("specalType","0");
			specialList=planBaseService.selectForCategoryInfo(map);
		}
		if(specialList.isEmpty()){
			map.put("itmeList", "");
			map.put("success", "false");
			map.put("msg", "查询失败");
		}else {
			List<Map<String, Object>> fundsSourceList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> maps : specialList) {
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
	public String selectForYearAndDevelopInfo(String specalType,String prctr){
		specalType=Rtext.toStringTrim(specalType, ""); //专项类别
		prctr=Rtext.toStringTrim(prctr, "");
		Map<String, Object>   baseMap = new HashMap<String, Object>();
		Map<String, Object>  yearMap = new HashMap<>();
		yearMap.put("num",3);
		List<Map<String, Object>>	yearList=planBaseService.selectForBaseYearInfo(yearMap);
		List<Map<String, Object>> yearAndDevelopList = new ArrayList<Map<String, Object>>();

		for(Map<String, Object> map:yearList){
			Object   year=map.get("year");
			baseMap.put("prctr",prctr);
			baseMap.put("year",year);
			baseMap.put("specialType",specalType);
			List<Map<String,Object>>	developList =planBaseService.selectForYearAndDevelopInfo(baseMap);
			Map<String, Object>  yearMaps = new HashMap<>();
			if(developList.isEmpty()){
				yearMaps.put("YEAR",year);
				yearMaps.put("TOTAL_INVEST",0);
				yearMaps.put("YEAR_INVEST",0);
				yearMaps.put("NUMBER_OF_ITEMS",0);
				yearMaps.put("CAPITAL_INVEST",0);
				yearMaps.put("COST_INVEST",0);
			}else{
				 yearMaps =developList.get(0);
			}
			yearAndDevelopList.add(yearMaps);
		}
		Map<String, Object>  map= new  HashMap<String, Object>();
		map.put("yearAndDevelopList", yearAndDevelopList);
		map.put("success", "ture");
		map.put("msg", "查询成功");
		String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	/**
	 * 计划投入情况-成本性和资本性投入趋势
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForCostAndCapitalInfo")
	public String selectForCostAndCapitalInfo(String specalType,String prctr){
		specalType=Rtext.toStringTrim(specalType, ""); //专项类别
		Map<String, Object>   baseMap = new HashMap<String, Object>();
		Map<String, Object>  yearMap = new HashMap<>();
		yearMap.put("num",3);
		List<Map<String, Object>>	yearList=planBaseService.selectForBaseYearInfo(yearMap);
		List<Map<String, Object>> yearAndDevelopList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map:yearList){
			Object   year=map.get("year");
			baseMap.put("year",year);
			baseMap.put("prctr",prctr);
			baseMap.put("specialType",specalType);
			List<Map<String,Object>>	developList =planBaseService.selectForCostAndCapitalInfo(baseMap);
			Map<String, Object>  yearMaps = new HashMap<>();
			if(developList.isEmpty()){
				yearMaps.put("YEAR",year);
				yearMaps.put("TOTAL_INVEST",0);
				yearMaps.put("YEAR_INVEST",0);
				yearMaps.put("NUMBER_OF_ITEMS",0);
				yearMaps.put("CAPITAL_INVEST",0);
				yearMaps.put("COST_INVEST",0);
			}else{
				yearMaps =developList.get(0);
			}
			yearAndDevelopList.add(yearMaps);
		}
		Map<String, Object>  map= new  HashMap<String, Object>();
		map.put("yearAndDevelopList", yearAndDevelopList);
		map.put("success", "ture");
		map.put("msg", "查询成功");
		String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	/**
	 * 计划投入情况-各专项年度投入情况
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForItemInfo")
	public String selectForItemInfo(String specalType,String year,String prctr){
		specalType=Rtext.toStringTrim(specalType, ""); //专项类别
		year=Rtext.toStringTrim(year, ""); //年份
		prctr=Rtext.toStringTrim(prctr, ""); //年份
		Map<String, Object>   baseMap = new HashMap<String, Object>();
		baseMap.put("year",year);
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userCode = currentUser.getHrCode();
		Map<String, Object>  AccessMap = new HashMap<>();
		AccessMap.put("userCode",userCode);
		List<Map<String, Object>>	accessInfoList=planBaseService.selectForMainrtainAccessInfo(AccessMap);
		Object   accessType=accessInfoList.get(0).get("ACCESS_TYPE");
		List<Map<String, Object> >  itemList=new ArrayList<Map<String, Object> >();

		if(accessType.equals("BUSINESS_MANAGEMENT")) {
			baseMap.put("prctr",prctr);
			if(!StringUtils.isEmpty(specalType)){
				String[] arry = specalType.split(",");
				for (String specialType : arry) {
					baseMap.put("specialType",specialType);
					List<Map<String,Object>>	List =planBaseService.selectForItemInfo(baseMap);
					Map<String, Object>  itemMaps = new HashMap<>();
					if(List.isEmpty()){
						itemMaps.put("YEAR",year);
						itemMaps.put("SPECIAL_CODE",specialType);
						Map<String, Object>  categoryMap = new HashMap<>();
						categoryMap.put("specalCode",specialType);
						List<Map<String,Object>>  specialList=planBaseService.selectForCategoryInfo(categoryMap);
						itemMaps.put("SPECIAL_NAME",specialList.get(0).get("SPECIAL_NAME"));
						itemMaps.put("TOTAL_INVEST",0);
						itemMaps.put("YEAR_INVEST",0);
						itemMaps.put("NUMBER_OF_ITEMS",0);
					}else {
						itemMaps=List.get(0);
					}
					itemList.add(itemMaps);
				}
			}else{
				Object deptCode = accessInfoList.get(0).get("DEPT_CODE");
				AccessMap.put("deptCode", deptCode);
				AccessMap.put("year", year);
				List<Map<String, Object> > 	specialList = planBaseService.selectForUserAccessInfo(AccessMap);
                if(!specialList.isEmpty()){
                    for(Map  specialMap:specialList){
					Object	specialType=specialMap.get("SPECIAL_CODE");
					Object  specialName=specialMap.get("SPECIAL_NAME");
						baseMap.put("specialType",specialType);
						List<Map<String,Object>>	List =planBaseService.selectForItemInfo(baseMap);
						Map<String, Object>  itemMaps = new HashMap<>();
						if(List.isEmpty()){
							itemMaps.put("YEAR",year);
							itemMaps.put("SPECIAL_CODE",specialType);
							itemMaps.put("SPECIAL_NAME",specialName);
							itemMaps.put("TOTAL_INVEST",0);
							itemMaps.put("YEAR_INVEST",0);
							itemMaps.put("NUMBER_OF_ITEMS",0);
						}else {
							itemMaps=List.get(0);
						}
						itemList.add(itemMaps);
					}
				}
			}
		   }else {
			if(!StringUtils.isEmpty(specalType)){
				String[] arry = specalType.split(",");
				for (String specialType : arry) {
					baseMap.put("specialType",specialType);
					List<Map<String,Object>>	List =planBaseService.selectForItemInfo(baseMap);
					Map<String, Object>  itemMaps = new HashMap<>();
					if(List.isEmpty()){
						itemMaps.put("YEAR",year);
						itemMaps.put("SPECIAL_CODE",specialType);
						Map<String, Object>  categoryMap = new HashMap<>();
						categoryMap.put("specalCode",specialType);
						List<Map<String,Object>>  specialList=planBaseService.selectForCategoryInfo(categoryMap);
						itemMaps.put("SPECIAL_NAME",specialList.get(0).get("SPECIAL_NAME"));
						itemMaps.put("TOTAL_INVEST",0);
						itemMaps.put("YEAR_INVEST",0);
						itemMaps.put("NUMBER_OF_ITEMS",0);
					}else {
						itemMaps=List.get(0);
					}
					itemList.add(itemMaps);
				}
			}else {
				Map<String, Object>  categoryMap = new HashMap<String, Object>();
				categoryMap.put("specalType","0");
				List<Map<String, Object> > 	specialList =planBaseService.selectForCategoryInfo(categoryMap);
				for(Map  specialMap:specialList){
					Object  specialName=specialMap.get("SPECIAL_NAME");
					Object	specialType=specialMap.get("SPECIAL_CODE");
					baseMap.put("specialType",specialType);
					List<Map<String,Object>>	List =planBaseService.selectForItemInfo(baseMap);
					Map<String, Object>  itemMaps = new HashMap<>();
					if(List.isEmpty()){
						itemMaps.put("YEAR",year);
						itemMaps.put("SPECIAL_CODE",specialType);
						itemMaps.put("SPECIAL_NAME",specialName);
						itemMaps.put("TOTAL_INVEST",0);
						itemMaps.put("YEAR_INVEST",0);
						itemMaps.put("NUMBER_OF_ITEMS",0);
					}else {
						itemMaps=List.get(0);
					}
					itemList.add(itemMaps);
				}
			}

		   }
		Map<String, Object>  map= new  HashMap<String, Object>();
		map.put("itemList", itemList);
		map.put("success", "ture");
		map.put("msg", "查询成功");
		String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	/**
	 * 计划执行情况-近综合计划执行进度-分类型
	 */
	@ResponseBody
	@RequestMapping(value = "/selectSubTypeInfo")
	public String selectSubTypeInfo(String year,String prctr){
		year=Rtext.toStringTrim(year, ""); //年份
		prctr=Rtext.toStringTrim(prctr, ""); //单位
		List<Map<String, Object>>	specialList= AccessList(year);//根据权限获取专项
		//List<Map<String, Object> >  executionList=new ArrayList<Map<String, Object> >();
//		for(Map<String, Object> specialMap:specialList){
//			Object  specialName=specialMap.get("SPECIAL_NAME");
//			Object  specialCode=specialMap.get("SPECIAL_CODE");
			Map<String, Object>   subTypeMap = new HashMap<String, Object>();
			subTypeMap.put("year",year);
			//subTypeMap.put("specialCode",specialCode);
			subTypeMap.put("prctr",prctr);
		//	Map<String, Object>  subTypes = new HashMap<>();
			List<Map<String,Object>>	executionList =planBaseService.selectSubTypeInfo(subTypeMap);
//			if(List.isEmpty()){
//				subTypes.put("YEAR",year);
//				subTypes.put("SPECIAL_CODE",specialCode);
//				subTypes.put("SPECIAL_NAME",specialName);
//				subTypes.put("ITEM_PROGRESS",0);
//			}else{
//				subTypes=List.get(0);
//			}
//			executionList.add(subTypes);
//		}

		Map<String, Object>  map= new  HashMap<String, Object>();
		map.put("executionList", executionList);
		map.put("success", "ture");
		map.put("msg", "查询成功");
		String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	/**
	 * 计划执行情况-近综合计划执行进度-分单位
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForSubUnitInfo")
	public String selectForSubUnitInfo(String year){
		year=Rtext.toStringTrim(year, ""); //年份
		Map<String, Object>   subUnitMap = new HashMap<String, Object>();
		subUnitMap.put("year",year);
		List<Map<String,Object>>	executionList =planBaseService.selectForSubUnitInfo(subUnitMap);
		Map<String, Object>  map= new  HashMap<String, Object>();
		map.put("executionList", executionList);
		map.put("success", "ture");
		map.put("msg", "查询成功");
		String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}







	/**
	 * 计划统计--计划执行情况-权限查询
	 */
	public  List<Map<String ,Object>>   AccessList(String year){
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userCode = currentUser.getHrCode();
		Map<String, Object>  AccessMap = new HashMap<>();
		AccessMap.put("userCode",userCode);
		List<Map<String, Object>> specialList=null;
		List<Map<String, Object>>	accessInfoList=planBaseService.selectForMainrtainAccessInfo(AccessMap);
		Object   accessType=accessInfoList.get(0).get("ACCESS_TYPE");
		if(accessType.equals("BUSINESS_MANAGEMENT")){
			Object   deptCode=accessInfoList.get(0).get("DEPT_CODE");
			AccessMap.put("deptCode",deptCode);
			AccessMap.put("year",year);
			specialList=planBaseService.selectForUserAccessInfo(AccessMap);
		}else{
			Map<String, Object> specialMap = new HashMap<>();
			specialMap.put("specalType","0");
			specialList=planBaseService.selectForCategoryInfo(specialMap);
		}
		return specialList;
	}

}
