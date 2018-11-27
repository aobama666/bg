package com.sgcc.bg.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.PageHelper;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.service.DutyService;

@Controller
@RequestMapping(value = "duty")
public class DutyController {
	@Autowired
	DutyService dutyService;
	
	/**
	 * 返回编辑页面
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String initPage(){
		return "bg/duty/duty";
	}
	
	/**
	 * 返回权限信息
	 * @return
	 */
	@RequestMapping(value = "/listDuties", method = RequestMethod.POST)
	@ResponseBody
	public String grantDuty(String username,String deptcode,String roleCode,Integer page, Integer limit,Map<String,Object> dataMap){
		username = Rtext.toStringTrim(username, "");
		deptcode = Rtext.toStringTrim(deptcode, "");
		roleCode = Rtext.toStringTrim(roleCode, "");
		List<Map<String,Object>> content = dutyService.getAllDuties(username,deptcode,roleCode);
		int start = 0;
		int end = 30;
		if(page != null && limit!=null){
			start = (page-1)*limit;
			end = page*limit;
		}
		PageHelper<Map<String, Object>>  pageHelper = new PageHelper<>(content, start, end);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("items", pageHelper.getResult());
		jsonMap.put("totalCount", pageHelper.getTotalNum());
		String jsonStr = JSON.toJSONString(jsonMap);
		return jsonStr;
	}
	
	/**
	 * 返回编辑页面
	 * @return
	 */
	@RequestMapping(value = "/addDuty", method = RequestMethod.POST)
	@ResponseBody
	public String addDuty(String empCode,String deptCode,String roleCode){
		/*if(Rtext.isEmpty(empCode) || Rtext.isEmpty(deptCode) || Rtext.isEmpty(roleCode)){
			return "数据不完整！";
		}*/
		//String result=syncService.addDuty(empCode,deptCode,roleCode);
		return "";
	}
}
