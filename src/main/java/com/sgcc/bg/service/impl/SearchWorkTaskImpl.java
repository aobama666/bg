package com.sgcc.bg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sgcc.bg.mapper.SearchWorkTaskMapper;
import com.sgcc.bg.model.Recode;
import com.sgcc.bg.service.SearchWorkTaskService;

@Service
public class SearchWorkTaskImpl implements SearchWorkTaskService{
	@Autowired
	SearchWorkTaskMapper searchWorkTaskMapper;
	
	public String search(int page,int limit,String startTime,String endTime,String type,String projectName,String hrCode,String deptId){
		Page<?> page2 = PageHelper.startPage(page, limit); 
		searchWorkTaskMapper.search(startTime,endTime,type,projectName,hrCode,deptId);
		long total = page2.getTotal();
		List<Map<String, String>> list = (List<Map<String, String>>) page2.getResult();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("items", list);
		map.put("totalCount", total);
		String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	
	public String searchExamine(int page,int limit,String startTime,String endTime,String projectName,String type,String userName,String userCode,String hrCode){
		Page<?> page2 = PageHelper.startPage(page, limit); 
		searchWorkTaskMapper.searchExamine(startTime,endTime,projectName,type,userName,userCode,hrCode);
		long total = page2.getTotal();
		List<Map<String, String>> list = (List<Map<String, String>>) page2.getResult();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("items", list);
		map.put("totalCount", total);
		String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	
	public String searchExamined(int page,int limit,String startTime,String endTime,String projectName,String type,String userName,String userCode,String hrName){
		Page<?> page2 = PageHelper.startPage(page, limit); 
		searchWorkTaskMapper.searchExamined(startTime,endTime,projectName,type,userName,userCode,hrName);
		long total = page2.getTotal();
		List<Map<String, Object>> list = (List<Map<String, Object>>) page2.getResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("items", list);
		map.put("totalCount", total);
		String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	@Override
	public List<Map<String, String>> queryOutDelegationExport(String startTime,String endTime,String type,String projectName,String hrCode,String deptId,List<String> ids) {
		List<Map<String, String>> resultList = searchWorkTaskMapper.search(startTime,endTime,type,projectName,hrCode,deptId);
		if(ids==null || ids.size()==0) return resultList; 
		
		List<Map<String, String>> newList = new ArrayList<>();
		for (String id : ids) {
			int index;
			try {
				index = Integer.parseInt(id);
			} catch (Exception e) {
				continue;
			}
			newList.add(resultList.get(index));
		}
		return newList;
		//return searchWorkTaskMapper.queryExportPrjs(startTime,endTime,type,projectName,hrCode,ids);
	}
	
	public int confirmExamine(String id,String type,String recode,String dealUserName){
		Date date = new Date();
		return searchWorkTaskMapper.confirmExamine(id,type,recode,dealUserName,date);
	}
	
	
	public int saveRecode(Recode recode){
		return searchWorkTaskMapper.saveRecode(recode);
	}
	
	public List<Map<String,Object>> queryAllExamine(String startTime,String endTime,String projectName,String type,String userName,String userCode,String hrName,List<String> ids){
		return searchWorkTaskMapper.searchExamineds(startTime,endTime,projectName,type,userName,userCode,hrName,ids);
	}

	//登陆自动补全
	public String getUser(HttpServletRequest request){
		String data = request.getParameter("data");
		Page<?> page2 = PageHelper.startPage(1, 5);
		searchWorkTaskMapper.getUser(data);
		List<Map<String, Object>> list = (List<Map<String, Object>>) page2.getResult();
		return JSON.toJSONString(list);
	};
}
