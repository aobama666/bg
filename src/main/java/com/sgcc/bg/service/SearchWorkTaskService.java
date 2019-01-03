package com.sgcc.bg.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.parser.deserializer.StringFieldDeserializer;
import com.sgcc.bg.model.Recode;

import javax.servlet.http.HttpServletRequest;

public interface SearchWorkTaskService {
	public String search(int page,int limit,String startTime,String endTime,String type,String projectName,String hrCode,String deptId);
	
	public String searchExamine(int page,int limit,String startTime,String endTime,String projectName,String type,String userName,String userCode,String hrCode);
	
	public List<Map<String,Object>> queryOutDelegationExport(String startTime,String endTime,String type,String projectName,String hrCode,List<String> ids);
	
	public String searchExamined(int page,int limit,String startTime,String endTime,String projectName,String type,String userName,String userCode,String hrName);
	
	public int confirmExamine(String id,String type,String recode,String dealUserName);
	
	public int saveRecode(Recode recode);
	
	public List<Map<String,Object>> queryAllExamine(String startTime,String endTime,String projectName,String type,String userName,String userCode,String hrName,List<String> ids);

	public String getUser(HttpServletRequest request);
}
